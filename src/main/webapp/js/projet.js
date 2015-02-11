window.onload = function() {

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}


 var projetId = getParameterByName('projet');
 var openshiftWebSocketPort = 8000; // Or use 8443 for wss
 var wsUriDoapModel = "ws://" + window.location.hostname + ":" + openshiftWebSocketPort + "/doapmodelws";
 var wsUriPageProjet = "ws://" + window.location.hostname + ":" + openshiftWebSocketPort + "/pageprojetws/?projet="+projetId;
 var titrePage = document.getElementById('titrePage');
 var titreProjet = document.getElementById('titreProjet');
 var descriptionProjet = document.getElementById('descriptionProjet');
 var doapDiv = document.getElementById('doapDiv');
 var methodeDiv = document.getElementById('methodeDiv');
 var statutTitre = document.getElementById('statutTitre');
 var statutDoap = document.getElementById('statutDoap');
 var statutMethode = document.getElementById('statutMethode');
 var websocketDoapModel = new WebSocket(wsUriDoapModel);
 var websocketPageProjet = new WebSocket(wsUriPageProjet);
var objDoap;
var objDetail;
var nomDoap=null;
var descriptionDoap=null;
 
 doapDiv.style.display="none";

 websocketDoapModel.onopen = function(event) {
  statutDoap.innerHTML = 'Connected to: ' + event.currentTarget.url;
  statutDoap.className = 'open';
};

websocketPageProjet.onopen = function(event) {
  statutTitre.innerHTML = 'Connected to: ' + event.currentTarget.url;
  statutTitre.className = 'open';
};

// Handle any errors that occur.
websocketDoapModel.onerror = function(error) {
  console.log('WebSocket websocketDoapModel Error: ' + error);
};
websocketPageProjet.onerror = function(error) {
  console.log('WebSocket PageProjet Error: ' + error);
};

// Show a disconnected message when the WebSocket is closed.
websocketDoapModel.onclose = function(event) {
  statutDoap.innerHTML = 'Disconnected from websocketDoapModel.';
  statutDoap.className = 'closed';
};
websocketPageProjet.onclose = function(event) {
  statutTitre.innerHTML = 'Disconnected from WebSocket Page Projet.';
  statutTitre.className = 'closed';
};


// Handle messages sent by the server.
websocketDoapModel.onmessage = function(event) {
console.log(event);
  var message = event.data;
 doapDiv.innerHTML ='';
console.log(event.data);
objDoap = JSON.parse(event.data);
doapDiv.innerHTML +='<form id="doap-form" action="#" method="post">';
var i=0;
for (var key in objDoap) {
  if (objDoap.hasOwnProperty(key)) {
doapDiv.innerHTML += '<li class="received"><span>'+objDoap[key].object+': '+
'</span></br>' +

'<textarea  id="'+objDoap[key].subject+'" placeholder="Aucune information pour l\'instant. Libre à vous de compléter"></textarea >'+
'<button type="submit">Enregistrer</button><button type="button" id="effacer">Effacer</button></li>';
  }
} 
doapDiv.innerHTML +='</form>';
  		document.getElementById("name").value  = nomDoap;
  		document.getElementById("Project").value = nomDoap;
  		document.getElementById("description").value = descriptionDoap;
               
};

websocketPageProjet.onmessage = function(event) {
console.log("Page projet:" +event.data);
objDetail=JSON.parse(event.data);
var i=0;
for (var key in objDetail){
	if(objDetail.hasOwnProperty(key)){
	 if (objDetail[key].propriete=='http://purl.org/dc/elements/1.1/title'){
	 nomDoap=objDetail[key].objet;
  		console.log("titre :"+nomDoap);
  		titreProjet.innerHTML=nomDoap;
  		titrePage.innerHTML=nomDoap;
  		if (document.getElementById("name")){
  		document.getElementById("name").value  = nomDoap;
  		document.getElementById("Project").value = nomDoap;
  		}

	}else if (objDetail[key].propriete=='http://purl.org/dc/elements/1.1/description'){
  		descriptionDoap=objDetail[key].objet;
  		console.log("description : "+descriptionDoap);
  		descriptionProjet.innerHTML=descriptionDoap;
  		if (document.getElementById("description")){
  		document.getElementById("description").value = descriptionDoap;
  		}
	}	
}
}
};

};