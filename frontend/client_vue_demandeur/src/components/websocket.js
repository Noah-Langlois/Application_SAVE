var ws;

export let discussions = ['Discussion1','Discussion2']
export let current_chatroom = 'Discussion1'

export function setChatroom(value) {
    current_chatroom = value;
  }

export let DescriptionNewAlerte = ''

export function setDescriptionNewAlerte(value) {
    DescriptionNewAlerte = value;
}

export function NewAlerte() {
    var message = {content : DescriptionNewAlerte, created : new Date(), browser : navigator.product}
    ws.send(JSON.stringify(message));
    setDescriptionNewAlerte('')
}

export function connect(user) {
    var host = document.location.host;
    var pathname = document.location.pathname;
    const wsURI = "ws://192.168.196.107:8024/chatjsonwebsocket/chat/demandeur/" + current_chatroom + "/" + user

    ws = new WebSocket(wsURI);
    ws.onopen = function (evt) {
        console.log(evt);
        writeMessage("Connect from WSEndpoint.");
        if (DescriptionNewAlerte != '') {
            NewAlerte()
        }
    };
    ws.onmessage = function (evt) {
        console.log(evt);
        const obj = JSON.parse(evt.data)
        writeMessage(obj.role + " : " + obj.content);
    };
    ws.onerror = function (evt) {
        console.log(evt);
    };
    ws.onclose = function (evt) {
        writeMessage("Disconnect from WSEndpoint.");
    }
}

export function disConnect() {
    ws.close();
}

function writeMessage(pValue) {
    var newElement = document.createElement("div");
    newElement.textContent = pValue;
    var wsMessages = document.getElementById("wsMessages");
    wsMessages.appendChild(newElement);
    wsMessages.scrollTop = wsMessages.scrollHeight;
}

export function clearMessages() {
    var wsMessages = document.getElementById("wsMessages");
    while (wsMessages.firstChild) {
        wsMessages.removeChild(wsMessages.firstChild);
    }
}

export function send() {
    var message = {content : document.getElementById("wsMessage").value, created : new Date(), browser : navigator.product}
    ws.send(JSON.stringify(message));
}
