<script setup>
import { useRouter, useRoute } from 'vue-router'
import { inject } from 'vue'
import Chat from "./Chat.vue"

const router = useRouter()

const store = inject('STORE')

function setChatroomList(pvalue, user) {
  store.methods.setChatroom(pvalue)
  store.methods.removeFromNotif(pvalue)
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
                      data-bs-toggle="list" role="tab" href="'#chat"
                      @click="setChatroomList({item}.item, $route.params.id)"
                      :key="item.chatroomId">{{item}}
                      <span v-if="store.state.discussionsWithNotif.includes(item)" class="badge text-bg-primary"></span>
                      </a>
                    </div>
                    <div class="mt-4 ms-4">
                      <button class="btn btn-primary" @click="changeRoute('Form')">Nouvelle Alerte</button>
                    </div>
                    <div style="text-align: center;">
                      <button class="btn btn-primary mt-4" @click="store.methods.refreshChatrooms($route.params.id)">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-clockwise" viewBox="0 0 16 16">
                          <path fill-rule="evenodd" d="M8 3a5 5 0 1 0 4.546 2.914.5.5 0 0 1 .908-.417A6 6 0 1 1 8 2z"/>
                          <path d="M8 4.466V.534a.25.25 0 0 1 .41-.192l2.36 1.966c.12.1.12.284 0 .384L8.41 4.658A.25.25 0 0 1 8 4.466"/>
                        </svg>
                      </button>
                    </div>
                </div>
                <div class="col ms-5 mt-4" v-show="store.state.isCurrentChatroomNotNull">
                  <Chat/>
                </div>
            </div>
        </div>
  </main>
</template>
