<html>
<meta charset="utf-8" />
<head>
<title>WebSocket Test</title>
<style type="text/css">
.info {
	color: #01529B;
	background-color: #BDE5F8;
}
.error {
	color: #D8000C;
	background-color: #FFBABA;
}
.warning {
	color: #9F6000;
	background-color: #FEEFB3;
}
.button{
    font: 11px verdana, arial, helvetica, sans-serif;
    border: 1px solid #ccc;
    color: #666;
    font-weight: bold;
    font-size: 10px;
    margin-top: 5px;
    overflow: hidden;
}

</style>
 <script type="text/javascript">
  var openshiftWebSocketPort = 8000; // Or use 8443 for wss
  var wsUri = "ws://" + window.location.hostname + ":" + openshiftWebSocketPort + "/testjson";
  console.log(wsUri);
       </script>
</head>
<body onload="init">

<h3>Hello HTML5 TestJson</h3>
<h3>Smag0</h3>
<div>Connect to: <span id="wsUri"></span></div><div id="statut"></div>

<div id="output">
</div>




</body>
<script language="javascript" type="text/javascript">

  function init()
  {
    document.getElementById("wsUri").innerHTML = wsUri;
    output = document.getElementById("output");
    statut = document.getElementById("statut");
    testWebSocket();
  }
  function stop()
  {
	  websocket.send('disconnect');
  }
function testWebSocket() {

       // websocket = new WebSocket(wsUri,'json');
        websocket = new WebSocket(wsUri);
        websocket.onopen = function(evt) { onOpen(evt) };
        websocket.onclose = function(evt) { onClose(evt) };
        websocket.onmessage = function(evt) { onMessage(evt) };
        websocket.onerror = function(evt) { onError(evt) }; 
}

function onOpen(evt) {
console.log("connected");
        writeToStatut("CONNECTED");
        doSend("WebSocket rocks");
}
function onClose(evt) {
console.log("disconnected");
        writeToStatut("DISCONNECTED");
}
function onMessage(evt) {
console.log("message reçu"+evt.data);
        writeToScreen('<span style="color: blue;">RESPONSE: ' + evt.data+'</span>');
        websocket.close();
}

function onError(evt) {
console.log("error");
        writeToStatut('<span style="color: red;">ERROR:</span> ' + evt.data);
}
function doSend(message) {
        writeToStatut("SENT: " + message);
        websocket.send(message);
}

  function writeToScreen(message,rule)
  {	output.innerHTML=message;
    output.className=rule;
  }
  
    function writeToStatut(message,rule)
  {	statut.innerHTML=message;
    statut.className=rule;
  }
  
  if(window.addEventListener){
	  window.addEventListener("load", init, false);
  }else{
	  window.attachEvent("onload", init);
  }
</script>
</html>
