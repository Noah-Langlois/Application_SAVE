import { reactive, readonly } from 'vue'
import router from './router/index.js';
import fb_sound from '../sounds/Facebook-message-sound.mp3'

// Variable de stockage du serveur websocket
var ws;

// Audio message
var audio = new Audio(fb_sound)

const system = reactive({
  debug: false
})

// Propriétés réactives accessibles depuis les pages
const state = reactive({
  // Liste des discussions accessibles par l'utilisateur
  discussions: [],
  // Discussion selectionnée
  current_chatroom: '',
  // Stockage de la description d'une nouvelle alerte
  DescriptionNewAlerte: '',
  // Statu de connection au serveur websocket
  isWSConnected: false,
  // Détecter si l'utilsateur n'a aucune discussions
  isDiscussionNotEmpty: false,
  // Type d'utilisateur (admin ou demandeur)
  userType: '',
  // Détecter si l'utilsateur n'a pas selectioné de discussions
  isCurrentChatroomNotNull: false,
  // Détection d'un bon mot de passe ou non
  rightPassword: true,
  // Détection si première connexion de l'utilisateur ou non
  firstConnection : true,
  // Détection d'un appareil mobile ou non
  isMobile : false,
})

function setFirstConnection(pValue) {
  state.firstConnection = pValue
}

function setRightPassword(pValue) {
  state.rightPassword = pValue
}

function setCurrentChatroomNotNull(pVale) {
  state.isCurrentChatroomNotNull = pVale
}

function setWSConnected(pValue) {
  state.isWSConnected = pValue
}

function setDiscussionEmpty(pValue) {
  state.isDiscussionNotEmpty = pValue
}

