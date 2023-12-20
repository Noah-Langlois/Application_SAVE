<script setup>
import { useRouter, useRoute } from 'vue-router'
import { inject } from 'vue'

const router = useRouter()
const route = useRoute()
const store = inject('STORE')
store.methods.setUserType('demandeur')



function checkPassword(value,user) {
    store.methods.getChatrooms(user, document.getElementById("login_password").value, value)
    console.log(store.system.debug)
}

store.methods.firstConnect(route.params.id)

</script>
<template>
    <main>
        <div class="row">
            <div class="m-4">
                <h1>Login</h1>
                <h1 v-if="store.state.firstConnection">Première Connexion</h1>
            </div>
        </div>
        <div class="col-2 m-4">
            <input type="text" class="form-control" id="login_password"/>
            <p v-if="!store.state.rightPassword">Mot de passe incorrect</p>
        </div>
        <div class="m-4">
            <button class="btn btn-primary" @click="checkPassword('Home',$route.params.id)">Confirmer</button>
        </div>
    </main>
</template>