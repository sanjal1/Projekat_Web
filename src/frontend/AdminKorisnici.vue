<template>
  <div class="container mt-5">
    <h2 class="mb-4">Upravljanje korisnicima</h2>

    <div v-if="ucitavanje" class="text-center">
      <p>Učitavanje...</p>
    </div>

    <div v-else>
      <!-- PRETRAGA -->
      <div class="mb-3">
        <input type="text" v-model="pretraga" class="form-control"
               placeholder="Pretražite korisnike po imenu ili emailu..."
               @input="pretragaKorisnika()">
      </div>

      <!-- LISTA KORISNIKA -->
      <div v-if="!odabraniKorisnik">
        <div class="card shadow">
          <div class="card-header bg-dark text-white">
            👥 Svi korisnici
          </div>
          <div class="card-body">
            <table class="table table-hover">
              <thead>
              <tr>
                <th>Ime i prezime</th>
                <th>Email</th>
                <th>Datum registracije</th>
                <th>Status</th>
                <th>Akcije</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="korisnik in prikazaniKorisnici" :key="korisnik.id">
                <td>{{ korisnik.ime }} {{ korisnik.prezime }}</td>
                <td>{{ korisnik.email }}</td>
                <td>{{ formatirajDatum(korisnik.datumRegistracije) }}</td>
                <td>
                  <span v-if="korisnik.blokiran" class="badge bg-danger">Blokiran</span>
                  <span v-else class="badge bg-success">Aktivan</span>
                </td>
                <td>
                  <button @click="pogledajStatistiku(korisnik)"
                          class="btn btn-sm btn-info me-1">
                    Statistika
                  </button>
                  <button v-if="!korisnik.blokiran"
                          @click="blokiraj(korisnik)"
                          class="btn btn-sm btn-danger me-1">
                    Blokiraj
                  </button>
                  <button v-else
                          @click="odblokiraj(korisnik)"
                          class="btn btn-sm btn-success">
                    Odblokiraj
                  </button>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- STATISTIKA ODABRANOG KORISNIKA -->
      <div v-if="odabraniKorisnik">
        <div class="d-flex justify-content-between align-items-center mb-3">
          <h4>Statistika za: {{ odabraniKorisnik.ime }} {{ odabraniKorisnik.prezime }}</h4>
          <button @click="odabraniKorisnik = null" class="btn btn-secondary">
            ← Nazad na listu
          </button>
        </div>

        <div v-if="ucitavanjeStatistike" class="text-center">
          <p>Učitavanje statistike...</p>
        </div>

        <div v-else>
          <!-- UKUPNO VREME -->
          <div class="card shadow mb-3">
            <div class="card-body text-center">
              <h5>Ukupno vreme igranja</h5>
              <h3 class="text-primary">
                {{ formatirajVreme(statistikaKorisnika.ukupnoVremeIgranja) }}
              </h3>
            </div>
          </div>

          <!-- PO IGRAMA -->
          <div class="card shadow mb-3">
            <div class="card-header bg-dark text-white">
              🎮 Po igricama
            </div>
            <div class="card-body">
              <div v-if="statistikaKorisnika.statistikaPoIgrama && statistikaKorisnika.statistikaPoIgrama.length > 0">
                <table class="table table-hover">
                  <thead>
                  <tr>
                    <th>Igrica</th>
                    <th>Vreme igranja</th>
                    <th>Broj pokretanja</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="igra in statistikaKorisnika.statistikaPoIgrama" :key="igra.igraId">
                    <td>{{ igra.nazivIgre }}</td>
                    <td>{{ formatirajVreme(igra.ukupnoVremeIgranja) }}</td>
                    <td>{{ igra.brojPokretanja }}</td>
                  </tr>
                  </tbody>
                </table>
              </div>
              <p v-else class="text-muted">Korisnik još nije igrao nijednu igricu.</p>
            </div>
          </div>

          <!-- PO KATEGORIJAMA -->
          <div class="card shadow mb-3">
            <div class="card-header bg-dark text-white">
              📂 Po kategorijama
            </div>
            <div class="card-body">
              <div v-if="statistikaKorisnika.statistikaPoKategorijama && statistikaKorisnika.statistikaPoKategorijama.length > 0">
                <table class="table table-hover">
                  <thead>
                  <tr>
                    <th>Kategorija</th>
                    <th>Ukupno vreme</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="kat in statistikaKorisnika.statistikaPoKategorijama" :key="kat.kategorijaNaziv">
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
      </div>
    </div>

    <div class="mt-3" v-if="!odabraniKorisnik">
      <router-link to="/" class="btn btn-secondary">Nazad na početnu</router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AdminKorisnici',

  data() {
    return {
      korisnici: [],
      prikazaniKorisnici: [],
      pretraga: '',
      odabraniKorisnik: null,
      statistikaKorisnika: {
        ukupnoVremeIgranja: 0,
        statistikaPoIgrama: [],
        statistikaPoKategorijama: []
      },
      ucitavanje: true,
      ucitavanjeStatistike: false,
      poruka: '',
      greska: ''
    }
  },

  async mounted() {
    const resJa = await fetch('/api/korisnici/ja', {
      credentials: 'include'
    });

    if (!resJa.ok) {
      this.$router.push('/login');
      return;
    }

    const korisnik = await resJa.json();
    if (korisnik.uloga !== 'ADMINISTRATOR') {
      this.$router.push('/');
      return;
    }

    await this.ucitajKorisnike();
    this.ucitavanje = false;
  },

  methods: {
    async ucitajKorisnike() {
      const res = await fetch('/api/korisnici', {
        credentials: 'include'
      });
      if (res.ok) {
        this.korisnici = await res.json();
        this.prikazaniKorisnici = this.korisnici;
      }
    },

    pretragaKorisnika() {
      if (!this.pretraga.trim()) {
        this.prikazaniKorisnici = this.korisnici;
        return;
      }
      const tekst = this.pretraga.toLowerCase();
      this.prikazaniKorisnici = this.korisnici.filter(k =>
        k.ime.toLowerCase().includes(tekst) ||
        k.prezime.toLowerCase().includes(tekst) ||
        k.email.toLowerCase().includes(tekst)
      );
    },

    async pogledajStatistiku(korisnik) {
      this.odabraniKorisnik = korisnik;
      this.ucitavanjeStatistike = true;

      const res = await fetch(`/api/statistika/korisnik/${korisnik.id}`, {
        credentials: 'include'
      });

      if (res.ok) {
        this.statistikaKorisnika = await res.json();
      }

      this.ucitavanjeStatistike = false;
    },

    async blokiraj(korisnik) {
      const res = await fetch(`/api/korisnici/${korisnik.id}/blokiraj`, {
        method: 'PUT',
        credentials: 'include'
      });

      if (res.ok) {
        korisnik.blokiran = true;
      }
    },

    async odblokiraj(korisnik) {
      const res = await fetch(`/api/korisnici/${korisnik.id}/odblokiraj`, {
        method: 'PUT',
        credentials: 'include'
      });

      if (res.ok) {
        korisnik.blokiran = false;
      }
    },

    formatirajVreme(sekunde) {
      if (!sekunde || sekunde === 0) return '0s';
      const sati = Math.floor(sekunde / 3600);
      const minuti = Math.floor((sekunde % 3600) / 60);
      const sec = sekunde % 60;
      if (sati > 0) return `${sati}h ${minuti}min ${sec}s`;
      if (minuti > 0) return `${minuti}min ${sec}s`;
      return `${sec}s`;
    },

    formatirajDatum(datum) {
      if (!datum) return '-';
      const d = new Date(datum);
      return d.toLocaleString('sr-RS');
    }
  }
}
</script>