// Méthodes publiques
const methods = {

  // Newalerte() : Envoie la description de l'alerte comme premier message. Réinitialise la description à vide.
  NewAlerte() {
    var message = {content : state.DescriptionNewAlerte, created : new Date(), browser : navigator.product}
    ws.send(JSON.stringify(message));
    methods.setDescriptionNewAlerte('')
  },

  // setUserType(String : type d'utilisateur) : Modifier le type d'utilisateur (demandeur ou admin)
  setUserType(pValue) {
    state.userType = pValue
  },

  // writeMessage(String : contenu du message, String : affichage du message à gauche ou à droite) : 
  // Affiche le message dans la balise avec l'id wsMessages
  // Joue le son à chaque message
  writeMessage(pValue, pType) {
    var newElement = document.createElement("div");
    newElement.textContent = pValue;
    newElement.className = pType;
    var wsMessages = document.getElementById("wsMessages");
    wsMessages.appendChild(newElement);
    wsMessages.scrollTop = wsMessages.scrollHeight;
    audio.play()
  },

  // clearMessages() : Supprime les messages affichés dans le div avec l'id wsMessages
  clearMessages() {
    var wsMessages = document.getElementById("wsMessages");
    while (wsMessages.firstChild) {
        wsMessages.removeChild(wsMessages.firstChild);
    }
  },

  // firstConnect(String : pseudo de l'utilisateur) : Première connexion lors du démarage de l'application
  // Permet de savoir si l'utilsateur est connu ou non
  // Se connecte au serveur qui lui renvoie un message contenant l'information, deconnexion NON-automatique
  firstConnect(user) {
    const wsURIFirstConnection = "ws://192.168.196.107:8024/chatjsonwebsocket/chat/" + state.userType + "/" + user
    ws = new WebSocket(wsURIFirstConnection);
    ws.onopen = function (evt) {
      console.log(evt);
      setWSConnected(true);
    };
    ws.onmessage = function (evt) {
      console.log(evt);
      const obj = JSON.parse(evt.data)
      if (obj.type == 'Nouvel utilisateur') {
        setFirstConnection(true)
      }
      if (obj.type == 'Utilisateur existant') {
        setFirstConnection(false)
      }
    };
  },

  // getChatrooms(String : pseudo, String : password, String : Nom de la page à afficher si bon pseudo)
  // Si le mot de passe est bon : le serveur envoie les discussions accessible par l'utilisateur, on peut alors rediriger
  // l'utilisateur vers la page 'value'
  // Si le mot de passe est mauvais : le serveur n'envoie pas de messages et déconnecte l'utilisateur
  getChatrooms(user, password, value) {

    const wsURIChatroomsAdmin = "ws://192.168.196.107:8024/chatjsonwebsocket/chat/" + state.userType + "/" + user + "/" + password
    ws = new WebSocket(wsURIChatroomsAdmin);
    ws.onopen = function (evt) {
        console.log(evt);
        setWSConnected(true);
        setRightPassword(false)
    };
    ws.onmessage = function (evt) {
        console.log(evt);
        const obj = JSON.parse(evt.data)
        if (obj.type=='Liste chatrooms') {
          for (let i = obj.chatrooms.length-1 ; i >= 0 ; i--) {
            state.discussions[obj.chatrooms.length-1-i]=(obj.chatrooms[i])
            setDiscussionEmpty(true)
          }
        }
        methods.disConnect()
        router.push({
          name: value,
          params: {id: user}
        })
        setRightPassword(true)
    };
    ws.onerror = function (evt) {
        console.log(evt);
    };
    ws.onclose = function (evt) {
      console.log(evt);
      setWSConnected(false);
    }
  },

  // connect(String : pseudo)
  // Connection à la discussion selectionné (state.current_chatroom)
  // Dans le cas d'une nouvelle alerte, la description de l'alerte est non nulle, on envoie donc la description avec
  // NewAlerte()
  connect(user) {

    const wsURI = "ws://192.168.196.107:8024/chatjsonwebsocket/chat/" + state.userType + "/" + user + "/" + state.password + "/" + state.current_chatroom

    ws = new WebSocket(wsURI);
    ws.onopen = function (evt) {
        console.log(evt);
        setWSConnected(true);
        if (state.DescriptionNewAlerte != '') {
            methods.NewAlerte()
        }
    };
    ws.onmessage = function (evt) {
        console.log(evt);
        const obj = JSON.parse(evt.data)
        if (obj.type=='message chat') {
          var typeMessage = ''
          if (obj.userId == user) {
            // The message comes from the current user
            typeMessage = "message-bubble left"
          }
          else {
            typeMessage = "message-bubble right"
          }
          methods.writeMessage(obj.role + " : " + obj.content, typeMessage);
        }
        if (obj.type=='Liste chatrooms') {
          for (let i = 1 ; i < obj.chatrooms.length ; i++) {
            state.discussions[i-1]=(obj.chatrooms[i])
            setDiscussionEmpty(true)
          }
        }
        if (obj.type=='Notification') {
          // TODO : add notifications on concerned chatrooms
          methods.writeMessage("Info : new message in chatroom " + obj.chatroomId, "info-text");
        }
    };
    ws.onerror = function (evt) {
        console.log(evt);
    };
    ws.onclose = function (evt) {
      console.log(evt);
      setWSConnected(false);
    }
  },

  // send()
  // Créer un JSON pour envoyer le message avec comme contenu ce qui est écrit dans 'wsMessage'
  send() {
    var message = {content : document.getElementById("wsMessage").value, created : new Date(), browser : navigator.product}
    ws.send(JSON.stringify(message));
  },

  // setChatroom(String : nom de la discussion)
  // Modification de la discussion selectionnéé
  setChatroom(pValue) {
    state.current_chatroom = pValue;
    setCurrentChatroomNotNull(true)
    if (pValue=='') {
      setCurrentChatroomNotNull(false)
    }
  },

  // setDescriptionNewAlerte(String : description de l'alerte)
  setDescriptionNewAlerte(pValue) {
    state.DescriptionNewAlerte = pValue;
  },

  // discConnect() : Fermuture de la conenxion au serveur websocket
  disConnect() {
    ws.close();
  },

  // addDiscussion(String : Nom de la discussion)
  // Rajoute la discussion à la liste des discussion de l'utilisateur
  addDiscussion(pValue) {
    state.discussions.push(pValue)
    setDiscussionEmpty(true)
  },

  clearMessageEntry() {
    var wsMessage = document.getElementById("wsMessage");
    wsMessage.value = "";
  },

  setIsMobile() {
    if(window.innerWidth <= 1130) {
      state.isMobile = true;
    }
    else {
      state.isMobile = false;
    }
  },
}

export default {
  state: readonly(state),
  system,
  methods,
}
