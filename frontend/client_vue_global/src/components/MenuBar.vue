<script setup>
import { useRouter, useRoute } from 'vue-router'
import { inject } from 'vue'

const router = useRouter()
const route = useRoute()

const store = inject('STORE')



function isActiveRoute(value) {
  return route.name === value
}

function changeRoute(value) {
  console.log(store.system.debug)
  router.push({
    name: value
  })
}
</script>

<template>
  <header>
    <nav class="navbar navbar-expand-lg sticky-top bg-primary" data-bs-theme="dark">
      <div class="container-fluid">
        <img src="../../img/logo_save.png" alt="logo" width="50" height="50" />
        <div id="navbarNav" class="collapse navbar-collapse">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item">
              <a
                class="nav-link"
                :class="{ active: isActiveRoute('Home') }"
                href="#"
                @click="changeRoute('Home')"
                >Home</a
              >
            </li>
          </ul>
        </div>
      </div>
    </nav>
  </header>
</template>
