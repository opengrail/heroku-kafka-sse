; 'use strict';

if (typeof(EventSource) !== "undefined") {
    var source = new EventSource("sse");

    source.onmessage = function (event) {
        document.getElementById("result").innerHTML += event.data + "<br>";
    };

} else {
    document.getElementById("result").innerHTML =
        "Sorry, your browser does not support server-sent events...";
}

