import { createRouter, createWebHistory } from 'vue-router'

// login i pocetna strana
import LoginView from "@/frontend/LoginView.vue";
import RegistracijaView from "@/frontend/RegistracijaView.vue";
import HomeView from "@/frontend/HomeView.vue"

//korisnik
import ProfilView from "@/frontend/ProfilView.vue";
import StatistikaView from "@/frontend/StatistikaView.vue";

// igrice
import SveIgre from "@/frontend/SveIgre.vue";
import IgraDetalji from "@/frontend/IgraDetalji.vue";
import DodajIgru from "@/frontend/DodajIgru.vue";
import IzmeniIgru from "@/frontend/IzmeniIgru.vue";

// admin
import AdminIgre from "@/frontend/AdminIgre.vue";
import AdminKategorija from "@/frontend/AdminKategorija.vue";
import AdminKorisnici from "@/frontend/AdminKorisnici.vue";
import AdminDashboard from "@/frontend/AdminDashboard.vue";
import AdminMonitoring from "@/frontend/AdminMonitoring.vue";
import Postignuca from "@/frontend/Postignuca.vue";




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
