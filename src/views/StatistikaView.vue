<template>
  <div class="container mt-5">
    <h2 class="mb-4">Moja statistika</h2>

    <div v-if="ucitavanje" class="text-center">
      <p>Učitavanje...</p>
    </div>

    <div v-else>
      <!-- UKUPNO VREME -->
      <div class="card shadow mb-4">
        <div class="card-body text-center">
          <h5 class="card-title">Ukupno vreme igranja</h5>
          <h2 class="text-primary">{{ formatirajVreme(statistika.ukupnoVremeIgranja) }}</h2>
        </div>
      </div>

      <!-- STATISTIKA PO IGRAMA -->
      <div class="card shadow mb-4">
        <div class="card-header bg-dark text-white">
          🎮 Najigranije igrice
        </div>
        <div class="card-body">
          <div v-if="statistika.statistikaPoIgrama && statistika.statistikaPoIgrama.length > 0">
            <table class="table table-hover">
              <thead>
              <tr>
                <th>Igrica</th>
                <th>Vreme igranja</th>
                <th>Broj pokretanja</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="igra in statistika.statistikaPoIgrama" :key="igra.igraId">
                <td>{{ igra.nazivIgre }}</td>
                <td>{{ formatirajVreme(igra.ukupnoVremeIgranja) }}</td>
                <td>{{ igra.brojPokretanja }}</td>
              </tr>
              </tbody>
            </table>
          </div>
          <p v-else class="text-muted">Još niste igrali nijednu igricu.</p>
        </div>
      </div>

      <!-- STATISTIKA PO KATEGORIJAMA -->
      <div class="card shadow mb-4">
        <div class="card-header bg-dark text-white">
          📂 Vreme igranja po kategorijama
        </div>
        <div class="card-body">
          <div v-if="statistika.statistikaPoKategorijama && statistika.statistikaPoKategorijama.length > 0">
            <table class="table table-hover">
              <thead>
              <tr>
                <th>Kategorija</th>
                <th>Ukupno vreme</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="kat in statistika.statistikaPoKategorijama" :key="kat.kategorijaNaziv">
                <td>{{ kat.kategorijaNaziv }}</td>
                <td>{{ formatirajVreme(kat.ukupnoVremeIgranja) }}</td>
              </tr>
              </tbody>
            </table>
          </div>
          <p v-else class="text-muted">Nema podataka po kategorijama.</p>
        </div>
      </div>
    </div>

    <router-link to="/" class="btn btn-secondary">Nazad na početnu</router-link>
  </div>
</template>

<script>
export default {
  name: 'StatistikaView',

  data() {
    return {
      statistika: {
        ukupnoVremeIgranja: 0,
        statistikaPoIgrama: [],
        statistikaPoKategorijama: []
      },
      korisnikId: null,
      ucitavanje: true
    }
  },

  async mounted() {
    // Proveri da li je ulogovan
    const resJa = await fetch('/api/korisnici/ja', {
      credentials: 'include'
    });

    if (!resJa.ok) {
      this.$router.push('/login');
      return;
    }

    const korisnik = await resJa.json();
    this.korisnikId = korisnik.id;

    // Učitaj statistiku
    const resStatistika = await fetch(`/api/statistika/korisnik/${this.korisnikId}`, {
      credentials: 'include'
    });

    if (resStatistika.ok) {
      this.statistika = await resStatistika.json();
    }

    this.ucitavanje = false;
  },

  methods: {
    formatirajVreme(sekunde) {
      if (!sekunde || sekunde === 0) return '0 min';

      const sati = Math.floor(sekunde / 3600);
      const minuti = Math.floor((sekunde % 3600) / 60);
      const sec = sekunde % 60;

      if (sati > 0) {
        return `${sati}h ${minuti}min ${sec}s`;
      } else if (minuti > 0) {
        return `${minuti}min ${sec}s`;
      } else {
        return `${sec}s`;
      }
    }
  }
}
</script>
