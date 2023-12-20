<script setup>
import { useRouter, useRoute } from 'vue-router'
import { inject } from 'vue'

const router = useRouter()

const store = inject('STORE')

function changeRoute(value) {
    let user = document.getElementById("login_pseudo").value
    store.methods.getChatroomsAdmin(user)
    if (store.state.isWSConnected) {
        store.methods.disConnect()
    }
    console.log(store.system.debug)
    router.push({
        name: value,
        params: {id: user}
    })
}

</script>
<template>
    <main>
        <div class="row">
            <div class="m-4">
                <h1>Login</h1>
            </div>
        </div>
        <div class="col-2 m-4">
            <label>Pseudo</label>
            <input type="text" class="form-control" id="login_pseudo"/>
        </div>
        <div class="col-2 m-4">
            <label>Mot de passe</label>
            <input type="text" class="form-control" id="login_password"/>
        </div>
        <div class="m-4">
            <button class="btn btn-primary" @click="changeRoute('HomeAdmin')">Confirmer</button>
        </div>
    </main>
</template>