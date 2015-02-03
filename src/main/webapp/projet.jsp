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
</head>
<body onload="init">
<h3>Page Projet</h3>
<h3>Smag0</h3>
<div>Connect to: <span id="wsUri"></span></div>
<input type="button" value="stop" name="stopBtn" class="button" onclick="javascript:stop();"/>
<div id="output">
</div>

<span class="warning">Behold websockets</span>
<div id="doaptab">
<table><tr><th>DOAP - Description of a project</th></tr>
<tr>
<td><a href="https://github.com/edumbill/doap/wiki>détails doap</a>
</td>
</tr>
</table>
</div>
<div id="methodetab">
<table><tr><th>Méthode Diamond</th></tr>
<tr>
<td><a href="http://smag0.rww.io/diamond.owl">détails de la méthode</a>
</td>
</tr>
</table>
</div>

</body>
<script language="javascript" type="text/javascript">

var projetId;
  var openshiftWebSocketPort = 8000; // Or use 8443 for wss
  var wsUri;
 /* var wsUri = "ws://" + window.location.hostname + ":" + openshiftWebSocketPort + "/projetws?projet=2"; */
  var output; 
  var methodetab;
  var doaptab;
var obj="test";
function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}
  function init()
  {
    projetId = getParameterByName('projet');
    wsUri = "ws://" + window.location.hostname + ":" + openshiftWebSocketPort + "/projetws/?projet="+projetId;
    wsUriMethode = "ws://" + window.location.hostname + ":" + openshiftWebSocketPort + "/methodews/?projet="+projetId;
    wsUriDoap = "ws://" + window.location.hostname + ":" + openshiftWebSocketPort + "/doapws/?projet="+projetId;
    document.getElementById("wsUri").innerHTML = wsUri;
    output = document.getElementById("output");
    doaptab = document.getElementById("doaptab");
    methodetab = document.getElementById("methodetab");
    writeToScreen(" Not Connected to server",'warning');

    testWebSocket();
  }
  function stop()
  {
	  websocket.send('disconnect');
  }
  function testWebSocket()
  {
    websocket = new WebSocket(wsUri);
    websocketMethode = new WebSocket(wsUriMethode);
    websocketDoap = new WebSocket(wsUriDoap);
    websocket.onopen = function(evt) { onOpen(evt) };
    websocket.onclose = function(evt) { onClose(evt) };
    websocket.onmessage = function(evt) { onMessage(evt) };
    websocket.onerror = function(evt) { onError(evt) };
    
    websocketMethode.onopen = function(evt) { onOpenMet(evt) };
    websocketMethode.onclose = function(evt) { onCloseMet(evt) };
    websocketMethode.onmessage = function(evt) { onMessageMet(evt) };
    websocketMethode.onerror = function(evt) { onErrorMet(evt) };
    
    websocketDoap.onopen = function(evt) { onOpenDoap(evt) };
    websocketDoap.onclose = function(evt) { onCloseDoap(evt) };
    websocketDoap.onmessage = function(evt) { onMessageDoap(evt) };
    websocketDoap.onerror = function(evt) { onErrorDoap(evt) };
  }

function onOpenMet(evt){
console.log("onopenmet");
 websocketMethode.send(projetId);
}
function onCloseMet(evt){
console.log("onclosemet");
}
function onMessageMet(evt){
console.log("onmessagemet");
writeToMethode(evt.data, 'info');
}
function onErrorMet(evt){
console.log("onerrormet");
}

function onOpenDoap(evt){
console.log("onopenDoap");
 websocketDoap.send(projetId);
}
function onCloseDoap(evt){
console.log("oncloseDoap");
}
function onMessageDoap(evt){
console.log("onmessageDoap");
writeToDoap(evt.data, 'info');
}
function onErrorDoap(evt){
console.log("onerrorDoap");
}
  function onOpen(evt)
  {
    writeToScreen("Connected to Websocket Projet server ");
    doSend("Hello are you there WebSocket Server");

  }

  function onClose(evt)
  {
    writeToScreen("...Kaboom...Im gone",'warning');
  }

  function onMessage(evt)
  {console.log(event.data);
    writeToScreen(evt.data, 'info');

      /*                          var obj = JSON.parse(msg.data); 
                                console.log(obj);
  */}

  function onError(evt)
  {
	  writeToScreen(evt.data,'error');
  }

  function doSend(message)
  {
    writeToScreen("SENT: " + message); 
    websocket.send(message);
  }

  function writeToScreen(message,rule)
  {output.innerHTML=message;
    output.className=rule;
  }
    function writeToMethode(message,rule)
  {
  methodetab.innerHTML=message;
    methodetab.className=rule;
  }
    function writeToDoap(message,rule)
  {
  doaptab.innerHTML=message;
    doaptab.className=rule;
  }

  if(window.addEventListener){
	  window.addEventListener("load", init, false);
  }else{
	  window.attachEvent("onload", init);
  }
  

</script>
</html>
