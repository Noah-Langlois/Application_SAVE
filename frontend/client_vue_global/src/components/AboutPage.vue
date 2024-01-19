<script setup>
import { useRouter, useRoute } from 'vue-router'
import { inject } from 'vue'
import AboutUs from './AboutUs.vue';
const router = useRouter()

const store = inject('STORE')

function changeRoute(value) {
    console.log(store.system.debug)
    router.push({
        name: value
  })
}


let URL = window.location.pathname
const myArray = URL.split("/")
if (store.state.token=='') {
  if (myArray[1]=='demandeur') {
    router.push({
            name: 'Login'
        })
  }
  if (myArray[1]=='admin') {
    router.push({
            name: 'LoginAdmin'
        })
  }
}

</script>

<template>
    <main style="min-height: 85vh;">
        <div class="row" >
            <div class="col">
                <h1 class="pt-4 ps-4">About SAVE</h1>
            </div>
        </div>
        <div class="row">
            <div class="col">
                <AboutUs/>
            </div>
        </div>
        <div class="row justify-content-center pt-5">
            <div class="col container text-center">
                <button class="btn btn-outline-dark" @click="changeRoute('Home')">Menu</button>
            </div>
        </div>
    </main>
</template>