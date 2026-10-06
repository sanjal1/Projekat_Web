<template>
  <div class="container mt-5">
    <h2 class="mb-4">Moja postignuća</h2>

    <div class="mb-3">
      <router-link to="/" class="btn btn-secondary">← Početna</router-link>
    </div>

    <div class="card shadow">
      <div class="card-header bg-dark text-white">
        🏆 Postignuća
      </div>
      <div class="card-body">
        <div v-if="postignuca.length === 0" class="text-muted">
          Još nemate postignuća. Počnite da igrate!
        </div>

        <div v-for="p in postignuca" :key="p.id"
             class="d-flex align-items-center gap-3 mb-3 p-3"
             style="border: 1px solid #eee; border-radius: 8px;">
          <div style="font-size: 2rem;">🏆</div>
          <div>
            <strong>{{ p.naziv }}</strong>
            <p class="mb-0 text-muted">{{ p.opis }}</p>
            <small class="text-muted" v-if="p.igra">Igrica: {{ p.igra }}</small>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: 'PostignucaView',
  data: function () {
    return {
      postignuca: [],
      korisnikId: null
    }
  },
  mounted: function () {
    axios.get('http://localhost:8080/api/korisnici/ja', { withCredentials: true })
      .then((res) => {
        this.korisnikId = res.data.id
        return axios.get('http://localhost:8080/api/postignuca/korisnik/' + res.data.id,
          { withCredentials: true })
      })
      .then((res) => { this.postignuca = res.data })
      .catch((err) => { console.log(err); this.$router.push('/login') })
  }
}
</script>
