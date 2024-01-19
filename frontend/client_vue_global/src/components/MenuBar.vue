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
    <nav class="navbar navbar-expand sticky-top" data-bs-theme="dark">
      <div class="container-fluid">
        <img src="../../img/savelogo.png" alt="logo" width="50" height="50" />
        <h3 class="ps-2 pe-2">SAVE</h3>
        <img src="../../img/baton.png" alt="logo" width="20" height="50" />
        <div id="navbarNav" class="collapse navbar-collapse ps-2">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item p-2">
              <a
                class="link-underline-dark link-underline-opacity-0 link-underline-opacity-100-hover"
                :class="{ active: isActiveRoute('Home') }"
                href="#"
                v-if="isActiveRoute('Home')||isActiveRoute('AboutPage')
                ||isActiveRoute('HomeAdmin')||
                isActiveRoute('AdminManager')||isActiveRoute('Form')"
                @click="changeRoute('Home')"
                style=" color: rgb(0, 0, 0);"
                >Home</a
              >
            </li>
            <li class="nav-item p-2">
              <a
                class="link-underline-dark link-underline-opacity-0 link-underline-opacity-100-hover"
                :class="{ active: isActiveRoute('Home') }"
                href="#"
                v-if="isActiveRoute('Home')||isActiveRoute('HomeAdmin')
                ||isActiveRoute('AdminManager')||isActiveRoute('Form')"
                @click="changeRoute('AboutPage')"
                style=" color: rgb(0, 0, 0);"
                >About</a
              >
            </li>
          </ul>
        </div>
      </div>
    </nav>
  </header>
</template>
