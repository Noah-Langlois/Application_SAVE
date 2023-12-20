import { reactive, readonly } from 'vue'
var ws;
const system = reactive({
  debug: false
})

const state = reactive({
  discussions: [],
  current_chatroom: '',
  DescriptionNewAlerte: '',
  isWSConnected: false,
  isDiscussionNotEmpty: false,
  userType: ''
})

function setWSConnected(pValue) {
  state.isWSConnected = pValue
}

function setDiscussionEmpty(pValue) {
  state.isDiscussionNotEmpty = pValue
}



const methods = {
  NewAlerte() {
    var message = {content : state.DescriptionNewAlerte, created : new Date(), browser : navigator.product}
    ws.send(JSON.stringify(message));
    methods.setDescriptionNewAlerte('')
  },

  setUserType(pValue) {
    state.userType = pValue
  },

  writeMessage(pValue, pType) {
    var newElement = document.createElement("div");
    newElement.textContent = pValue;
    newElement.className = pType || "message-bubble"; // by default, a message is displayed as a bubble message
    var wsMessages = document.getElementById("wsMessages");
    wsMessages.appendChild(newElement);
    wsMessages.scrollTop = wsMessages.scrollHeight;
  },

  clearMessages() {
    var wsMessages = document.getElementById("wsMessages");
    while (wsMessages.firstChild) {
        wsMessages.removeChild(wsMessages.firstChild);
    }
  },

  getChatrooms(user) {
    var host = document.location.host;
    var pathname = document.location.pathname;
    const wsURIChatroomsAdmin = "ws://192.168.196.107:8024/chatjsonwebsocket/chat/" + state.userType + "/" + user
    ws = new WebSocket(wsURIChatroomsAdmin);
    ws.onopen = function (evt) {
        console.log(evt);
        setWSConnected(true);
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
    };
    ws.onerror = function (evt) {
        console.log(evt);
    };
    ws.onclose = function (evt) {
      setWSConnected(false);
    }
  },

  connect(user) {
    var host = document.location.host;
    var pathname = document.location.pathname;
    const wsURI = "ws://192.168.196.107:8024/chatjsonwebsocket/chat/" + state.userType + "/" + user + "/" + state.current_chatroom

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
          if (obj.userId != user) {
            typeMessage = "message-bubble.right"
          }
          else {
            typeMessage = "message-bubble"
          }
          methods.writeMessage(obj.role + " : " + obj.content, typeMessage);
        }
        if (obj.type=='Liste chatrooms') {
          for (let i = 1 ; i < obj.chatrooms.length ; i++) {
            state.discussions[i-1]=(obj.chatrooms[i])
            setDiscussionEmpty(true)
          }
        }
    };
    ws.onerror = function (evt) {
        console.log(evt);
    };
    ws.onclose = function (evt) {
      setWSConnected(false);
    }
  },

  send() {
    var message = {content : document.getElementById("wsMessage").value, created : new Date(), browser : navigator.product}
    ws.send(JSON.stringify(message));
  },

  setChatroom(pValue) {
    state.current_chatroom = pValue;
  },

  setDescriptionNewAlerte(pValue) {
    state.DescriptionNewAlerte = pValue;
  },

  disConnect() {
    ws.close();
  },

  addDiscussion(pValue) {
    state.discussions.push(pValue)
  },

  clearMessageEntry() {
    var wsMessage = document.getElementById("wsMessage");
    wsMessage.value = "";
  }
}

export default {
  state: readonly(state),
  system,
  methods,
}
