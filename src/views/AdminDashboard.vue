<template>
  <div class="container mt-5">
    <h2 class="mb-4">Admin Dashboard</h2>

    <div v-if="ucitavanje" class="text-center">
      <p>Učitavanje...</p>
    </div>

    <div v-else>
      <!-- BROJEVI -->
      <div class="row mb-4">
        <div class="col-md-3">
          <div class="card shadow text-center bg-primary text-white">
            <div class="card-body">
              <h2>{{ brojKorisnika }}</h2>
              <p>Ukupno korisnika</p>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card shadow text-center bg-success text-white">
            <div class="card-body">
              <h2>{{ brojAktivnih }}</h2>
              <p>Aktivnih korisnika (30 dana)</p>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card shadow text-center bg-info text-white">
            <div class="card-body">
              <h2>{{ brojIgara }}</h2>
              <p>Ukupno igrica</p>
            </div>
          </div>
        </div>
      </div>

      <!-- NAJIGRANIJE IGRICE U 30 DANA -->
      <div class="card shadow mb-4">
        <div class="card-header bg-dark text-white">
          🎮 Najigranije igrice u poslednjih 30 dana
        </div>
        <div class="card-body">
          <div v-if="najigranije30Dana.length > 0">
            <table class="table table-hover">
              <thead>
              <tr>
                <th>#</th>
                <th>Igrica</th>
                <th>Broj pokretanja</th>
                <th>Ukupno vreme</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(igra, index) in najigranije30Dana" :key="igra.igraId">
                <td>{{ index + 1 }}</td>
                <td>{{ igra.nazivIgre }}</td>
                <td>{{ igra.brojPokretanja }}</td>
                <td>{{ formatirajVreme(igra.ukupnoVremeIgranja) }}</td>
              </tr>
              </tbody>
            </table>
          </div>
          <p v-else class="text-muted">Nema podataka za poslednjih 30 dana.</p>
        </div>
      </div>

      <!-- NAJAKTIVNIJI KORISNICI -->
      <div class="card shadow mb-4">
        <div class="card-header bg-dark text-white">
          👥 Najaktivniji korisnici
        </div>
        <div class="card-body">
          <div v-if="najaktivnijiKorisnici.length > 0">
            <table class="table table-hover">
              <thead>
              <tr>
                <th>#</th>
                <th>Korisnik</th>
                <th>Ukupno vreme igranja</th>
                <th>Broj pokretanja</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(korisnik, index) in najaktivnijiKorisnici" :key="korisnik.korisnikId">
                <td>{{ index + 1 }}</td>
                <td>{{ korisnik.imeKorisnika }} {{ korisnik.prezimeKorisnika }}</td>
                <td>{{ formatirajVreme(korisnik.ukupnoVremeIgranja) }}</td>
                <td>{{ korisnik.brojPokretanja }}</td>
              </tr>
              </tbody>
            </table>
          </div>
          <p v-else class="text-muted">Nema podataka o aktivnosti korisnika.</p>
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
  name: 'AdminDashboard',

  data() {
    return {
      brojKorisnika: 0,
      brojAktivnih: 0,
      brojIgara: 0,
      najigranije30Dana: [],
      najaktivnijiKorisnici: [],
      ucitavanje: true
    }
  },

  async mounted() {
    // Proveri da li je ulogovan i da li je admin
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

    // Učitaj sve podatke paralelno
    const [resKorisnici, resAktivni, resIgre, res30Dana, resNajaktivniji] = await Promise.all([
      fetch('/api/korisnici/broj', { credentials: 'include' }),
      fetch('/api/korisnici/broj-aktivnih', { credentials: 'include' }),
      fetch('/api/igre/broj', { credentials: 'include' }),
      fetch('/api/statistika/dashboard/najigranije-30-dana', { credentials: 'include' }),
      fetch('/api/statistika/dashboard/najaktivniji-korisnici', { credentials: 'include' })
    ]);

    this.brojKorisnika = await resKorisnici.json();
    this.brojAktivnih = await resAktivni.json();
    this.brojIgara = await resIgre.json();

    if (res30Dana.ok) this.najigranije30Dana = await res30Dana.json();
    if (resNajaktivniji.ok) this.najaktivnijiKorisnici = await resNajaktivniji.json();

    this.ucitavanje = false;
  },

  methods: {
    formatirajVreme(sekunde) {
      if (!sekunde || sekunde === 0) return '0s';
      const sati = Math.floor(sekunde / 3600);
      const minuti = Math.floor((sekunde % 3600) / 60);
      const sec = sekunde % 60;
      if (sati > 0) return `${sati}h ${minuti}min ${sec}s`;
      if (minuti > 0) return `${minuti}min ${sec}s`;
      return `${sec}s`;
    }
  }
}
</script>
