<script setup>
import { useRouter, useRoute } from 'vue-router'
import { inject } from 'vue'

const router = useRouter()
const store = inject('STORE')

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
                            <!-- For development purpose only 
                            <div v-if="store.state.isWSConnected" class="connection-state-dot connected"></div>
                            <div v-else class="connection-state-dot disconnected"></div>-->
                        </div>
                    </div>
                    <div class="row" >
                        <div class="col">
                            <div class="form-group" style="background-color: white; border-radius: 1cap;">
                                <div class="mytextarea" style="padding: 30px; border-radius: 1cap;" id="wsMessages">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="">
                    <div class="row">
                    </div>
                    <div class="row pt-2">
                        <div class="col">
                            <div class="input-group">
                                <input type="text" v-on:keyup.enter="sendAndClear()" class="form-control" id="wsMessage">
                                <button class="btn btn-secondary" type="button" @click="store.methods.send()">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-send" viewBox="0 0 16 16">
                                        <path d="M15.854.146a.5.5 0 0 1 .11.54l-5.819 14.547a.75.75 0 0 1-1.329.124l-3.178-4.995L.643 7.184a.75.75 0 0 1 .124-1.33L15.314.037a.5.5 0 0 1 .54.11ZM6.636 10.07l2.761 4.338L14.13 2.576zm6.787-8.201L1.591 6.602l4.339 2.76z"/>
                                    </svg>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </main>
</template>