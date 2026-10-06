<template>
  <div class="container mt-5">
    <h2 class="mb-4">Dodaj igricu</h2>

    <div class="card shadow">
      <div class="card-header bg-dark text-white">
        🎮 Nova igrica
      </div>
      <div class="card-body">

        <div v-if="greska" class="alert alert-danger">{{ greska }}</div>

        <div class="mb-3">
          <label class="form-label">Naziv</label>
          <input v-model="igra.naziv" class="form-control" placeholder="Naziv igrice" />
        </div>

        <div class="mb-3">
          <label class="form-label">Opis</label>
          <textarea v-model="igra.opis" class="form-control" rows="3" placeholder="Opis igrice"></textarea>
        </div>

        <div class="mb-3">
          <label class="form-label">Putanja HTML</label>
          <input v-model="igra.url" class="form-control" placeholder="npr. games/naziv/index.html" />
        </div>

        <div class="mb-3">
          <label class="form-label">Putanja slike</label>
          <input v-model="igra.slika" class="form-control" placeholder="npr. games/naziv/icon.png" />
        </div>

        <div class="mb-3">
          <label class="form-label">Kategorija</label>
          <select v-model="igra.kategorija" class="form-select">
            <option value="">-- Izaberi kategoriju --</option>
            <option v-for="k in kategorije" :key="k.id" :value="k.naziv">{{ k.naziv }}</option>
          </select>
        </div>

        <div class="mb-3 form-check">
          <input type="checkbox" v-model="igra.aktivna" class="form-check-input" id="aktivnaCheck" />
          <label class="form-check-label" for="aktivnaCheck">Aktivna</label>
        </div>

        <div class="d-flex gap-2">
          <button @click="dodaj()" class="btn btn-success">Sačuvaj</button>
          <button @click="$router.push('/admin/igre')" class="btn btn-secondary">Odustani</button>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: 'DodajIgruView',
  data: function () {
    return {
      igra: {
        naziv: '',
        opis: '',
        url: '',
        slika: '',
        kategorija: '',
        aktivna: true
      },
      kategorije: [],
      greska: ''
    }
  },
  mounted: function () {
    axios.get('http://localhost:8080/api/kategorije', { withCredentials: true })
      .then((res) => { this.kategorije = res.data })
      .catch((err) => console.log(err))
  },
  methods: {
    dodaj: function () {
      this.greska = ''
      axios.post('http://localhost:8080/api/igre', this.igra, { withCredentials: true })
        .then(() => { this.$router.push('/admin/igre') })
        .catch((err) => {
          console.log(err)
          this.greska = err.response?.data || 'Greška pri dodavanju!'
        })
    }
  }
}
</script>
