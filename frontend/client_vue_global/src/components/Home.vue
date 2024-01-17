<script setup>
import { useRouter, useRoute } from 'vue-router'
import { inject } from 'vue'
import { ref } from 'vue'
import Chat from "./Chat.vue"
import ListChatrooms from './ListChatrooms.vue'
import AboutUs from './AboutUs.vue'

const router = useRouter()

const store = inject('STORE')
store.methods.setIsMobile()
window.onresize = store.methods.setIsMobile


</script>

<template>
  <main style="min-height: 85vh;">
    <!--Version WEB-->
    <div v-if="!store.state.isMobile">
      <div class="mb-5">
        <div class="row">
          <div class="col-2 ms-5 mt-5 me-5">
            <ListChatrooms/>
          </div>
          <div class="col-md-auto">
            <img src="../../img/baton_long.png" class="mt-4" alt="batton" width="2" height="650" />
          </div>
          <div class="col mt-4 ms-4" v-if="store.state.isCurrentChatroomNotNull">
            <div class="mt-5">
              <Chat/>
            </div>
          </div>
          <div class="col mt-4 ms-4" v-else>
            <div v-if="!store.state.isDiscussionNotEmpty">
              <AboutUs/>
            </div>
          </div>
        </div>
      </div>
    </div>  

    <!--Version Mobile-->

    <div v-else>
      <div class="mb-5">
        <div v-show="!store.state.isCurrentChatroomNotNull">
          <ListChatrooms/>
          <div v-if="!store.state.isDiscussionNotEmpty">
            <AboutUs/>
          </div>
        </div>
        <button class="btn btn-secondary mt-5" @click="store.methods.setChatroom('')" v-if="store.state.isCurrentChatroomNotNull">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-left-circle" viewBox="0 0 16 16">
            <path fill-rule="evenodd" d="M1 8a7 7 0 1 0 14 0A7 7 0 0 0 1 8m15 0A8 8 0 1 1 0 8a8 8 0 0 1 16 0m-4.5-.5a.5.5 0 0 1 0 1H5.707l2.147 2.146a.5.5 0 0 1-.708.708l-3-3a.5.5 0 0 1 0-.708l3-3a.5.5 0 1 1 .708.708L5.707 7.5z"/>
          </svg>
        </button>
        <div class="row justify-content-center" v-show="store.state.isCurrentChatroomNotNull">
          <div class="col container text-center">
            <Chat/>
          </div>
        </div>
      </div>
    </div>

  </main>
</template>
