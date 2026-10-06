<template>
  <div class="container mt-5">
    <h2 class="mb-4">Upravljanje kategorijama</h2>

    <div class="mb-3">
      <router-link to="/" class="btn btn-secondary">←Početna</router-link>
    </div>
    <!-- dodaj -->
    <div class="card shadow mb-4">
      <div class="card-body d-flex gap-2">
        <input v-model="novaKat" placeholder="Unesi naziv kategorije..."
               class="form-control" style="max-width: 300px;" />
        <button @click="dodaj()" class="btn btn-success">+ Dodaj</button>
      </div>
      <div class="card-body pt-0" v-if="greska">
        <p class="text-danger mb-0">{{ greska }}</p>
      </div>
    </div>

    <!-- sve kategorije -->
    <div class="card shadow">
      <div class="card-header bg-dark text-white">
        📂 Sve kategorije
      </div>
      <div class="card-body">
        <table class="table table-hover">
          <thead>
          <tr>
            <th>ID</th>
            <th>Naziv</th>
            <th>Akcije</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="k in kategorije" :key="k.id">
            <td>{{ k.id }}</td>
            <td>
              <span v-if="!k.editMode">{{ k.naziv }}</span>
              <input v-else v-model="k.noviNaziv" class="form-control form-control-sm" />
            </td>
            <td>
              <button @click="toggleEdit(k)" class="btn btn-sm btn-info me-1">
                {{ k.editMode ? 'Sačuvaj' : 'Izmeni' }}
              </button>
              <button @click="obrisi(k.id)" class="btn btn-sm btn-danger">Obriši</button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: 'AdminKategorije',
  data: function () {
    return {
      kategorije: [],
      novaKat: '',
      greska: ''
    }
  },
  mounted: function () {
    this.ucitaj()
  },
  methods: {
    ucitaj: function () {
      axios.get('http://localhost:8080/api/kategorije', { withCredentials: true })
        .then((res) => {
          this.kategorije = res.data.map(k => ({
            ...k, editMode: false, noviNaziv: k.naziv
          }))
        })
        .catch((err) => console.log(err))
    },
    dodaj: function () {
      this.greska = ''
      if (!this.novaKat.trim()) {
        this.greska = 'Unesite naziv kategorije!'
        return
      }
      axios.post('http://localhost:8080/api/kategorije',
        { naziv: this.novaKat }, { withCredentials: true })
        .then(() => { this.novaKat = ''; this.ucitaj() })
        .catch((err) => { console.log(err); this.greska = 'Greška pri dodavanju!' })
    },
    toggleEdit: function (k) {
      if (k.editMode) {
        axios.put('http://localhost:8080/api/kategorije/' + k.id,
          { naziv: k.noviNaziv }, { withCredentials: true })
          .then(() => { k.naziv = k.noviNaziv; k.editMode = false })
          .catch((err) => { console.log(err); alert('Greška pri izmeni!') })
      } else {
        k.editMode = true
      }
    },
    obrisi: function (id) {
      if (!confirm('Da li ste sigurni?')) return
      axios.delete('http://localhost:8080/api/kategorije/' + id, { withCredentials: true })
        .then(() => { this.kategorije = this.kategorije.filter(k => k.id !== id) })
        .catch((err) => { console.log(err); alert('Greška pri brisanju!') })
    }
  }
}
</script>
