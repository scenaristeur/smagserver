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
 var reponse="recherche en cours";
       function drawSomeText(id) {
         var pjs = Processing.getInstanceById(id);
         var text = document.getElementById('inputtext').value;
         pjs.drawText(text); }
         
         function actualisePde(id) {
         var pjs = Processing.getInstanceById(id);
         /* var text = document.getElementById('inputtext').value;*/
         var text = reponse;
         pjs.drawText(text); }
       </script>
</head>
<body onload="init">
<script src="/js/processing.min.js"></script>
<div> Chaque projet intégré au système Smag est pris en compte comme un agent #Ferber #Janus.</br>
Dont le but est de se résoudre par lui même en fonction des son environnement et des autres projets.</div>
<div>Vous pouvez, vous aussi ajouter un projet.</div>
<table><tr><td>
<h3>Hello HTML5 Web Socket</h3>
<h3>Smag0</h3>
<div>Connect to: <span id="wsUri"></span></div>
</td>
<td>
<input type="button" value="stop" name="stopBtn" class="button" onclick="javascript:stop();"/>

        ----
        </br>
        <canvas id="projets" data-processing-sources="pde/smag/smag.pde /pde/smag/Noeud.pde"></canvas>
       <!-- <canvas id="mysketch2" data-processing-sources="/pde/smag/smag.pde /pde/smag/Noeud.pde"></canvas>-->

        </br>
        ----</br>
         <input type="textfield" value="my text" id="inputtext">
    <button type="button" onclick="drawSomeText('projets')">place</button>
</td></tr></table>


<div id="output">
</div>

<span class="warning">Behold websockets</span>


</body>
<script language="javascript" type="text/javascript">

  var openshiftWebSocketPort = 8000; // Or use 8443 for wss
  var wsUri = "ws://" + window.location.hostname + ":" + openshiftWebSocketPort + "/projetsws";
  var output; 
var obj="test";

  function init()
  {
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
  {console.log(evt.data);
 /* var json = '{"result":true,"count":1}',
    obj = JSON.parse(json);

alert(obj.count);*/
    writeToScreen(evt.data, 'info');
    /* test avec json 
    var testjson= JSON.parse(evt.data);
    alert(testjson.id);
    writeToScreen(testjson, 'info');*/
    	  reponse=evt.data;
	  actualisePde('projets')
  }

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
