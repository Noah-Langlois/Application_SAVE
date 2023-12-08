var ws;

function connect() {
    var host = document.location.host;
    var pathname = document.location.pathname;

    ws = new WebSocket(document.getElementById("wsURI").value);
    ws.binaryType = "arraybuffer";

    ws.onopen = function (evt) {
        console.log(evt);
        writeMessage("Connect to WSEndpoint.");
    };
    ws.onmessage = function (evt) {
        console.log(evt);

        if(typeof evt.data === 'string') {
            writeMessage(evt.data);
        } else if (evt.data instanceof ArrayBuffer) {
            writeBinaryMessage(evt.data);
        }
    };
    ws.onerror = function (evt) {
        console.log(evt);
    };
    ws.onclose = function (evt) {
        writeMessage("Disconnect from WSEndpoint.");
    }
}

function disConnect() {
    ws.close();
}

function writeBinaryMessage(pValue) {
    var canvas = document.createElement("canvas");
    canvas.width="200";
    canvas.height="200";
    var ctx = canvas.getContext('2d');

    var imageData = ctx.createImageData(200,200);
    var pixels = imageData.data;

    var buffer = new Uint8Array(pValue);
    for (var i=0; i < pixels.length; i++) {
        pixels[i] = buffer[i];
    }
    ctx.putImageData(imageData, 0, 0);
    var wsMessage = document.getElementById("wsMessages");

    var newElement = document.createElement("div");
    newElement.appendChild(canvas);
    wsMessage.appendChild(newElement);
    wsMessage.scrollTop = wsMessage.scrollHeight;
}

function writeMessage(pValue) {
    var newElement = document.createElement("div");
    newElement.textContent = pValue;
    var wsMessage = document.getElementById("wsMessages");
    wsMessage.appendChild(newElement);
    wsMessage.scrollTop = wsMessage.scrollHeight;
}

function clearMessages() {
    var wsMessage = document.getElementById("wsMessages");
    while (wsMessage.firstChild) {
        wsMessage.removeChild(wsMessage.firstChild);
    }
}

function send() {
    var content = document.getElementById("wsMessage").value;
    var chatMessage = {
        content: content,
        created: new Date(),
        browser: navigator.product
    }

    console.log(JSON.stringify(chatMessage));
    ws.send(JSON.stringify(chatMessage));
}

function sendImage() {
    var canvas = document.getElementById("whiteboardcanvas");
    var context = canvas.getContext('2d');

    var imagedata = context.getImageData(0, 0, canvas.width, canvas.height).data;
    ws.send(new Uint8Array(imagedata));
}
