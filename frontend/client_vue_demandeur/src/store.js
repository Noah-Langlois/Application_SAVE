import { reactive, readonly } from 'vue'
var ws;

const system = reactive({
  debug: false
})

const state = reactive({
  discussions: ['Discussion1','Discussion2'],
  current_chatroom: 'Discussion1',
  DescriptionNewAlerte: ''
})

const methods = {
  NewAlerte() {
    var message = {content : state.DescriptionNewAlerte, created : new Date(), browser : navigator.product}
    ws.send(JSON.stringify(message));
    methods.setDescriptionNewAlerte('')
  },

  writeMessage(pValue) {
    var newElement = document.createElement("div");
    newElement.textContent = pValue;
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

  connect(user) {
    var host = document.location.host;
    var pathname = document.location.pathname;
    const wsURI = "ws://192.168.196.107:8024/chatjsonwebsocket/chat/demandeur/" + state.current_chatroom + "/" + user

    ws = new WebSocket(wsURI);
    ws.onopen = function (evt) {
        console.log(evt);
        methods.writeMessage("Connect from WSEndpoint.");
        if (state.DescriptionNewAlerte != '') {
            methods.NewAlerte()
        }
    };
    ws.onmessage = function (evt) {
        console.log(evt);
        const obj = JSON.parse(evt.data)
        methods.writeMessage(obj.role + " : " + obj.content);
    };
    ws.onerror = function (evt) {
        console.log(evt);
    };
    ws.onclose = function (evt) {
        methods.writeMessage("Disconnect from WSEndpoint.");
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
  }
}

export default {
  state: readonly(state),
  system,
  methods,
}
