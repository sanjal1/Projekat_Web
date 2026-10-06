<template>
  <div class="container mt-5">
    <h2 class="mb-4">Igrice</h2>


    <div class="card shadow mb-4">
      <div class="card-body d-flex gap-2 align-items-center flex-wrap">
        <router-link to="/" class="btn btn-secondary">← Početna</router-link>

        <select v-model="izabranaKategorija" @change="filtriraj()" class="form-select" style="max-width: 220px;">
          <option value="">-- Sve kategorije --</option>
          <option v-for="k in kategorije" :key="k.id" :value="k.id">{{ k.naziv }}</option>
        </select>

        <button @click="sortirajPoOceni()" class="btn btn-info">Sortiraj po oceni</button>
        <button @click="ucitajSve()" class="btn btn-outline-secondary">Resetuj</button>
      </div>
    </div>

    <!-- lista igrica -->
    <div class="card shadow">
      <div class="card-header bg-dark text-white">
        🎮 Lista igrica
      </div>
      <div class="card-body">
        <div v-if="igre.length === 0" class="text-muted">Nema igrica.</div>

        <div v-for="igra in igre" :key="igra.id"
             class="d-flex align-items-center gap-3 mb-3 p-2"
             style="border-bottom: 1px solid #eee;">
          <img :src="igra.slika && igra.slika.startsWith('http') ? igra.slika : '/' + igra.slika"
               width="80" height="80" style="object-fit: cover; border-radius: 8px;" />
          <div class="flex-grow-1">
            <strong>{{ igra.naziv }}</strong>
            <span class="text-muted ms-2">{{ igra.kategorija || 'Bez kategorije' }}</span>
            <br />
            <small>⭐ {{ igra.prosecnaOcena }}</small>
          </div>
          <button @click="$router.push('/igra/' + igra.id)" class="btn btn-success">▶ Igraj</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: 'SveIgre',
  data: function () {
    return {
      igre: [],
      kategorije: [],
      izabranaKategorija: ''
    }
  },
  mounted: function () {
    this.ucitajSve()
    axios.get('http://localhost:8080/api/kategorije', { withCredentials: true })
      .then((res) => { this.kategorije = res.data })
      .catch((err) => console.log(err))
  },
  methods: {
    ucitajSve: function () {
      axios.get('http://localhost:8080/api/igre', { withCredentials: true })
        .then((res) => { this.igre = res.data })
        .catch((err) => console.log(err))
    },
    filtriraj: function () {
      if (!this.izabranaKategorija) {
        this.ucitajSve()
        return
      }
      axios.get('http://localhost:8080/api/igre/kategorija/' + this.izabranaKategorija,
        { withCredentials: true })
        .then((res) => { this.igre = res.data })
        .catch((err) => console.log(err))
    },
    sortirajPoOceni: function () {
      axios.get('http://localhost:8080/api/igre/sortirane', { withCredentials: true })
        .then((res) => { this.igre = res.data })
        .catch((err) => console.log(err))
    }
  }
}
</script>
