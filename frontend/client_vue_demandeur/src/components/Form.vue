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

function createAlerte(value) {
    store.methods.setChatroom(document.getElementById("discussion_title").value)
    store.methods.setDescriptionNewAlerte(document.getElementById("description").value)
    store.methods.addDiscussion(store.state.current_chatroom)
    console.log(store.system.debug)
    router.push({
        name: value
  })
}

</script>

<template>
    <main>
        <h1 class="m-4">Nouvelle Alerte</h1>
            <div class="m-4">
                    <div>
                        <label class="form-label">Titre de la discussion</label>
                        <input type="text" class="form-control"
                        id="discussion_title" aria-describedby="title_help">
                        <div id="title_help" class="form-text">
                            Exemple : Témoin d'une agrresion / Victime d'une agression
                        </div>
                    </div>
                    <div class="mt-5">
                        <label class="from-label">Description</label>
                        <textarea type="text" class="form-control"
                        id="description" aria-describedby="description_help"></textarea>
                        <div id="description_help" class="form-text">
                            Raconte nous ton témoignange (de manière anonyme si tu le souhaite)
                        </div>
                    </div>
            </div>
            <div id="Buttons" class="m-4">
                <button class="btn btn-primary me-5" @click="createAlerte('Chat')">Envoyer</button>
                    <button class="btn btn-primary" @click="changeRoute('Home')">Menu</button>
            </div> 

    </main>
</template>