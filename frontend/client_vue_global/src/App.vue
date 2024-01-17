<script setup>
import { provide } from 'vue'
// Import RouterView component
import { RouterView } from 'vue-router'

import MenuBar from './components/MenuBar.vue'
import Footer from './components/Footer.vue'
import store from './store.js'

provide('STORE', store)
</script>

<template>
  <MenuBar />
  <div class="container-fluid" style="background-color: rgb(250, 239, 239);">
    <!-- Add RouterView component -->
    <RouterView />
    <Footer />
  </div>
</template>
