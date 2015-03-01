window.onload = function() {

	
function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
};

// global var
var lienAjouteLien=null;
var vocabulaire=null;
var vocabulaireSource=null;
var vocabulaireType=null;

  
  // Get references to elements on the page.
  var ajouteObjetForm = document.getElementById('ajouteObjetForm');
  var nomObjetField = document.getElementById('nomObjet');
  var adresseIpObjetField = document.getElementById('adresseIpObjet');
  var portObjetField = document.getElementById('portObjet');
  var commentaireObjetField = document.getElementById('commentaireObjet');
  var emailNouvelObjetField = document.getElementById('emailNouvelObjet');
  var listeObjetConnecteDiv = document.getElementById('listeObjetConnecteDiv');
  var lienAjoute = document.getElementById('lienAjoute');
  var lienDansVoc=document.getElementById('lienDansVoc');



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
 lienDansVoc.style.visibility="hidden";

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
//ligneObjetConnecte.innerHTML+=obj.titre;
  	ligneObjetConnecte.innerHTML+="<div id="+obj.objetConnecte+" title=\""+obj.description+"\"><a href=\""+obj.adresseIpObjet+":"+obj.portObjet+"\" target=\"_blank\">"+obj.titre+"</a> </br>";
  	  //	ligneObjetConnecte.innerHTML+="   ......   indicateur dispo ou non</br>... Laisser un message à cet objet connecté / à son gestionnaire </div>";
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
  		listeObjetConnecteDiv.innerHTML = "";										 
	 	websocketListeObjetsConnectes.send(data);                           // Send the message through the WebSocket. 
		return false;
		};
}

// gestion du formulaire d'ajout d'un lien
lienSimple.onchange=function(){
console.log(lienSimple.value);
switch (lienSimple.value)
{
    case "sameAs":
    	/*var propPrefix = document.createElement('b');
    	var propExpliq = document.createElement('i');
    	propPrefix.innerHTML="owl:sameAs";
    	propExpliq.innerHTML=" est identique à";
  		lienAjouteLien.appendChild(propPrefix);
        lienAjouteLien+="http://www.w3.org/2002/07/owl#sameAs";
        lienAjouteLien.appendChild(propExpliq);*/
        lienAjouteLien="Est identique à (owl:sameAs) http://www.w3.org/2002/07/owl#sameAs";
        break;
    case "type":   	
    	lienAjouteLien="est un/une (=est de type) (rdf:type) http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
    	break;
    case "isPartOf":
        lienAjouteLien="est un composant de (dcterms:isPartOf) http://purl.org/dc/terms/isPartOf";
        break;
    case "isInterestedIn":
        lienAjouteLien="est interessé par (pkm:isInterestedIn) http://www.ontotext.com/proton/protonkm#isInterestedIn";
        break;
    default : 
    	lienAjouteLien="relation indeterminée (smag:relationIndeterminee) http://smag0.blogspot.fr/ns/smag0#relationIndeterminee";
    	break;
}
lienAjoute.value=lienAjouteLien;
lienComplexe.value="";
};


lienComplexe.onchange=function(){
console.log(lienComplexe.value);
lienAjoute.value="";
switch (lienComplexe.value)
{
    case "foaf":
        vocabulaire="http://xmlns.com/foaf/0.1/";
        vocabulaireSource="http://xmlns.com/foaf/0.1/";
        vocabulaireType="rdf/xml";
        break;
    case "doap":
        vocabulaire="http://usefulinc.com/ns/doap#";
        vocabulaireSource="http://usefulinc.com/ns/doap#";
        vocabulaireType="rdf/xml";
        break;
    case "diamond":
        vocabulaire="http://smag0.blogspot.fr/ontologies/Diamond#";
        vocabulaireSource="https://raw.githubusercontent.com/scenaristeur/smagserver/master/src/main/webapp/ontologies/diamond.owl";
        vocabulaireType="owl";
        break;
    case "wai":
    	vocabulaire="http://purl.org/wai#";
    	break;
    case "smag":
        vocabulaire="http://smag0.blogspot.fr/ns/smag0#";
        vocabulaireSource="http://fuseki-smag0.rhcloud.com/ds/query";
        vocabulaireType="sparqlEndpoint";
        break;
    default : 
    	vocabulaire="http://smag0.blogspot.fr/ns/smag0#";
    	vocabulaireSource="http://fuseki-smag0.rhcloud.com/ds/query";
    	vocabulaireType="sparqlEndpoint";
    	break;
}
lienSimple.value="";
lienDansVoc.style.visibility="visible";
lienDansVoc.value="";
lienAjoute.value+=vocabulaire;
//lienAjoute.value=lienComplexe.value;
};

lienDansVoc.onchange=function(){
console.log(lienDansVoc.value);
lienAjoute.value+=lienDansVoc.value;
}
};