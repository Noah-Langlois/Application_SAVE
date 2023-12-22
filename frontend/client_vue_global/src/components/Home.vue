<script setup>
import { useRouter, useRoute } from 'vue-router'
import { inject } from 'vue'
import Chat from "./Chat.vue"

const router = useRouter()

const store = inject('STORE')

function setChatroomList(pvalue, user) {
  store.methods.setChatroom(pvalue)
  if (store.state.isWSConnected) {
    store.methods.disConnect()
    store.methods.clearMessages()
  }
  store.methods.connect(user)
}

function changeRoute(value) {
  if (store.state.isWSConnected) {
    store.methods.disConnect()
    store.methods.clearMessages()
  }
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
                <div class="col-2 ms-5 mt-5">
                  <div class="mt-4">
                  </div>
                    <h2 v-if="store.state.isDiscussionNotEmpty">Discussions</h2>
                    <div id="select_chatroom" class="list-group" role="tablist">
                      <a v-for="item in store.state.discussions" class="list-group-item list-group-item-action"
                      data-bs-toggle="list" role="tab" href="#chat" @click="setChatroomList({item}.item, $route.params.id)">{{item}}</a>
                    </div>
                    <div class="mt-4 ms-4">
                      <button class="btn btn-primary" @click="changeRoute('Form')">Nouvelle Alerte</button>
                    </div>
                    <div style="text-align: center;">
                        <img src="../../img/refresh_arrow.png" alt="refresh" width="20" height="20" />
                    </div>
                </div>
                <div class="col ms-5 mt-4" v-show="store.state.isCurrentChatroomNotNull">
                  <Chat/>
                </div>
            </div>
        </div>
  </main>
</template>
