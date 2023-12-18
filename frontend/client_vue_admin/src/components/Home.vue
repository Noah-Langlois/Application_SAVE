<script setup>
import { useRouter, useRoute } from 'vue-router'
import { inject } from 'vue'

const router = useRouter()

const store = inject('STORE')

function changeRoute(value) {
  if (value=='Chat') {
    store.methods.setChatroom(document.getElementById("select_chatroom").value)
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
              <h1 class="ms-4 mt-4 col-9">Bienvenue sur SAVE</h1>
            </div>
            <div class="row mt-5">
                <div class="col-2 ms-5 " v-if="store.state.isDiscussionEmpty">
                    <label>Discussions</label>
                    <select id="select_chatroom" class="form-select">
                      <option v-for="item in store.state.discussions">{{item}}</option>
                    </select>
                    <button class="btn btn-primary mt-2" @click="changeRoute('Chat')">Connexion</button>
                </div>
            </div>
        </div>
  </main>
</template>
