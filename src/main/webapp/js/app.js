window.onload = function() {

  // Get references to elements on the page.
  var form = document.getElementById('message-form');
  var messageField = document.getElementById('message');
  var messagesList = document.getElementById('messages');
  var socketStatus = document.getElementById('status');
  var closeBtn = document.getElementById('close');

  // The rest of the code in this tutorial will go here...
  // Create a new WebSocket.
var socket = new WebSocket('ws://echo.websocket.org');

// Show a connected message when the WebSocket is opened.
socket.onopen = function(event) {
  socketStatus.innerHTML = 'Connected to: ' + event.currentTarget.url;
  socketStatus.className = 'open';
};

// Handle any errors that occur.
socket.onerror = function(error) {
  console.log('WebSocket Error: ' + error);
};

// Send a message when the form is submitted.
form.onsubmit = function(e) {
  e.preventDefault();

  // Retrieve the message from the textarea.
  var message = messageField.value;

  // Send the message through the WebSocket.
  socket.send(message);

  // Add the message to the messages list.
  messagesList.innerHTML += '<li class="sent"><span>Sent:</span>' + message +
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

// Show a disconnected message when the WebSocket is closed.
socket.onclose = function(event) {
  socketStatus.innerHTML = 'Disconnected from WebSocket.';
  socketStatus.className = 'closed';
};

// Close the WebSocket connection when the close button is clicked.
closeBtn.onclick = function(e) {
  e.preventDefault();

  // Close the WebSocket.
  socket.close();

  return false;
};
  
  
};

