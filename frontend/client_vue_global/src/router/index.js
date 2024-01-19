import { createRouter, createWebHistory } from 'vue-router'

import Home from '../components/Home.vue'
import Form from '../components/Form.vue'
import Login from '../components/Login.vue'
import LoginAdmin from '../components/LoginAdmin.vue'
import AdminManager from '../components/AdminManager.vue'
import AboutPage from '../components/AboutPage.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/demandeur/:id',
      name: 'Login',
      component: Login,
      props: true
    },
    {
      path: '/demandeur/:id/home',
      name: 'Home',
      component: Home,
      props: true
    },
    {
      path: '/demandeur/:id/form',
      name: 'Form',
      component: Form,
      props: true
    },
    {
      path: '/admin/login',
      name: 'LoginAdmin',
      component: LoginAdmin
    },
    {
      path: '/admin/:id/home',
      name: 'HomeAdmin',
      component: Home,
      props: true
    },
    {
      path: '/demandeur/:id/about',
      name: 'AboutPage',
      component: AboutPage,
      props: true
    },
    {
      path: '/admin/:id/admin_manager',
      name: 'AdminManager',
      component: AdminManager,
      props: true
    }
  ]
})

export default router
