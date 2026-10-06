<template>
  <div class="container mt-5">
    <h2 class="mb-4">Moj profil</h2>

    <!-- TABS -->
    <ul class="nav nav-tabs mb-4">
      <li class="nav-item">
        <a class="nav-link" :class="{ active: aktivan === 'podaci' }"
           @click="aktivan = 'podaci'" href="#">Lični podaci</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" :class="{ active: aktivan === 'slika' }"
           @click="aktivan = 'slika'" href="#">Profilna slika</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" :class="{ active: aktivan === 'lozinka' }"
           @click="aktivan = 'lozinka'" href="#">Promena lozinke</a>
      </li>
    </ul>

    <!-- LIČNI PODACI -->
    <div v-if="aktivan === 'podaci'" class="card shadow">
      <div class="card-body">
        <h5 class="card-title">Izmena ličnih podataka</h5>

        <div v-if="uspehPodaci" class="alert alert-success">{{ uspehPodaci }}</div>
        <div v-if="greskaPodaci" class="alert alert-danger">{{ greskaPodaci }}</div>

        <div class="mb-3">
          <label class="form-label">Ime</label>
          <input type="text" v-model="forma.ime" class="form-control">
        </div>
        <div class="mb-3">
          <label class="form-label">Prezime</label>
          <input type="text" v-model="forma.prezime" class="form-control">
        </div>
        <div class="mb-3">
          <label class="form-label">Email</label>
          <input type="email" v-model="forma.email" class="form-control">
        </div>
        <div class="mb-3">
          <label class="form-label">Datum rođenja</label>
          <input type="date" v-model="forma.datumRodjenja" class="form-control">
        </div>

        <button @click="sacuvajPodatke()" class="btn btn-primary">Sačuvaj</button>
      </div>
    </div>

    <!-- PROFILNA SLIKA -->
    <div v-if="aktivan === 'slika'" class="card shadow">
      <div class="card-body">
        <h5 class="card-title">Promena profilne slike</h5>

        <div v-if="uspehSlika" class="alert alert-success">{{ uspehSlika }}</div>
        <div v-if="greskaSlika" class="alert alert-danger">{{ greskaSlika }}</div>

        <div v-if="forma.profilnaSlika" class="mb-3">
          <label class="form-label">Trenutna slika</label><br>
          <img :src="forma.profilnaSlika" alt="Profilna slika"
               style="width: 100px; height: 100px; object-fit: cover; border-radius: 50%;">
        </div>

        <div class="mb-3">
          <label class="form-label">Putanja do nove slike</label>
          <input type="text" v-model="novaSlika" class="form-control"
                 placeholder="npr. /slike/moja-slika.jpg">
        </div>

        <button @click="sacuvajSliku()" class="btn btn-primary">Sačuvaj sliku</button>
      </div>
    </div>

    <!-- PROMENA LOZINKE -->
    <div v-if="aktivan === 'lozinka'" class="card shadow">
      <div class="card-body">
        <h5 class="card-title">Promena lozinke</h5>

        <div v-if="uspehLozinka" class="alert alert-success">{{ uspehLozinka }}</div>
        <div v-if="greskaLozinka" class="alert alert-danger">{{ greskaLozinka }}</div>

        <div class="mb-3">
          <label class="form-label">Stara lozinka</label>
          <input type="password" v-model="lozinka.staraLozinka" class="form-control">
        </div>
        <div class="mb-3">
          <label class="form-label">Nova lozinka</label>
          <input type="password" v-model="lozinka.novaLozinka" class="form-control">
        </div>
        <div class="mb-3">
          <label class="form-label">Potvrda nove lozinke</label>
          <input type="password" v-model="lozinka.potvrda" class="form-control">
        </div>

        <button @click="promeniLozinku()" class="btn btn-primary">Promeni lozinku</button>
      </div>
    </div>

    <div class="mt-3">
      <router-link to="/" class="btn btn-secondary">Nazad na početnu</router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ProfilView',

  data() {
    return {
      aktivan: 'podaci',
      korisnikId: null,
      forma: {
        ime: '',
        prezime: '',
        email: '',
        datumRodjenja: '',
        profilnaSlika: ''
      },
      novaSlika: '',
      lozinka: {
        staraLozinka: '',
        novaLozinka: '',
        potvrda: ''
      },
      uspehPodaci: '',
      greskaPodaci: '',
      uspehSlika: '',
      greskaSlika: '',
      uspehLozinka: '',
      greskaLozinka: ''
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

    // Popuni formu sa postojećim podacima
    this.forma.ime = korisnik.ime;
    this.forma.prezime = korisnik.prezime;
    this.forma.email = korisnik.email;
    this.forma.datumRodjenja = korisnik.datumRodjenja;
    this.forma.profilnaSlika = korisnik.profilnaSlika;
  },

  methods: {
    async sacuvajPodatke() {
      this.uspehPodaci = '';
      this.greskaPodaci = '';

      const res = await fetch(`/api/korisnici/${this.korisnikId}`, {
        method: 'PUT',
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          ime: this.forma.ime,
          prezime: this.forma.prezime,
          email: this.forma.email,
          datumRodjenja: this.forma.datumRodjenja,
          profilnaSlika: this.forma.profilnaSlika
        })
      });

      if (res.ok) {
        this.uspehPodaci = 'Podaci su uspešno sačuvani!';
      } else {
        this.greskaPodaci = await res.text();
      }
    },

    async sacuvajSliku() {
      this.uspehSlika = '';
      this.greskaSlika = '';

      if (!this.novaSlika.trim()) {
        this.greskaSlika = 'Unesite putanju do slike!';
        return;
      }

      const res = await fetch(`/api/korisnici/${this.korisnikId}/profilna-slika`, {
        method: 'PUT',
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(this.novaSlika)
      });

      if (res.ok) {
        const korisnik = await res.json();
        this.forma.profilnaSlika = korisnik.novaSlika;

        this.novaSlika = '';
        this.uspehSlika = 'Profilna slika je uspešno promenjena!';
      } else {
        this.greskaSlika = await res.text();
      }
    },

    async promeniLozinku() {
      this.uspehLozinka = '';
      this.greskaLozinka = '';

      if (this.lozinka.novaLozinka !== this.lozinka.potvrda) {
        this.greskaLozinka = 'Nova lozinka i potvrda se ne poklapaju!';
        return;
      }

      const res = await fetch(`/api/korisnici/${this.korisnikId}/lozinka`, {
        method: 'PUT',
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          staraLozinka: this.lozinka.staraLozinka,
          novaLozinka: this.lozinka.novaLozinka
        })
      });

      if (res.ok) {
        this.uspehLozinka = 'Lozinka je uspešno promenjena!';
        this.lozinka.staraLozinka = '';
        this.lozinka.novaLozinka = '';
        this.lozinka.potvrda = '';
      } else {
        this.greskaLozinka = await res.text();
      }
    }
  }
}
</script>
