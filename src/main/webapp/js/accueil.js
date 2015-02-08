window.onload = function() {

  // Get references to elements on the page.
  var form = document.getElementById('message-form');
  var messageField = document.getElementById('message');
   var nomField = document.getElementById('nomProjet');
  var messagesList = document.getElementById('messages');
  var socketStatus = document.getElementById('status');
  var socketStatusNouveau = document.getElementById('statusNouveau');
  var socketStatusListe = document.getElementById('statusListe');
  var closeBtn = document.getElementById('close');

  // The rest of the code in this tutorial will go here...
  // Create a new WebSocket.
var socket = new WebSocket('ws://echo.websocket.org');
  var openshiftWebSocketPort = 8000; // Or use 8443 for wss
  var wsUriNouveauProjet = "ws://" + window.location.hostname + ":" + openshiftWebSocketPort + "/nouveauprojetws";
  var wsUriListeProjets = "ws://" + window.location.hostname + ":" + openshiftWebSocketPort + "/listeprojetsws";
 var websocketNouveauProjet = new WebSocket(wsUriNouveauProjet);
  var websocketListeProjets = new WebSocket(wsUriListeProjets);

// Show a connected message when the WebSocket is opened.
socket.onopen = function(event) {
  socketStatus.innerHTML = 'Connected to: ' + event.currentTarget.url;
  socketStatus.className = 'open';
};
websocketNouveauProjet.onopen = function(event) {
  socketStatusNouveau.innerHTML = 'Connected to: ' + event.currentTarget.url;
  socketStatusNouveau.className = 'open';
};
websocketListeProjets.onopen = function(event) {
  socketStatusListe.innerHTML = 'Connected to: ' + event.currentTarget.url;
  socketStatusListe.className = 'open';
  websocketListeProjets.send('100');
};

// Handle any errors that occur.
socket.onerror = function(error) {
  console.log('WebSocket Echo Error: ' + error);
};
// Handle any errors that occur.
websocketNouveauProjet.onerror = function(error) {
  console.log('WebSocket Nouveau Error: ' + error);
};
// Handle any errors that occur.
websocketListeProjets.onerror = function(error) {
  console.log('WebSocket Liste Error: ' + error);
};

// Send a message when the form is submitted.
form.onsubmit = function(e) {
  e.preventDefault();

  // Retrieve the message from the textarea.
  var message = messageField.value;
  var projet = nomField.value;
  var data = "{ \"type\" : \"nouveauProjet\", "+
    "\"titre\": \""+ projet +"\", "+
    "\"description\": \""+ message +"\", "+
    "\"date\": \""+Date.now()+"\""+
  "}";
  console.log(data);

  // Send the message through the WebSocket.
  socket.send(message);
  websocketNouveauProjet.send(data);

  // Add the message to the messages list.
  messagesList.innerHTML += '<li class="sent"><span>Sent:</span>' + message +
                            '</li>';
  messagesList.innerHTML += '<li class="sent"><span>Sent:</span>' + data +
                            '</li>';

  // Clear out the message field.
  messageField.value = '';

  return false;
};


// Handle messages sent by the server.
socket.onmessage = function(event) {
  var message = event.data;
  messagesList.innerHTML += '<li class="received"><span>Received:</span>' +
                             message + '</li>';
};
//recupere ListeProjets
websocketListeProjets.onmessage = function(event){
var message = event.data;
console.log(event.data);
obj = JSON.parse(event.data);
var i=0;
for (var key in obj) {
  if (obj.hasOwnProperty(key)) {
messagesList.innerHTML += '<li class="received"><span><a href=\"projet.jsp?projet='+obj[key].projet+'\">'+obj[key].titre+': </a></span></br>' +
                             obj[key].description + '</li>';
  }

   }
};


// Show a disconnected message when the WebSocket is closed.
socket.onclose = function(event) {
  socketStatus.innerHTML = 'Disconnected from WebSocket.';
  socketStatus.className = 'closed';
};
// Show a disconnected message when the WebSocket is closed.
websocketNouveauProjet.onclose = function(event) {
  socketStatusNouveau.innerHTML = 'Disconnected from WebSocket Nouveau Projet.';
  socketStatusNouveau.className = 'closed';
};
// Show a disconnected message when the WebSocket is closed.
websocketListeProjets.onclose = function(event) {
  socketStatusListe.innerHTML = 'Disconnected from WebSocket ListeProjets.';
  socketStatusListe.className = 'closed';
};

// Close the WebSocket connection when the close button is clicked.
closeBtn.onclick = function(e) {
  e.preventDefault();

  // Close the WebSocket.
  socket.close();

  return false;
};

};

