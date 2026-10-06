import { createRouter, createWebHistory } from 'vue-router'

// login i pocetna strana
import LoginView from "@/views/LoginView.vue";
import RegistracijaView from "@/views/RegistracijaView.vue";
import HomeView from "@/views/HomeView.vue"

//korisnik
import ProfilView from "@/views/ProfilView.vue";
import StatistikaView from "@/views/StatistikaView.vue";

// igrice
import SveIgre from "@/views/SveIgre.vue";
import IgraDetalji from "@/views/IgraDetalji.vue";
import DodajIgru from "@/views/DodajIgru.vue";
import IzmeniIgru from "@/views/IzmeniIgru.vue";

// admin
import AdminIgre from "@/views/AdminIgre.vue";
import AdminKategorija from "@/views/AdminKategorija.vue";
import AdminKorisnici from "@/views/AdminKorisnici.vue";
import AdminDashboard from "@/views/AdminDashboard.vue";
import AdminMonitoring from "@/views/AdminMonitoring.vue";
import Postignuca from "@/views/Postignuca.vue";




// @ts-ignore
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/',                       name: 'home',             component: HomeView },
    { path: '/login',                  name: 'login',            component: LoginView },
    { path: '/registracija',           name: 'registracija',     component: RegistracijaView },
    {path: '/profil',                  name: 'profil',           component: ProfilView},
    {path: '/statistika',              name: 'statistika',       component: StatistikaView},
    { path: '/igre',                   name: 'igre',             component: SveIgre },
    { path: '/igra/:id',               name: 'igra-detalji',     component: IgraDetalji },
    { path: '/admin/igre',             name: 'admin-igre',       component: AdminIgre },
    { path: '/admin/igre/dodaj',       name: 'dodaj-igru',       component: DodajIgru },
    { path: '/admin/igre/izmeni/:id',  name: 'izmeni-igru',      component: IzmeniIgru },
    { path: '/admin/kategorije',       name: 'admin-kategorije', component: AdminKategorija },
    { path: '/admin/korisnici',        name: 'admin-korisnici',  component: AdminKorisnici },
    { path: '/admin/dashboard',        name: 'admin-dashboard',  component: AdminDashboard },
    { path: '/admin/monitoring',       name: 'admin-monitoring', component: AdminMonitoring},
    { path: '/postignuca',             name: 'postignuca',       component: Postignuca }
  ],
})

export default router
