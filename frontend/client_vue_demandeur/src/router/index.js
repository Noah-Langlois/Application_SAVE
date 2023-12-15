import { createRouter, createWebHistory } from 'vue-router'

import Home from '../components/Home.vue'
import Chat from '../components/Chat.vue'
import Form from '../components/Form.vue'
import Login from '../components/Login.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/:id',
      name: 'Login',
      component: Login,
      props: true
    },
    {
      path: '/:id/home',
      name: 'Home',
      component: Home,
      props: true
    },
    {
      path: '/:id/chat',
      name: 'Chat',
      component: Chat,
      props: true
    },
    {
      path: '/:id/form',
      name: 'Form',
      component: Form,
      props: true
    }
  ]
})

export default router
