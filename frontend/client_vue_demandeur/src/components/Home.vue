<script setup>
import { useRouter, useRoute } from 'vue-router'
import { inject } from 'vue'

const router = useRouter()

const store = inject('STORE')

function setChatroomList(pvalue) {
  store.methods.setChatroom(pvalue)

}

function changeRoute(value) {
  console.log(store.system.debug)
  router.push({
    name: value
  })
}


</script>

<template>
  <main>
    <div class="mb-5">
            <div class="row">
              <h1 class="ms-4 mt-4 col-9">Bienvenue sur SAVE</h1>
            </div>
            <div class="row mt-5">
                <div class="col-2 ms-5 ">
                    <label>Discussions</label>
                    <div id="select_chatroom" class="list-group" role="tablist">
                      <a v-for="item in store.state.discussions" class="list-group-item list-group-item-action"
                      data-bs-toggle="list" role="tab" @click="setChatroomList({item}.item)">{{item}}</a>
                    </div>
                    <button class="btn btn-primary mt-2" @click="changeRoute('Chat')">Connexion</button>
                </div>
                <div class="col mt-4">
                  <button class="btn btn-primary" @click="changeRoute('Form')">Nouvelle Alerte</button>
                </div>
            </div>
        </div>
  </main>
</template>
