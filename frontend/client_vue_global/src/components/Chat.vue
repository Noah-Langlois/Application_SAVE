<script setup>
import { useRouter, useRoute } from 'vue-router'
import { inject } from 'vue'

const router = useRouter()
const store = inject('STORE')

function changeRoute(value, user) {
    if (store.state.isWSConnected) {
        store.methods.disConnect()
        store.methods.clearMessages()
        console.log(store.system.debug)
        router.push({
            name: value
        })
    }
    else {
        store.methods.clearMessages()
        console.log(store.system.debug)
        router.push({
            name: value
    })
    }
    if (value=='Home') {
        store.methods.getChatrooms(user)
    }
}

function sendAndClear() {
    store.methods.send()
    store.methods.clearMessageEntry()
}
</script>

<template>
    <main
    class='flex min-h-screen flex-col items-center justify-between p-24'
    >
        <div class="container-fluid">
            <div class="row">
                <div>
                    <h2>{{ store.state.current_chatroom }}</h2>
                    <div class="row">
                        <div class="col">
                            <label>Messages</label>
                            <div v-if="store.state.isWSConnected" class="connection-state-dot connected"></div>
                            <div v-else class="connection-state-dot disconnected"></div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col">
                            <div class="form-group">
                                <div class="mytextarea" style="padding: 30px;" id="wsMessages"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="">
                    <div class="row">
                        <div class="col">
                            <label>Message</label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col">
                            <div class="form-group">
                                <input type="text" v-on:keyup.enter="sendAndClear()" class="form-control" id="wsMessage">
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col">
                            <button class="btn btn-primary" type="button" @click="store.methods.send()">Send</button>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </main>
</template>