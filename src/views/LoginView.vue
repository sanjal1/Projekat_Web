<template>
  <div class="container mt-5">
    <div class="row justify-content-center">
      <div class="col-md-4">
        <div class="card shadow">
          <div class="card-body">
            <h3 class="text-center mb-4">Prijava</h3>

            <div v-if="greska" class="alert alert-danger">{{ greska }}</div>

            <div class="mb-3">
              <label class="form-label">Email</label>
              <input type="email" v-model="email" class="form-control" placeholder="vas@email.com">
            </div>
            <div class="mb-3">
              <label class="form-label">Lozinka</label>
              <input type="password" v-model="lozinka" class="form-control">
            </div>

            <button @click="login()" class="btn btn-primary w-100">Prijavi se</button>

            <p class="text-center mt-3">
              Nemate nalog?
              <router-link to="/registracija">Registrujte se</router-link>
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'LoginView',

  data() {
    return {
      email: '',
      lozinka: '',
      greska: ''
    }
  },

  async mounted() {
    // Proveri da li je već ulogovan, ako jeste idi na početnu
    const res = await fetch('/api/korisnici/ja', {
      credentials: 'include'
    });
    if (res.ok) {
      this.$router.push('/');
    }
  },

  methods: {
    async login() {
      const res = await fetch('/api/korisnici/login', {
        method: 'POST',
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          email: this.email,
          lozinka: this.lozinka
        })
      });

      if (res.ok) {
        const korisnik = await res.json();
        localStorage.setItem('uloga', korisnik.uloga);
        localStorage.setItem('korisnikId', korisnik.id);
        localStorage.setItem('ime', korisnik.ime);
        this.$router.push('/');
      } else {
        this.greska = await res.text();
      }
    }
  }
}
</script>
