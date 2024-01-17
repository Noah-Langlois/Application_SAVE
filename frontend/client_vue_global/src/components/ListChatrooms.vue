<script setup>
import { useRouter, useRoute } from 'vue-router'
import { inject } from 'vue'

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
    <div>
        <div class="row justify-content-center pt-5" v-if="store.state.isDiscussionNotEmpty">
            <div class="col container text-center">
              <h2>Discussions</h2>
            </div>
          </div>
          <div class="row justify-content-center mt-4" v-if="store.state.isDiscussionNotEmpty">
            <div class="col">
              <div id="select_chatroom" class="list-group" role="tablist">
                <a v-for="item in store.state.discussions" class="list-group-item list-group-item-action list-group-item-light"
                data-bs-toggle="list" role="tab" href="#chat" @click="setChatroomList({item}.item, $route.params.id)">{{item}}</a>
              </div>
            </div>
          </div>
          <div class="row justify-content-center pt-5" v-if="store.state.userType=='demandeur'">
            <div class="col container text-center">
              <button class="btn btn-outline-dark" @click="changeRoute('Form')">Nouvelle Alerte</button>
            </div>
          </div>
          <div class="row justify-content-center mt-1" v-if="store.state.isDiscussionNotEmpty">
            <div class="col container text-center">
              <img src="../../img/refresh_arrow.png" alt="refresh" width="20" height="20" />
            </div>
          </div>
          <div class="row justify-content-center">
            <div class="col container text-center">
              <button class="btn btn-outline-danger mt-5" v-if="($route.params.id=='SuperAdmin') && (store.state.userType=='admin')" @click="changeRoute('AdminManager')">Gestionnaire d'autorisations</button>
            </div>
          </div>
    </div>
</template>