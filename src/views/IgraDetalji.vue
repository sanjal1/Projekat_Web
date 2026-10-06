<template>
  <div class="container mt-4" v-if="igra">

    <div class="mb-3">
      <router-link to="/igre" class="btn btn-secondary">← Nazad na igrice</router-link>
    </div>

    <!-- detlji o igrici -->
    <div class="card shadow mb-4">
      <div class="card-body d-flex gap-4 align-items-start">
        <img
          :src="igra.slika && igra.slika.startsWith('http') ? igra.slika : '/' + igra.slika"
          width="150" height="150" style="object-fit: cover; border-radius: 8px;" />
        <div>
          <h2>{{ igra.naziv }}</h2>
          <p class="text-muted">{{ igra.opis }}</p>
          <p><strong>Kategorija:</strong> {{ igra.kategorija || 'Bez kategorije' }}</p>
          <p><strong>Ocena:</strong> ⭐ {{ igra.prosecnaOcena }}</p>

          <!-- ulogovan -->
          <div v-if="ulogovan" class="d-flex gap-2">
            <button @click="otvoriIgricu()" :disabled="igraPokrenuta" class="btn btn-success">
              ▶ Igraj u punom ekranu
            </button>
            <button v-if="igraPokrenuta" @click="zavrsiIgranje()"
                    :disabled="zavrsavanjeUToku" class="btn btn-danger">
              ■ Završi igranje
            </button>
          </div>

          <!-- nije ulogovan -->
          <div v-else class="d-flex gap-2">
            <router-link to="/login" class="btn btn-primary">Prijavite se</router-link>
            <router-link to="/registracija" class="btn btn-outline-primary">Registrujte se</router-link>
          </div>
        </div>
      </div>
    </div>

    <!-- recenzije -->
    <div v-if="ulogovan">


      <div class="card shadow mb-4">
        <div class="card-header bg-dark text-white">✍️ Ostavi recenziju</div>
        <div class="card-body">
          <div class="mb-2">
            <label class="form-label">Ocena:</label>
            <select v-model="novaRecenzija.ocena" class="form-select" style="max-width: 150px;">
              <option v-for="n in [1,2,3,4,5]" :key="n" :value="n">{{ n }} ⭐</option>
            </select>
          </div>
          <div class="mb-2">
            <label class="form-label">Komentar:</label>
            <textarea v-model="novaRecenzija.komentar" rows="3" class="form-control"></textarea>
          </div>
          <button @click="ostaviRecenziju()" class="btn btn-primary">Sačuvaj recenziju</button>
          <p v-if="recenzijaUspeh" class="text-success mt-2">{{ recenzijaUspeh }}</p>
          <p v-if="recenzijaGreska" class="text-danger mt-2">{{ recenzijaGreska }}</p>
        </div>
      </div>

      <!-- sve recenzije -->
      <div class="card shadow">
        <div class="card-header bg-dark text-white">⭐ Recenzije</div>
        <div class="card-body">
          <div v-if="recenzije.length === 0" class="text-muted">Još nema recenzija.</div>
          <div v-for="r in recenzije" :key="r.id"
               class="mb-3 p-3" style="border-bottom: 1px solid #eee;">
            <strong>{{ r.korisnikIme }}</strong> —
            <span v-for="n in r.ocena" :key="n">⭐</span>
            <p class="mb-1 mt-1">{{ r.komentar }}</p>
            <small class="text-muted">{{ r.datum }}</small>
          </div>
        </div>
      </div>
    </div>

    <!-- nije ulogovan poruka -->
    <div v-else class="card shadow text-center p-4">
      <h5>Prijavite se da biste videli i ostavili recenzije</h5>
    </div>

  </div>
</template>

<script>
import axios from 'axios'
let gameWindow = null
export default {
  name: 'IgraDetalji',
  data: function () {
    return {
      igra: null,
      ulogovan: false,
      igraAktivna: false,
      recenzije: [],
      novaRecenzija: { ocena: 5, komentar: '' },
      recenzijaUspeh: '',
      recenzijaGreska: '',
      korisnikId: null,
      igraPokrenuta: false,
      zavrsavanjeUToku: false
    }
  },
  mounted: function () {
    const id = this.$route.params.id
    axios.get('http://localhost:8080/api/igre/' + id, { withCredentials: true })
      .then((res) => { this.igra = res.data.igra; this.recenzije = res.data.recenzije })
      .catch((err) => console.log(err))
    axios.get('http://localhost:8080/api/korisnici/ja', { withCredentials: true })
      .then((res) => { this.ulogovan = true; this.korisnikId = res.data.id })
      .catch(() => { this.ulogovan = false })
  },
  methods: {
    async otvoriIgricu() {
      if (!this.korisnikId) { alert("Morate biti prijavljeni."); return }
      try {
        await axios.post('http://localhost:8080/api/statistika/zapocni',
          { korisnikId: this.korisnikId, igraId: this.igra.id }, { withCredentials: true })
        const url = this.igra.url.startsWith('http') ? this.igra.url : '/' + this.igra.url
        gameWindow = window.open(url, '_blank')
        this.igraPokrenuta = true
      } catch (err) { alert(err.response?.data || 'Greška!') }
    },
    async zavrsiIgranje() {
      if (this.zavrsavanjeUToku) return
      this.zavrsavanjeUToku = true
      try {
        await axios.put('http://localhost:8080/api/statistika/zavrsi/' + this.korisnikId,
          {}, { withCredentials: true })
        if (gameWindow && !gameWindow.closed) gameWindow.close()
        gameWindow = null
        this.igraPokrenuta = false
        alert('Igranje je završeno.')
      } catch (err) { alert(err.response?.data || 'Greška!') }
      finally { this.zavrsavanjeUToku = false }
    },
    ostaviRecenziju: function () {
      const id = this.$route.params.id
      this.recenzijaUspeh = ''
      this.recenzijaGreska = ''
      axios.post('http://localhost:8080/api/recenzije/igra/' + id,
        this.novaRecenzija, { withCredentials: true })
        .then((res) => {
          this.recenzije.push(res.data)
          this.novaRecenzija = { ocena: 5, komentar: '' }
          this.recenzijaUspeh = 'Recenzija je sačuvana!'
        })
        .catch((err) => { this.recenzijaGreska = err.response?.data || 'Greška!' })
    }
  }
}
</script>
