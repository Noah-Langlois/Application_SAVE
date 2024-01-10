<script setup>
import { useRouter, useRoute } from 'vue-router'
import { inject } from 'vue'

const router = useRouter()

const store = inject('STORE')

function changeRoute(value) {
  console.log(store.system.debug)
  router.push({
    name: value
  })
}

</script>

<template>
    <main>
        <div class="mt-5">
            <h1>Gestion des administrateurs</h1>
        </div>
        <div class="mt-5">
            <button @click="changeRoute('HomeAdmin')">Retour</button>
        </div>
    </main>
</template>