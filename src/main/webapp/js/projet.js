window.onload = function() {

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}


 var projetId = getParameterByName('projet');
 var openshiftWebSocketPort = 8000; // Or use 8443 for wss
 var wsUriPageProjet = "ws://" + window.location.hostname + ":" + openshiftWebSocketPort + "/pageprojetws/?projet="+projetId;
 var titrePage = document.getElementById('titrePage');
 var titre = document.getElementById('titre');
 var description = document.getElementById('description');
 var doapDiv = document.getElementById('doapDiv');
 var methodeDiv = document.getElementById('methodeDiv');
 var statutTitre = document.getElementById('statutTitre');
 var statutDoap = document.getElementById('statutDoap');
 var statutMethode = document.getElementById('statutMethode');
 var websocketPageProjet = new WebSocket(wsUriPageProjet);

 
websocketPageProjet.onopen = function(event) {
  statutTitre.innerHTML = 'Connected to: ' + event.currentTarget.url;
  statutTitre.className = 'open';

 // websocketPageProjet.send('100');
};

// Handle any errors that occur.
websocketPageProjet.onerror = function(error) {
  console.log('WebSocket PageProjet Error: ' + error);
};

// Show a disconnected message when the WebSocket is closed.
websocketPageProjet.onclose = function(event) {
  statutTitre.innerHTML = 'Disconnected from WebSocket Page Projet.';
  statutTitre.className = 'closed';
};


// Handle messages sent by the server.
websocketPageProjet.onmessage = function(event) {
console.log(event);
  var message = event.data;
 doapDiv.innerHTML ='';

console.log(event.data);
obj = JSON.parse(event.data);
var i=0;
for (var key in obj) {
  if (obj.hasOwnProperty(key)) {
doapDiv.innerHTML += '<li class="received"><span>'+obj[key].object+': '+
'</span></br>' +
'<form id="doap-form" action="#" method="post">'+
'<textarea  id="'+obj[key].subject+'" placeholder="Aucune information pour l\'instant. Libre à vous de compléter"></textarea >'+
'<button type="submit">Enregistrer</button><button type="button" id="effacer">Effacer</button></form></li>';
  }
}
                             
                             
};

};