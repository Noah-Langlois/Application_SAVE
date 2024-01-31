<script setup>
import { useRouter, useRoute } from 'vue-router'
import { inject, onMounted , onUnmounted , ref } from 'vue'

const router = useRouter()
const route = useRoute()

const store = inject('STORE')

function setChatroomList(pvalue, user) {
  store.methods.setChatroom(pvalue)
  if (store.state.isWSConnected) {
    store.methods.disConnect()
    store.methods.clearMessages()
  }
  store.methods.connect(user)
  // Remove the new chatroom from the list of discussions with notifications
  store.methods.removeFromNotif(pvalue)
  console.log("[setChatroomList] should remove " + pvalue + " from the list of chatrooms with notifications")
  // Update the list of discussions with notifications
  updateBadges(user)
}

function changeRoute(value) {
  if (store.state.isWSConnected) {
    store.methods.disConnect()
  }
  console.log(store.system.debug)
  router.push({
    name: value
  })

}

function refreshChatrooms(user) {
  var temp_current_chatroom = store.state.current_chatroom
  var temp_discussions = store.state.discussions.slice()
  console.log("The current chatroom is " + temp_current_chatroom)
  store.methods.updateChatroomsList(user) // This is an async function
  var new_discussions = temp_discussions.filter(chatroom => !temp_discussions.includes(chatroom));
  displayChatrooms(user, new_discussions)
  updateBadges(user)
}

function displayChatrooms(user, pNewDiscussions) {
  const CurrentListChatrooms = document.getElementById('select_chatroom');
  if (CurrentListChatrooms == null) {
    return
  }
  // CurrentListChatrooms.innerHTML = '';
  for (let i = 0; i < pNewDiscussions.length; i++) {
    const listItem = document.createElement('a');
    listItem.textContent = pNewDiscussions[i];
    listItem.className = "list-group-item list-group-item-action list-group-item-light";
    listItem.setAttribute("data-bs-toggle", "list");
    listItem.setAttribute("role", "tab");
    listItem.setAttribute("href", "#chat");
    listItem.addEventListener("click", function () {
      setChatroomList(pNewDiscussions[i], user);
    });
    CurrentListChatrooms.appendChild(listItem);
  }
}

function updateBadges(user) {
  console.log("[updateBadges] Updating Notifications")
  store.methods.setNeedUpdate(false)
  var temp_badges = store.state.discussionsWithNotif.slice()
  const CurrentListChatrooms = document.getElementById('select_chatroom')
  if (CurrentListChatrooms == null) {
    return
  }
  let children = CurrentListChatrooms.children;

  for (let i = 0; i < children.length; i++) {
    if (children[i].textContent == store.state.current_chatroom) {
      // Makes sure the current chatroom is not highlighted
      children[i].className = "list-group-item list-group-item-action list-group-item-light active";
    } else {      
      if (temp_badges.includes(children[i].textContent)) {
        children[i].className = "list-group-item list-group-item-action list-group-item-danger";
      } else {
        children[i].className = "list-group-item list-group-item-action list-group-item-light";
      }
    }
  }
}

let intervalId;

onMounted(() => {
  intervalId = setInterval(() => {
    if (store.methods.getNeedUpdate()) {
    updateBadges(route.params.id);
    }
  }, 1000); // Runs updateBadges every 1000 milliseconds (1 second)
});

onUnmounted(() => {
  clearInterval(intervalId); // Clears the interval when the component is unmounted
});

</script>

<template>
    <div>
        <div class="row justify-content-center pt-5" v-if="store.state.isDiscussionNotEmpty">
            <div class="col container text-center">
              <h2>Discussions</h2>
            </div>
          </div>
          <div class="row justify-content-center mt-4" v-if="store.state.isDiscussionNotEmpty" ref="chatroomsDiv">
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
          <div class="row justify-content-center mt-1">
            <div class="col container text-center">
              <img src="../../img/refresh_arrow.png" @click="refreshChatrooms($route.params.id)" alt="refresh" width="20" height="20" />
            </div>
          </div>
          <div class="row justify-content-center">
            <div class="col container text-center">
              <button class="btn btn-outline-danger mt-5" v-if="($route.params.id=='SuperAdmin') && (store.state.userType=='admin')" @click="changeRoute('AdminManager')">Gestionnaire d'autorisations</button>
            </div>
          </div>
    </div>
</template>
