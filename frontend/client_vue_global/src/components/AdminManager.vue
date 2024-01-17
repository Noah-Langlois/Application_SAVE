<script setup>
import { useRouter, useRoute } from 'vue-router'
import { inject } from 'vue'

const router = useRouter()

const store = inject('STORE')

function changeRoute(value) {
  console.log("Debug status : " + store.system.debug)
  router.push({
    name: value
  })
}

function createNewAdmin(user) {
  console.log("Debug status : " + store.system.debug)
  console.log("Creation d'un nouvel admin")
  var pseudoNewAdmin = document.getElementById("pseudoNewAdmin");
  console.log("[Creation admin] le token actuel est :" + store.state.token);
  const newAdminUsername = pseudoNewAdmin.value;
  const wsURInewAdminRequest = "ws://192.168.196.107:8024/chatjsonwebsocket/chat/" + user + "/" + store.state.token + "/Ajout/" + newAdminUsername ;
  var ws = new WebSocket(wsURInewAdminRequest);
  pseudoNewAdmin.value = "";
  displayAdminList(user);
}

function displayAdminList(user) {
  console.log("Debug status : " + store.system.debug);
  const adminList = store.methods.getAdminList(user);
  const adminListElement = document.getElementById('admin-list');
  adminListElement.innerHTML = '';
  for (const admin of adminList) {
    const listItem = document.createElement('li');
    listItem.textContent = admin.userId;
    adminListElement.appendChild(listItem);
  }
}

function createAlerte(value,user) {
    store.methods.setChatroom(document.getElementById("discussion_title").value)
    document.getElementById("discussion_title").value = ""
    store.methods.setDescriptionNewAlerte(document.getElementById("description").value)
    document.getElementById("description").value = ""
    store.methods.addDiscussion(store.state.current_chatroom)
    store.methods.connect(user)
    console.log(store.system.debug)
    router.push({
        name: value
  })
}

</script>

<template>
    <main style="min-height: 85vh;">
      <div>
        <div class="ps-5 pt-5">
            <h1>Gestion des administrateurs</h1>
        </div>
        <div class="row p-5">
          <div class="col-xl-6 mb-3">
            <div class="card">
              <div class="card-body">
                <h5 class="card-title text-truncate">Administrateurs</h5>
                <h6 class="card-subtitle mb-2 text-body-secondary">Liste des administrateurs de SAVE</h6>
                <ul class="mt-5 text-truncate" id="admin-list">
                  <li>SuperAdmin</li>
                </ul>
              </div>
            </div>
          </div>
          <div class="col ms-2">
            <div class="row">
              <!--Boutons d'ajout et suppression d'admins qui activent un modal-->
              <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#ajoutModal" style="max-width: 10rem;">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-plus-circle-fill" viewBox="0 0 16 16">
                  <path d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0M8.5 4.5a.5.5 0 0 0-1 0v3h-3a.5.5 0 0 0 0 1h3v3a.5.5 0 0 0 1 0v-3h3a.5.5 0 0 0 0-1h-3z"/>
                </svg> Ajouter         
              </button>
              <!-- Modal d'ajout admin -->
              <div class="modal fade" id="ajoutModal" tabindex="-1" aria-labelledby="ajoutModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                  <div class="modal-content">
                    <div class="modal-header">
                      <h1 class="modal-title fs-5" id="ajoutModalLabel">Ajouter un administrateur</h1>
                      <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                      <h4>Pseudo :</h4>
                      <input class="form-control" id="pseudoNewAdmin"/>
                    </div>
                    <div class="modal-footer">
                      <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annuler</button>
                      <button type="button" @click="createNewAdmin($route.params.id)" class="btn btn-primary">Valider</button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="row mb-5">
              <!--Boutton de supression-->
              <button class="btn btn-danger mt-2" data-bs-toggle="modal" data-bs-target="#suppModal" style="max-width: 10rem;">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-dash-circle-fill" viewBox="0 0 16 16">
                  <path d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0M4.5 7.5a.5.5 0 0 0 0 1h7a.5.5 0 0 0 0-1z"/>
                </svg> Supprimer
              </button>
              <!-- Modal de supressions admin -->
              <div class="modal fade" id="suppModal" tabindex="-1" aria-labelledby="suppModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                  <div class="modal-content">
                    <div class="modal-header">
                      <h1 class="modal-title fs-5" id="suppModalLabel">Supprimer un administrateur</h1>
                      <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                      <h4>Pseudo :</h4>
                      <input class="form-control"/>
                    </div>
                    <div class="modal-footer">
                      <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annuler</button>
                      <button type="button" class="btn btn-primary">Valider</button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div>
            <button class="btn btn-primary ms-5" @click="changeRoute('HomeAdmin')">Retour</button>
        </div>
      </div>
    </main>
</template>