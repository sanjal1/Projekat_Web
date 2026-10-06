<template>
  <div>
    <!-- NAVIGACIJA -->
    <nav class="navbar navbar-dark bg-dark px-4">
      <span class="navbar-brand">🎮 GamePlatforma</span>
      <div>
        <router-link to="/igre" class="btn btn-outline-light me-2">Lista igrica</router-link>
        <!-- KORISNIK -->
        <template v-if="ulogovan && uloga === 'KORISNIK'">
          <router-link to="/statistika" class="btn btn-outline-light me-2">Moja statistika</router-link>
          <router-link to="/postignuca" class="btn btn-outline-light me-2">Postignuća</router-link>
          <router-link to="/profil" class="btn btn-outline-light me-2">Profil</router-link>
          <!-- PROFILNA SLIKA -->
          <img v-if="profilnaSlika"
               :src="profilnaSlika"
               style="width: 35px; height: 35px; border-radius: 50%; object-fit: cover;"
               class="me-2"
               @error="profilnaSlika = ''">
          <span class="text-white me-3">{{ ime }}</span>
          <button @click="logout()" class="btn btn-danger">Odjavi se</button>
        </template>

        <!-- ADMINISTRATOR -->
        <template v-else-if="ulogovan && uloga === 'ADMINISTRATOR'">
          <router-link to="/admin/dashboard" class="btn btn-outline-light me-2">Dashboard</router-link>
          <router-link to="/admin/monitoring" class="btn btn-outline-light me-2">Monitoring</router-link>
          <router-link to="/admin/korisnici" class="btn btn-outline-light me-2">Korisnici</router-link>
          <router-link to="/admin/igre" class="btn btn-outline-light me-2">Igrice</router-link>
          <router-link to="/admin/kategorije" class="btn btn-outline-light me-2">Kategorije</router-link>
          <span class="text-white me-3">{{ ime }} (Admin)</span>
          <button @click="logout()" class="btn btn-danger">Odjavi se</button>
        </template>

        <!-- NEPRIJAVLJEN -->
        <template v-else>
          <router-link to="/login" class="btn btn-outline-light me-2">Prijava</router-link>
          <router-link to="/registracija" class="btn btn-success">Registracija</router-link>
        </template>
      </div>
    </nav>

    <!-- HERO SEKCIJA -->
    <div class="bg-dark text-white text-center py-5">
      <h1>Dobrodošli na GamePlatformu!</h1>
      <p class="lead">Igrajte igrice, komunicirajte sa igračima i pratite statistiku</p>
      <div class="d-flex justify-content-center gap-4 mt-4">
        <div class="card bg-secondary text-white p-4">
          <h2>{{ brojKorisnika }}</h2>
          <p>Registrovanih korisnika</p>
        </div>
        <div class="card bg-secondary text-white p-4">
          <h2>{{ brojIgara }}</h2>
          <p>Dostupnih igrica</p>
        </div>
      </div>
      <router-link to="/igre" class="btn btn-success btn-lg mt-4">Pregledaj igrice</router-link>
    </div>

    <!-- CHAT -->
    <div class="container mt-5">
      <div class="row">
        <div class="col-md-6 offset-md-3">
          <div class="card">
            <div class="card-header bg-dark text-white">
              💬 Globalni chat
            </div>
            <div class="card-body" style="height: 300px; overflow-y: auto;" id="chatBox">
              <div v-for="(poruka, index) in poruke" :key="index"
                   class="mb-2 d-flex align-items-center">
                <!-- Profilna slika pored poruke -->
                <img v-if="poruka.profilnaSlika"
                     :src="poruka.profilnaSlika"
                     style="width: 30px; height: 30px; border-radius: 50%; object-fit: cover;"
                     class="me-2"
                     @error="poruka.profilnaSlika = ''">
                <div v-else
                     style="width: 30px; height: 30px; border-radius: 50%; background-color: #6c757d;"
                     class="me-2 d-flex align-items-center justify-content-center text-white">
                  {{ poruka.korisnikIme ? poruka.korisnikIme[0] : '?' }}
                </div>
                <div>
                <strong>{{ poruka.korisnikIme }}:</strong> {{ poruka.poruka }}
              </div>
                </div>
              <p v-if="poruke.length === 0" class="text-muted">Nema poruka još.</p>
            </div>
            <div class="card-footer" v-if="ulogovan">
              <div class="input-group">
                <input
                  type="text"
                  v-model="novaPoruka"
                  class="form-control"
                  placeholder="Unesite poruku..."
                  @keyup.enter="posaljiPoruku()"
                >
                <button @click="posaljiPoruku()" class="btn btn-primary">Pošalji</button>
              </div>
            </div>
            <div class="card-footer" v-else>
              <p class="text-muted mb-0">
                <router-link to="/login">Prijavite se</router-link> da biste učestvovali u chatu.
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import SockJS from 'sockjs-client'
import Stomp from 'stompjs'

export default {
  name: 'HomeView',

  data() {
    return {
      brojKorisnika: 0,
      brojIgara: 0,
      ulogovan: false,
      uloga: '',
      ime: '',
      profilnaSlika: '',
      poruke: [],
      novaPoruka: '',
      stompClient: null
    }
  },

  async mounted() {
    // Učitaj broj korisnika i igara
    const resKorisnici = await fetch('/api/korisnici/broj', {
      credentials: 'include'
    });
    this.brojKorisnika = await resKorisnici.json();

    const resIgre = await fetch('/api/igre/broj', {
      credentials: 'include'
    });
    this.brojIgara = await resIgre.json();

    // Proveri da li je ulogovan
    const resJa = await fetch('/api/korisnici/ja', {
      credentials: 'include'
    });
    if (resJa.ok) {
      const korisnik = await resJa.json();
      this.ulogovan = true;
      this.ime = korisnik.ime;
      this.uloga = korisnik.uloga;
      this.profilnaSlika = korisnik.profilnaSlika;
    }

    // Poveži se na WebSocket chat
    this.poveziChat();
  },

  beforeUnmount() {
    if (this.stompClient) {
      this.stompClient.disconnect();
    }
  },

  methods: {
    poveziChat() {
      const socket = new SockJS('/chat');
      this.stompClient = Stomp.over(socket);
      this.stompClient.debug = null; // isključi debug logove

      this.stompClient.connect({}, () => {
        this.stompClient.subscribe('/topic/messages', (message) => {
          const poruka = JSON.parse(message.body);
          this.poruke.push(poruka);

          // Skroluj dole automatski
          this.$nextTick(() => {
            const chatBox = document.getElementById('chatBox');
            if (chatBox) chatBox.scrollTop = chatBox.scrollHeight;
          });
        });
      });
    },

    posaljiPoruku() {
      if (!this.novaPoruka.trim()) return;

      this.stompClient.send('/app/send', {}, JSON.stringify({
        poruka: this.novaPoruka
      }));

      this.novaPoruka = '';
    },

    async logout() {
      await fetch('/api/korisnici/logout', {
        method: 'POST',
        credentials: 'include'
      });
      localStorage.clear();
      this.ulogovan = false;
      this.$router.push('/login');
    }
  }
}
</script>
