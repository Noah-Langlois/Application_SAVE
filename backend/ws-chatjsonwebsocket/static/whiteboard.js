var canvas = document.getElementById("whiteboardcanvas");
var context = canvas.getContext("2d");

canvas.addEventListener("mousedown", startImage, false);
canvas.addEventListener("mouseup", finishImage, false);
canvas.addEventListener("mouseleave", finishImage, false);
canvas.addEventListener("mousemove", paintImage, false);

var paint;

function clearCanvas() {
    context.clearRect(0, 0, canvas.width, canvas.height);
}

function startImage(evt) {
    paint = true;
}

function finishImage(evt) {
    paint = false;
}

function paintImage(evt) {
    if (paint) {
        var rect = canvas.getBoundingClientRect();
        var x = evt.clientX - rect.left;
        var y = evt.clientY - rect.top;

        context.fillRect(x, y, 5, 5);
    }
}
