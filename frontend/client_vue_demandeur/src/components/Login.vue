<script setup>
import { useRouter, useRoute } from 'vue-router'
import { inject } from 'vue'

const router = useRouter()

const store = inject('STORE')

function changeRoute(value,user) {
    store.methods.setChatroom(user)
    store.methods.connect(user)
    if (store.state.isWSConnected) {
        store.methods.disConnect()
    }
    console.log(store.system.debug)
    router.push({
        name: value
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
            <input type="text" class="form-control" id="login_password"/>
        </div>
        <div class="m-4">
            <button class="btn btn-primary" @click="changeRoute('Home',$route.params.id)">Confirmer</button>
        </div>
    </main>
</template>