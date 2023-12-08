<script setup>
import { useRouter, useRoute } from 'vue-router'
import { inject } from 'vue'
import { disConnect, connect, clearMessages, send } from '../components/websocket.js'
import { current_chatroom } from '../components/websocket.js'



const router = useRouter()

const store = inject('STORE')

function changeRoute(value) {
  console.log(store.system.debug)
  router.push({
    name: value
  })
}
</script>

<template>
    <main
      class='flex min-h-screen flex-col items-center justify-between p-24'
    >
        <h1 class="m-4">{{ current_chatroom }}</h1>
      <div class="container-fluid">
          <div class="row">
              <div class="col-sm-5">
                  <div class="row">
                      <div class="col">
                          <label>Message</label>
                      </div>
                  </div>
                  <div class="row">
                      <div class="col">
                          <div class="form-group">
                              <input type="text" class="form-control" id="wsMessage">
                          </div>
                      </div>
                  </div>
                  <div class="row">
                      <div class="col">
                          <button class="btn btn-primary" type="button" @click="send">Send</button>
                      </div>
                  </div>
                  <div class="row pt-5">
                      <div class="col">
                          <button class="btn btn-primary" type="button" @click="connect($route.params.id)">Connect</button>
                          <button class="btn btn-secondary" type="button" @click="disConnect">Disconnect</button>
                      </div>
                  </div>
                  <div class="row pt-5">
                      <div class="col">
                            <button class="btn btn-primary" @click="changeRoute('Home')">Menu</button>
                      </div>
                  </div>
              </div>
              <div class="col-sm-7">
                  <div class="row">
                      <div class="col">
                          <label>Messages</label>
                      </div>
                  </div>
                  <div class="row">
                      <div class="col">
                          <div class="form-group">
                              <div class="mytextarea">
                                <div class="card">
                                  <div class="card-body">
                                    <div id="wsMessages" ></div>
                                  </div>
                                </div>                              </div>
                          </div>
                      </div>
                  </div>
                  <div class="row">
                      <div class="col">
                          <button class="btn btn-danger" type="button" @click="clearMessages">Clear</button>
                      </div>
                  </div>
              </div>
          </div>
      </div>
    </main>

</template>