<template>
  <div class="container mt-5">
    <h2 class="mb-4">Upravljanje igricama</h2>

    <div class="mb-3">
      <router-link to="/" class="btn btn-secondary me-2">←Početna</router-link>
      <button @click="$router.push('/admin/igre/dodaj')" class="btn btn-success me-2">+ Dodaj igricu</button>
    </div>

    <div class="card shadow">
      <div class="card-header bg-dark text-white">
        🎮 Sve igrice
      </div>
      <div class="card-body">
        <table class="table table-hover" style="table-layout: fixed; word-break: break-all;">
          <thead>
          <tr>
            <th>Naziv</th>
            <th>Kategorija</th>
            <th>URL</th>
            <th>Slika</th>
            <th>Status</th>
            <th>Akcije</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="igra in igre" :key="igra.id">
            <td>{{ igra.naziv }}</td>
            <td>{{ igra.kategorija || 'Bez kategorije' }}</td>
            <td>{{ igra.url }}</td>
            <td>{{ igra.slika }}</td>
            <td>
              <span v-if="igra.aktivna" class="badge bg-success">Aktivna</span>
              <span v-else class="badge bg-danger">Neaktivna</span>
            </td>
            <td>
              <button @click="$router.push('/admin/igre/izmeni/' + igra.id)"
                      class="btn btn-sm btn-info me-1">Izmeni</button>
              <button v-if="igra.aktivna"
                      @click="promeniAktivnost(igra)"
                      class="btn btn-sm btn-danger">Deaktiviraj</button>
              <button v-else
                      @click="promeniAktivnost(igra)"
                      class="btn btn-sm btn-success">Aktiviraj</button>
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
  name: 'AdminIgreView',
  data: function () {
    return {
      igre: []
    }
  },
  mounted: function () {
    axios.get('http://localhost:8080/api/igre/admin', { withCredentials: true })
      .then((res) => { this.igre = res.data })
      .catch((err) => {
        console.log(err)
        alert('Greška pri učitavanju igrica!')
      })
  },
  methods: {
    promeniAktivnost: function (igra) {
      axios.patch('http://localhost:8080/api/igre/' + igra.id + '/aktivnost',
        { aktivna: !igra.aktivna }, { withCredentials: true })
        .then(() => { igra.aktivna = !igra.aktivna })
        .catch((err) => {
          console.log(err)
          alert('Greška!')
        })
    }
  }
}
</script>
