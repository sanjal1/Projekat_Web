<template>
  <div class="container mt-5">
    <h2 class="mb-4">Monitoring</h2>

    <div v-if="ucitavanje" class="text-center">
      <p>Učitavanje...</p>
    </div>

    <div v-else>
      <!-- TABS -->
      <ul class="nav nav-tabs mb-4">
        <li class="nav-item">
          <a class="nav-link" :class="{ active: aktivan === 'sesije' }"
             @click="aktivan = 'sesije'" href="#">Sve sesije igranja</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" :class="{ active: aktivan === 'igre' }"
             @click="aktivan = 'igre'" href="#">Statistika po igrama</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" :class="{ active: aktivan === 'najigranije' }"
             @click="aktivan = 'najigranije'" href="#">Najigranije igrice</a>
        </li>
      </ul>

      <!-- SVE SESIJE IGRANJA -->
      <div v-if="aktivan === 'sesije'">
        <div class="card shadow">
          <div class="card-header bg-dark text-white">
            📋 Sve sesije igranja
          </div>
          <div class="card-body">
            <div v-if="sveSesije.length > 0">
              <table class="table table-hover">
                <thead>
                <tr>
                  <th>Korisnik</th>
                  <th>Igrica</th>
                  <th>Početak</th>
                  <th>Završetak</th>
                  <th>Trajanje</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="sesija in sveSesije" :key="sesija.id">
                  <td>{{ sesija.korisnikIme }} {{ sesija.korisnikPrezime }}</td>
                  <td>{{ sesija.igraNaziv }}</td>
                  <td>{{ formatirajDatum(sesija.vremePocetka) }}</td>
                  <td>{{ sesija.vremeZavrsetka ? formatirajDatum(sesija.vremeZavrsetka) : 'U toku' }}</td>
                  <td>{{ sesija.vremeZavrsetka ? formatirajVreme(sesija.trajanjeSekundi) : '-' }}</td>
                </tr>
                </tbody>
              </table>
            </div>
            <p v-else class="text-muted">Nema sesija igranja.</p>
          </div>
        </div>
      </div>

      <!-- STATISTIKA PO IGRAMA -->
      <div v-if="aktivan === 'igre'">
        <div class="card shadow">
          <div class="card-header bg-dark text-white">
            🎮 Statistika po igrama
          </div>
          <div class="card-body">
            <div v-if="najigranije.length > 0">
              <table class="table table-hover">
                <thead>
                <tr>
                  <th>Igrica</th>
                  <th>Ukupno vreme</th>
                  <th>Broj pokretanja</th>
                  <th>Detalji</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="igra in najigranije" :key="igra.igraId">
                  <td>{{ igra.nazivIgre }}</td>
                  <td>{{ formatirajVreme(igra.ukupnoVremeIgranja) }}</td>
                  <td>{{ igra.brojPokretanja }}</td>
                  <td>
                    <button @click="ucitajSesijeIgre(igra.igraId, igra.nazivIgre)"
                            class="btn btn-sm btn-info">
                      Pogledaj sesije
                    </button>
                  </td>
                </tr>
                </tbody>
              </table>

              <!-- Sesije odabrane igrice -->
              <div v-if="odabranaIgra" class="mt-4">
                <h5>Sesije za: {{ odabranaIgra }}</h5>
                <table class="table table-sm">
                  <thead>
                  <tr>
                    <th>Korisnik</th>
                    <th>Početak</th>
                    <th>Završetak</th>
                    <th>Trajanje</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="sesija in sesijeOdabraneIgre" :key="sesija.id">
                    <td>{{ sesija.korisnikIme }} {{ sesija.korisnikPrezime }}</td>
                    <td>{{ formatirajDatum(sesija.vremePocetka) }}</td>
                    <td>{{ sesija.vremeZavrsetka ? formatirajDatum(sesija.vremeZavrsetka) : 'U toku' }}</td>
                    <td>{{ sesija.vremeZavrsetka ? formatirajVreme(sesija.trajanjeSekundi) : '-' }}</td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
            <p v-else class="text-muted">Nema podataka.</p>
          </div>
        </div>
      </div>

      <!-- NAJIGRANIJE IGRICE -->
      <div v-if="aktivan === 'najigranije'">
        <div class="card shadow">
          <div class="card-header bg-dark text-white">
            🏆 Najigranije igrice u sistemu
          </div>
          <div class="card-body">
            <div v-if="najigranije.length > 0">
              <table class="table table-hover">
                <thead>
                <tr>
                  <th>#</th>
                  <th>Igrica</th>
                  <th>Ukupno vreme</th>
                  <th>Broj pokretanja</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(igra, index) in najigranije" :key="igra.igraId">
                  <td>{{ index + 1 }}</td>
                  <td>{{ igra.nazivIgre }}</td>
                  <td>{{ formatirajVreme(igra.ukupnoVremeIgranja) }}</td>
                  <td>{{ igra.brojPokretanja }}</td>
                </tr>
                </tbody>
              </table>
            </div>
            <p v-else class="text-muted">Nema podataka.</p>
          </div>
        </div>
      </div>
    </div>

    <div class="mt-3">
      <router-link to="/" class="btn btn-secondary">Nazad na početnu</router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AdminMonitoring',

  data() {
    return {
      aktivan: 'sesije',
      sveSesije: [],
      najigranije: [],
      odabranaIgra: null,
      sesijeOdabraneIgre: [],
      ucitavanje: true
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

    const [resSesije, resNajigranije] = await Promise.all([
      fetch('/api/statistika/admin/sve-sesije', { credentials: 'include' }),
      fetch('/api/statistika/admin/najigranije', { credentials: 'include' })
    ]);

    if (resSesije.ok) this.sveSesije = await resSesije.json();
    if (resNajigranije.ok) this.najigranije = await resNajigranije.json();

    this.ucitavanje = false;
  },

  methods: {
    async ucitajSesijeIgre(igraId, nazivIgre) {
      this.odabranaIgra = nazivIgre;
      const res = await fetch(`/api/statistika/admin/igra/${igraId}`, {
        credentials: 'include'
      });
      if (res.ok) {
        this.sesijeOdabraneIgre = await res.json();
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
