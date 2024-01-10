<script setup>
import { useRouter, useRoute } from 'vue-router'
import { inject } from 'vue'
import { ref } from 'vue'
import Chat from "./Chat.vue"

const router = useRouter()

const store = inject('STORE')
store.methods.setIsMobile()
window.onresize = store.methods.setIsMobile

const isChatroomSelected = ref(false)

function setIsChatroomSelected(pvalue) {
  isChatroomSelected.value = pvalue
  if (store.state.isWSConnected) {
    store.methods.disConnect()
  }
}

function setChatroomList(pvalue, user) {
  setIsChatroomSelected(true)
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
    <!--Version WEB-->
    <div v-if="!store.state.isMobile">
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
            <button class="btn btn-danger mt-5" v-if="($route.params.id=='SuperAdmin') && (store.state.userType=='admin')" @click="changeRoute('AdminManager')">Gestionnaire d'autorisations</button>
          </div>
          <div class="col ms-5 mt-4" v-show="store.state.isCurrentChatroomNotNull">
            <div class="mt-5">
              <Chat/>
            </div>
          </div>
        </div>
      </div>
    </div>  

    <!--Version Mobile-->

    <div v-else>
      <div class="mb-5">
        <div v-show="!isChatroomSelected">
          <div class="row justify-content-center mt-5" v-if="store.state.isDiscussionNotEmpty">
            <div class="col container text-center">
              <h2>Discussions</h2>
            </div>
          </div>
          <div class="row justify-content-center mt-4" v-if="store.state.isDiscussionNotEmpty">
            <div class="col">
              <div id="select_chatroom" class="list-group" role="tablist">
                <a v-for="item in store.state.discussions" class="list-group-item list-group-item-action"
                data-bs-toggle="list" role="tab" href="#chat" @click="setChatroomList({item}.item, $route.params.id)">{{item}}</a>
              </div>
            </div>
          </div>
          <div class="row justify-content-center mt-2">
            <div class="col container text-center">
              <button class="btn btn-primary" @click="changeRoute('Form')">Nouvelle Alerte</button>
            </div>
          </div>
          <div class="row justify-content-center mt-1">
            <div class="col container text-center">
              <img src="../../img/refresh_arrow.png" alt="refresh" width="20" height="20" />
            </div>
          </div>
          <div class="row justify-content-center">
            <div class="col container text-center">
              <button class="btn btn-danger mt-5" v-if="($route.params.id=='SuperAdmin') && (store.state.userType=='admin')" @click="changeRoute('AdminManager')">Gestionnaire d'autorisations</button>
            </div>
          </div>
        </div>
        <button class="btn btn-secondary mt-5" @click="setIsChatroomSelected(false)" v-if="isChatroomSelected">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-left-circle" viewBox="0 0 16 16">
            <path fill-rule="evenodd" d="M1 8a7 7 0 1 0 14 0A7 7 0 0 0 1 8m15 0A8 8 0 1 1 0 8a8 8 0 0 1 16 0m-4.5-.5a.5.5 0 0 1 0 1H5.707l2.147 2.146a.5.5 0 0 1-.708.708l-3-3a.5.5 0 0 1 0-.708l3-3a.5.5 0 1 1 .708.708L5.707 7.5z"/>
          </svg>
        </button>
        <div class="row justify-content-center" v-show="isChatroomSelected">
          <div class="col container text-center">
            <Chat/>
          </div>
        </div>
      </div>
    </div>

  </main>
</template>
