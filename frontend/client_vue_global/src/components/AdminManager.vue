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
  const wsURInewAdminRequest = store.system.wsURIprefix + "/chat/" + user + "/" + store.state.token + "/Ajout/" + pseudoNewAdmin.value ;
  var ws = new WebSocket(wsURInewAdminRequest);
  pseudoNewAdmin.value = "";
  displayAdminList(user);
}

function removeAdmin(user) {
  console.log("Debug status : " + store.system.debug)
  console.log("Suppression d'un admin")
  var pseudoAdminToRemove = document.getElementById("pseudoAdminToRemove");
  const wsURInewAdminRequest = store.system.wsURIprefix + "/chat/" + user + "/" + store.state.token + "/Suppr/" + pseudoAdminToRemove.value ;
  var ws = new WebSocket(wsURInewAdminRequest);
  pseudoAdminToRemove.value = "";
  displayAdminList(user);
}

async function displayAdminList(user) {
  console.log("Debug status : " + store.system.debug);
  var v_adminList = ["SuperAdmin"];
  const wsURIAdminListRequest = store.system.wsURIprefix + "/chat/" + user + "/" + store.state.token + "/List";
  console.log("[getAdminList] URI is: " + wsURIAdminListRequest);
  console.log("[getAdminList] Token is: " + store.state.token);
  var ws;
  ws = new WebSocket(wsURIAdminListRequest);
  const message = await new Promise(resolve => {
    ws.onmessage = function (evt) {
      console.log(evt);
      const obj = JSON.parse(evt.data)
      if (obj.type == 'Liste admins') {
        for (let i = 0; i < obj.adm.length; i++) {
          console.log("[getAdminList] Admin found: " + obj.adm[i]);
          v_adminList.push(obj.adm[i]);
        }
      }
      resolve(evt.data);
    };
  });

  const adminListElement = document.getElementById('admin-list');
  console.log("[displayAdminList] adminList is : " + v_adminList);
  adminListElement.innerHTML = '';
  for (let i = 0; i < v_adminList.length; i++) {
    const listItem = document.createElement('li');
    listItem.textContent = v_adminList[i];
    adminListElement.appendChild(listItem);
  }
  console.log("[displayAdminList] adminListElement is : " + v_adminList);
}

let URL = window.location.pathname
const myArray = URL.split("/")
if (store.state.token=='') {
  if (myArray[1]=='demandeur') {
    router.push({
            name: 'Login'
        })
  }
  if (myArray[1]=='admin') {
    router.push({
            name: 'LoginAdmin'
        })
  }
}

displayAdminList(myArray[2]);

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
                      <input class="form-control" id="pseudoAdminToRemove"/>
                    </div>
                    <div class="modal-footer">
                      <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annuler</button>
                      <button type="button" @click="removeAdmin($route.params.id)" class="btn btn-primary">Valider</button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div>
            <button class="btn btn-primary ms-5" @click="displayAdminList($route.params.id)">Refresh</button>
        </div>
        <div>
            <button class="btn btn-primary ms-5" @click="changeRoute('HomeAdmin')">Retour</button>
        </div>
      </div>
    </main>
</template>