<script setup>
import { useRouter, useRoute } from 'vue-router'
import { inject } from 'vue'

const router = useRouter()

const store = inject('STORE')
store.methods.setUserType('admin')

function checkPassword(value) {
    let user = document.getElementById("login_pseudo").value
    store.methods.getChatrooms(user, document.getElementById("login_password").value, value)
    console.log(store.system.debug)
}


</script>
<template>
    <main>
        <div class="row">
            <div class="m-4">
                <h1>Login</h1>
            </div>
        </div>
        <div>
            <div class="col-2 m-4">
                <label>Pseudo</label>
                <input type="text" class="form-control" id="login_pseudo"/>
            </div>
            <div class="col-2 m-4">
                <label>Mot de passe</label>
                <input type="text" class="form-control" id="login_password"/>
                <p v-if="!store.state.rightPassword">Mot de passe incorrect</p>
            </div>
            <div class="m-4">
                <button class="btn btn-primary" @click="checkPassword('HomeAdmin')">Confirmer</button>
            </div>
        </div>
    </main>
</template>