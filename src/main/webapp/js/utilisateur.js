window.onload = function() {

	
function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

  // Get references to elements on the page.
  var ajouteObjetForm = document.getElementById('ajouteObjetForm');
  var nomObjetField = document.getElementById('nomObjet');
  var adresseIpObjetField = document.getElementById('adresseIpObjet');
  var portObjetField = document.getElementById('portObjet');
  var commentaireObjetField = document.getElementById('commentaireObjet');
  var emailNouvelObjetField = document.getElementById('emailNouvelObjet');
  var listeObjetConnecteDiv = document.getElementById('listeObjetConnecteDiv');

var openshiftWebSocketPort = 8000; // Or use 8443 for wss

var email = getParameterByName('email');
var uri = getParameterByName('projet');
var projetId = getParameterByName('projet');

var wsUtilisateur = "ws://" + window.location.hostname + ":" + openshiftWebSocketPort + "/utilisateurws";
var websocketUtilisateur = new WebSocket(wsUtilisateur);

var wsListeObjetsConnectes = "ws://" + window.location.hostname + ":" + openshiftWebSocketPort + "/listeobjetsconnectesws";
var websocketListeObjetsConnectes = new WebSocket(wsListeObjetsConnectes);

var titre = document.getElementById('titre');
 
 toggleMe('ajouteServiceDiv');
   
   console.log(email);
   titre.innerHTML=email;
    websocketUtilisateur.onopen = function(evt) { onOpenUtilisateur(evt) };
    websocketUtilisateur.onclose = function(evt) { onCloseUtilisateur(evt) };
    websocketUtilisateur.onmessage = function(evt) { onMessageUtilisateur(evt) };
    websocketUtilisateur.onerror = function(evt) { onErrorUtilisateur(evt) };
    
    websocketListeObjetsConnectes.onopen = function(evt) { onOpenListeObjetsConnectes(evt) };
    websocketListeObjetsConnectes.onclose = function(evt) { onCloseListeObjetsConnectes(evt) };
    websocketListeObjetsConnectes.onmessage = function(evt) { onMessageListeObjetsConnectes(evt) };
    websocketListeObjetsConnectes.onerror = function(evt) { onErrorListeObjetsConnectes(evt) };
    
function onOpenUtilisateur(evt){
	console.log("onOpenUtilisateur");
	if (email!=null){
		console.log("envoi de l'email : "+email);
		//websocketUtilisateur.send(email);
		emailNouvelObjetField.value=email;
		  var data = "{ \"type\" : \"email\", "+
					"\"email\": \""+ email +"\", "+
					"\"date\": \""+Date.now()+"\""+
					"}";
  		console.log(data);
	// Send the message through the WebSocket.
	websocketUtilisateur.send(data);
	}
	
// Send a message when the form is submitted.
ajouteObjetForm.onsubmit = function(e) {
e.preventDefault();
  // Retrieve the message from the textarea.
  var nomObjet = nomObjetField.value;
  var adresseIpObjet = adresseIpObjetField.value;
  var portObjet = portObjetField.value;
  var commentaireObjet = commentaireObjetField.value;
  var emailNouvelObjet = emailNouvelObjetField.value;
  var data = "{ \"type\" : \"nouvelObjetConnecte\", "+
				"\"nomObjet\": \""+ nomObjet +"\", "+
				"\"adresseIpObjet\": \""+ adresseIpObjet +"\", "+
				"\"portObjet\": \""+ portObjet +"\", "+
				"\"commentaireObjet\": \""+ commentaireObjet +"\", "+
				"\"emailNouvelObjet\": \""+ emailNouvelObjet +"\", "+
				"\"date\": \""+Date.now()+"\""+
				"}";
  console.log(data);
  // Send the message through the WebSocket.
	websocketUtilisateur.send(data);
	ajouteObjetConnecteDiv.disable="true";
	nomObjet.value = '';
	ajouterObjet.value='Creation de votre objet connecté en cours';
	toggleMe('ajouteObjetConnecteDiv');
	demandeUpdateObjetsConnectes();
	return false;
	};
}
function onCloseUtilisateur(evt){
	console.log("onCloseUtilisateur");
}
function onErrorUtilisateur(evt){
	console.log("onErrorUtilisateur");
}
function onMessageUtilisateur(evt){
	console.log("onMessageUtilisateur");
	console.log(evt.data);
	ajouterObjet.value=evt.data;	
}


function onOpenListeObjetsConnectes(evt){
	console.log("onOpenListeObjetsConnectes");
	demandeUpdateObjetsConnectes();
	}
function onCloseListeObjetsConnectes(evt){
	console.log("onCloseListeObjetsConnectes");
}
function onErrorListeObjetsConnectes(evt){
	console.log("onErrorListeObjetsConnectes");
}

function onMessageListeObjetsConnectes(evt){
	console.log("onMessageListeObjetsConnectes");
	console.log(evt.data);
//	console.log("test"+listeObjetConnecteDiv.innerHTML);
	var listeObjetsUtilisateur = document.createElement('ul');
	listeObjetConnecteDiv.appendChild(listeObjetsUtilisateur);
	var ligneObjetConnecte = document.createElement('li');
  	listeObjetsUtilisateur.appendChild(ligneObjetConnecte);
	obj = JSON.parse(event.data);
var i=0;

  	ligneObjetConnecte.innerHTML+="<div id="+obj.objetConnecte+"><a href=\""+obj.adresseIpObjet+":"+portObjet+"\">"+obj.titre+"</a></div>"+evt.data+"ajouter bouton disponible ou non";

}
function demandeUpdateObjetsConnectes(){
		if (email!=null){
		console.log("envoi de l'email : "+email);
		//websocketUtilisateur.send(email);
		emailNouvelObjetField.value=email;
		  var data = "{ \"type\" : \"update\", "+
						"\"email\": \""+ email +"\", "+
						"\"date\": \""+Date.now()+"\""+
						"}";
  		console.log(data);	
  		listeObjetConnecteDiv.innerHTML = "Liste des objets connectes , Chargement des informations";										 
	 	websocketListeObjetsConnectes.send(data);                           // Send the message through the WebSocket. 
		return false;
		};
}
};