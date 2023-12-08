import { createRouter, createWebHistory } from 'vue-router'

import Home from '../components/Home.vue'
import Chat from '../components/Chat.vue'
import Form from '../components/Form.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/:id',
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
