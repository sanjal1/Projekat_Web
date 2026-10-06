<template>
  <div class="container mt-5">
    <div class="row justify-content-center">
      <div class="col-md-5">
        <div class="card shadow">
          <div class="card-body">
            <h3 class="text-center mb-4">Registracija</h3>

            <div v-if="greska" class="alert alert-danger">{{ greska }}</div>
            <div v-if="uspeh" class="alert alert-success">{{ uspeh }}</div>

            <div class="mb-3">
              <label class="form-label">Ime</label>
              <input type="text" v-model="ime" class="form-control">
            </div>
            <div class="mb-3">
              <label class="form-label">Prezime</label>
              <input type="text" v-model="prezime" class="form-control">
            </div>
            <div class="mb-3">
              <label class="form-label">Email</label>
              <input type="email" v-model="email" class="form-control">
            </div>
            <div class="mb-3">
              <label class="form-label">Lozinka</label>
              <input type="password" v-model="lozinka" class="form-control">
            </div>
            <div class="mb-3">
              <label class="form-label">Datum rođenja</label>
              <input type="date" v-model="datumRodjenja" class="form-control">
            </div>

            <button @click="registracija()" class="btn btn-success w-100">Registruj se</button>

            <p class="text-center mt-3">
              Već imate nalog?
              <router-link to="/login">Prijavite se</router-link>
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'RegistracijaView',

  data() {
    return {
      ime: '',
      prezime: '',
      email: '',
      lozinka: '',
      datumRodjenja: '',
      greska: '',
      uspeh: ''
    }
  },

  methods: {
    async registracija() {
      const res = await fetch('/api/korisnici/registracija', {
        method: 'POST',
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          ime: this.ime,
          prezime: this.prezime,
          email: this.email,
          lozinka: this.lozinka,
          datumRodjenja: this.datumRodjenja
        })
      });

      if (res.ok) {
        this.greska = '';
        this.uspeh = 'Uspešno ste se registrovali! Preusmeravamo vas na prijavu...';
        setTimeout(() => {
          this.$router.push('/login');
        }, 2000);
      } else {
        this.uspeh = '';
        this.greska = await res.text();
      }
    }
  }
}
</script>
