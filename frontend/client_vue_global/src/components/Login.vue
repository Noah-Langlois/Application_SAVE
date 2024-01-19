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
    <main style="min-height: 85vh;">
        <div>
            <div class="row ps-5 pt-5 pe-5" v-if="!store.state.firstConnection">
                <div class="col-lg-5">
                    <h1 class="mb-4 mt-4">Connexion</h1>
                    <label>Code PIN</label>
                    <input type="password" class="form-control" id="login_password" style="max-width: 18rem;" 
                    v-on:keyup.enter="checkPassword('Home',$route.params.id)"/>
                    <p v-if="!store.state.rightPassword">Mot de passe incorrect</p>
                    <div class="mt-4">
                        <button class="btn btn-outline-dark" @click="checkPassword('Home',$route.params.id)">Confirmer</button>
                    </div>
                </div>
            </div>
            <div v-else>
                <div class="row justify-content-start ps-5 pt-5 pe-5">
                    <div class="col-lg-5">
                        <div class="">
                            <div class="row justify-content-center">
                                <h1 class="mt-4">Bienvenue sur SAVE</h1>
                                <p class="">Signalement Anonyme des Violences à l'ENSMA</p>
                            </div>
                        </div>
                        <div class="pt-3">                    
                            <label>Veuillez initialiser votre code PIN : </label>
                            <input type="password" class="form-control" id="login_password" style="max-width: 18rem;"
                            v-on:keyup.enter="checkPassword('Home',$route.params.id)"/>
                            <p v-if="!store.state.rightPassword">Mot de passe incorrect</p>
                            <div class="mt-4">
                                <button class="btn btn-outline-dark" @click="checkPassword('Home',$route.params.id)">Confirmer</button>
                            </div>
                        </div>
                    </div>
                    <div class="col pt-5">
                        <div class="card" style="max-width: 40rem;">                   
                            <div class="card-body">
                                <ul>
                                    <li>Signaler des évenements inappropriés au CESA</li>
                                    <li>Discuter de manière complétement anonyme</li>
                                    <li>Trouver des solutions</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                
            </div>
        </div>
    </main>
</template>