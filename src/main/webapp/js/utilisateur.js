window.onload = function() {

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

var openshiftWebSocketPort = 8000; // Or use 8443 for wss

var email = getParameterByName('email');
var uri = getParameterByName('projet');
var projetId = getParameterByName('projet');

var wsUtilisateur = "ws://" + window.location.hostname + ":" + openshiftWebSocketPort + "/utilisateurws";
var websocketUtilisateur = new WebSocket(wsUtilisateur);

var titre = document.getElementById('titre');
   
   console.log(email);
   
    websocketUtilisateur.onopen = function(evt) { onOpenUtilisateur(evt) };
    websocketUtilisateur.onclose = function(evt) { onCloseUtilisateur(evt) };
    websocketUtilisateur.onmessage = function(evt) { onMessageUtilisateur(evt) };
    websocketUtilisateur.onerror = function(evt) { onErrorUtilisateur(evt) };
    
function onOpenUtilisateur(evt){
	console.log("onOpenUtilisateur");
	if (email!=null){
		console.log("envoi de l'email : "+email);
		websocketUtilisateur.send(email);
		titre.innerHTML=email;
	}
}
function onCloseUtilisateur(evt){
	console.log("onCloseUtilisateur");
}
function onErrorUtilisateur(evt){
	console.log("onErrorUtilisateur");
}
function onCloseUtilisateur(evt){
	console.log("onCloseUtilisateur");
}
function onMessageUtilisateur(evt){
	console.log("onMessageUtilisateur");
}
};