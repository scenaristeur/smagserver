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
<h1 id="titre">Page Projet</h1>
<h4 id="type">type</h4>
<h4 id="description"></h4>
<div>Connect to: <span id="wsUri"></span></div>
<input type="button" value="stop" name="stopBtn" class="button" onclick="javascript:stop();"/>
<div id="output">
</div>
<div id="doaptab">Merci de patienter, nous recherchons la description de ce projet
</div>
<div id="methodetab">Patientez, nous recherchons la meilleure m�thode pour faire avancer votre projet
</div>


<span class="warning">Behold websockets</span>
<span>DOAP - Description of a project</span>

<table><tr><th>DOAP - Description of a project</th></tr>
<tr>
<td><a href="https://github.com/edumbill/doap/wiki">d�tails doap</a>
</td>
</tr>
</table>

<span>M�thode Diamond 
<a href="http://smag0.rww.io/diamond.owl">d�tails de la m�thode</a></span>

<table><tr><th>M�thode Diamond</th></tr>
<tr>
<td>
</td>
</tr>
</table>


</body>
<script language="javascript" type="text/javascript">

var projetId;
  var openshiftWebSocketPort = 8000; // Or use 8443 for wss
  var wsUri;
  var urlProjet;
  var output; 
  var methodetab;
  var doaptab;
  var titre;
  var type;
  var description;
 
var obj="test";
var objTitre="TITRE au format JSON";
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
    titre = document.getElementById("titre");
    type=document.getElementById("type");
    description=document.getElementById("description");
    writeToScreen(" Not Connected to server",'warning');
urlProjet="http://"+window.location.hostname + ":" + openshiftWebSocketPort + "/projet.jsp?projet="+projetId;
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
console.log("methode JSON re�ue : "+evt.data);
obj = JSON.parse(evt.data);
var i=0;
//var reponse='<table border=1><tr><td></td><td>M�thode</td><td>Propri�t�</td><td>Objet</td><td>Valeur pour ce projet</td></tr>';
var reponse='';
var sujet='';
var types='types : ';
var hasPart='Compos� de : ';
var first='FIRST : ';
var autres='Autres : ';
for (var key in obj) {
  if (obj.hasOwnProperty(key)) {
  i++;
  if (obj[key].subject!=sujet){
  sujet=obj[key].subject;
  
  }
  
  if (obj[key].predicate=='type'){
  console.log(obj[key].predicate+obj[key].object);
  types+=obj[key].object+" ";
  }else if (obj[key].predicate=='hasPart'){
  console.log(obj[key].predicate+obj[key].object);
  hasPart+="<a href=\""+urlProjet+"&etape="+obj[key].object+"\">"+obj[key].object+"</a></br>";
  }
  else if (obj[key].predicate=='first'){
  console.log(obj[key].predicate+obj[key].object);
  first+=obj[key].object+" ";
  }
  else{
  //reponse+='<span style="color: blue;">'+key+'</span><span>'+obj[key].subject+'</span></br>';
  //reponse=reponse+'<tr><td>'+key+'</td><td><a href=\"projet.jsp?projet='+obj[key].projet+'\">'+obj[key].projet+'</a></td><td>'+obj[key].titre+'</td><td>'+obj[key].description+'</td></tr>';
  // reponse=reponse+'<tr><td>'+key+'</td><td><a href=\"http://smag0.rww.io/diamond.owl\" target=\"_blank\">'+obj[key].subject+'</td><td>'+obj[key].predicate+'</td><td>'+obj[key].object+'</td><td>Compl�ter (page en cours de confection)</td></tr>';
   // alert(key + " -> " + p[key]);
     console.log(obj[key].predicate+obj[key].object);
  autres+=obj[key].object+" ";
  }
  }
}
//reponse+='</table>';
reponse+="<h1>"+sujet+"</h1>";
reponse+="<div>ins�rer commentaire sur la m�thode<\div>";
reponse+=types;
reponse+="<h2>"+first+"</h2>";
reponse+="<h3>"+hasPart+"</h3>";
reponse+=autres;
       writeToMethode('<span style="color: blue;">'+i+' RESPONSE(S): </br>' + reponse+'</span>');
        websocketMethode.close();
// writeToMethode(evt.data, 'info');
}
function onErrorMet(evt){
console.log("onerrormet");
writeToMethode(evt.data, 'error');
}

function onOpenDoap(evt){
console.log("onopenDoap");
 /* debug non lancement de la recherche DOAP pendant le dev de Methode websocketDoap.send(projetId);*/
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
writeToDoap(evt.data, 'error');
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
  {console.log(evt.data);
    writeToScreen(evt.data, 'info');
/*console.log("OBJET TITRE JSON re�u : "+evt.data);
objTitre = JSON.parse(evt.data);
for (var key in objTitre) {
  if (objTitre.hasOwnProperty(key)) {
  console.log("A afficher : "+objTitre[key]);*/
  /*if (obj[key].subject!=sujet){
  sujet=obj[key].subject;
  */
 /* }}*/
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
  {output.innerHTML=message;
    output.className=rule;
  }
    function writeToMethode(message,rule)
  {
  console.log("ecriture dans methodetab : "+message);
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
