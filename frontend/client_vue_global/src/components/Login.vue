<script setup>
import { useRouter, useRoute } from 'vue-router'
import { inject } from 'vue'

const router = useRouter()
const route = useRoute()
const store = inject('STORE')
store.methods.setUserType('demandeur')

store.methods.setIsMobile()
window.onresize = store.methods.setIsMobile



function checkPassword(value,user) {
    store.methods.getChatrooms(user, document.getElementById("login_password").value, value)
    console.log(store.system.debug)
}

store.methods.firstConnect(route.params.id)

</script>
<template>
    <main>
        <div id="desktopLogin" v-if="!store.state.isMobile">
            <div class="row">
                <h1>Ordinateur</h1>
            </div>
            <div class="row" v-if="!store.state.firstConnection">
                <div class="col-2 m-4">
                    <h1 class="mb-4 mt-4">Connexion</h1>
                    <label>Code PIN</label>
                    <input type="text" class="form-control" id="login_password"/>
                    <p v-if="!store.state.rightPassword">Mot de passe incorrect</p>
                    <div class="mt-4">
                        <button class="btn btn-primary" @click="checkPassword('Home',$route.params.id)">Confirmer</button>
                    </div>
                </div>
            </div>
            <div class="row" v-else>
                <div class="m-4">
                    <h1 class="mt-4">Bienvenue sur SAVE</h1>
                    <p class="mb-5">Signalement Anonyme des Violences à l'ENSMA</p>
                    <div class="row">
                        <div class="col-4">
                            <label>Veuillez initialiser votre code PIN : </label>
                            <input type="text" class="form-control" id="login_password" style="width: 18rem;"/>
                            <p v-if="!store.state.rightPassword">Mot de passe incorrect</p>
                            <div class="mt-4">
                                <button class="btn btn-primary" @click="checkPassword('Home',$route.params.id)">Confirmer</button>
                            </div>
                        </div>
                        <div class="col ms-5">
                            <div class="card" style="width: 40rem;">                   
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
        </div>

        <!--Version Mobile-->

        <div id="mobileLogin" v-else>
            <div class="row" v-if="!store.state.firstConnection">
                <div class="row justify-content-center">
                    <div class="col container text-center">
                        <h1 class="mb-4 mt-4">Connexion</h1>
                    </div>
                </div>
                <div class="row justify-content-center">
                    <div class="col container text-center">
                        <label>Code PIN</label>
                    </div>
                </div>
                <div class="row justify-content-center">
                    <div class="col d-flex justify-content-center">
                        <input type="text" class="form-control" id="login_password" style="max-width: 18rem;"/>
                    </div>
                </div>
                <div class="row justify-content-center">
                    <div class="col container text-center">
                        <p v-if="!store.state.rightPassword">Mot de passe incorrect</p>
                    </div>
                </div>
                <div class="row justify-content-center mt-2">
                    <div class="col container text-center">
                        <button class="btn btn-primary" @click="checkPassword('Home',$route.params.id)">Confirmer</button>
                    </div>
                </div>
            </div>
            <div class="row" v-else>
                <div class="mt-4">
                    <div class="row justify-content-center">
                        <div class="col container text-center">
                            <h1 class="mt-4">Bienvenue sur SAVE</h1>
                        </div>
                    </div>
                    <div class="row justify-content-center">
                        <div class="col container text-center">
                            <p class="mb-5">Signalement Anonyme des Violences à l'ENSMA</p>
                        </div>
                    </div>
                    <div class="row justify-content-center mb-5">
                        <div class="col d-flex justify-content-center container text-center">
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
                    <div class="row justify-content-center">
                        <div class="col container text-center">
                            <label>Veuillez initialiser votre code PIN : </label>
                        </div>
                    </div>
                    <div class="row justify-content-center">
                        <div class="col d-flex justify-content-center">
                            <input type="text" class="form-control" id="login_password" style="max-width: 18rem;"/>
                        </div>
                    </div>
                    <div class="row justify-content-center mt-2">
                        <div class="col container text-center">
                            <button class="btn btn-primary" @click="checkPassword('Home',$route.params.id)">Confirmer</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>
</template>