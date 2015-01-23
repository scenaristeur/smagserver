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
<td>détails doap
</td>
</tr>
</table>
</div>
<div id="methode">
<table><tr><th>Méthode Diamond</th></tr>
<tr>
<td>détail de la méthode
</td>
</tr>
</table>
</div>

</body>
<script language="javascript" type="text/javascript">

  var openshiftWebSocketPort = 8000; // Or use 8443 for wss
  var wsUri;
 /* var wsUri = "ws://" + window.location.hostname + ":" + openshiftWebSocketPort + "/projetws?projet=2"; */
  var output; 
var obj="test";
function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}
  function init()
  {
          var projetId = getParameterByName('projet');
    wsUri = "ws://" + window.location.hostname + ":" + openshiftWebSocketPort + "/projetws/?projet="+projetId;
    document.getElementById("wsUri").innerHTML = wsUri;
    output = document.getElementById("output");
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
    websocket.onopen = function(evt) { onOpen(evt) };
    websocket.onclose = function(evt) { onClose(evt) };
    websocket.onmessage = function(evt) { onMessage(evt) };
    websocket.onerror = function(evt) { onError(evt) };
  }

  function onOpen(evt)
  {
    writeToScreen("Connected to server");
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
  {

    output.innerHTML=message;
    output.className=rule;
  }

  if(window.addEventListener){
	  window.addEventListener("load", init, false);
  }else{
	  window.attachEvent("onload", init);
  }
  

</script>
</html>
