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

function createAlerte(value,user) {
    store.methods.setChatroom(document.getElementById("discussion_title").value)
    document.getElementById("discussion_title").value = ""
    store.methods.setDescriptionNewAlerte(document.getElementById("description").value)
    document.getElementById("description").value = ""
    store.methods.addDiscussion(store.state.current_chatroom)
    store.methods.connect(user)
    console.log(store.system.debug)
    router.push({
        name: value
  })
}

if (store.state.token=='') {
    router.push({
              name: 'Login'
          })
}

</script>

<template>
    <main style="min-height: 85vh;">
        <h1 class="p-4">Nouvelle Alerte</h1>
            <div class="m-4">
                    <div>
                        <label class="form-label">Titre de la discussion</label>
                        <input type="text" class="form-control"
                        id="discussion_title" aria-describedby="title_help">
                        <div id="title_help" class="form-text">
                            Exemple : Témoin d'une agresion / Victime d'une agression
                        </div>
                    </div>
                    <div class="mt-5">
                        <label class="from-label">Description</label>
                        <textarea type="text" class="form-control"
                        id="description" aria-describedby="description_help"></textarea>
                        <div id="description_help" class="form-text">
                            Raconte nous ton témoignage (de manière anonyme si tu le souhaites)
                        </div>
                    </div>
            </div>
            <div id="Buttons" class="m-4">
                <button class="btn btn-outline-dark me-5" @click="createAlerte('Home',$route.params.id)">Envoyer</button>
                    <button class="btn btn-outline-dark" @click="changeRoute('Home')">Menu</button>
            </div> 

    </main>
</template>