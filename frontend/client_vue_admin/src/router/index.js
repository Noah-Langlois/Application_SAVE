import { createRouter, createWebHistory } from 'vue-router'

import Home from '../components/Home.vue'
import Chat from '../components/Chat.vue'
import Login from '../components/Login.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Login',
      component: Login,
      props: true
    },
    {
      path: '/home',
      name: 'Home',
      component: Home,
      props: true
    },
    {
      path: '/chat',
      name: 'Chat',
      component: Chat,
      props: true
    }
  ]
})

export default router
