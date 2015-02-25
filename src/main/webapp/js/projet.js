window.onload = function() {

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

       function drawSomeText(id) {
         var pjs = Processing.getInstanceById(id);
         //var text = document.getElementById('inputtext').value;
         var text='test';
         pjs.drawText(text); }

function detailProjetPde(id, objDetail){
         var pjs = Processing.getInstanceById(id);
         var objDetailJson = objDetail;
         pjs.drawDetails(objDetailJson);    
         };
function drawMethode(id, objMethode){
		var i=0;
        var pjs = Processing.getInstanceById(id);
 		for (var key in obj) {
 			if (obj.hasOwnProperty(key)) {
 		 		i++;
 		 		pjs.addNoeud(obj[key].subject,10, 20*i);
 		 		pjs.addNoeud(obj[key].object,300, 20*i);
 		 		console.log("ajoute point"+obj[key].subject);
 		 	}
 		}
        };
         
function setNomProjetPde(id, nomProjet){
         var pjs = Processing.getInstanceById(id);
         var nomProjet = nomProjet;
         pjs.setNomProjetPde(nomProjet);    
         };
function setIDProjetPde(id, _IDProjetPde){
         var pjs = Processing.getInstanceById(id);
         var IDProjetPde = _IDProjetPde;
         console.log(IDProjetPde);
         pjs.setIDProjetPde(IDProjetPde);    
         };
         
         
         
var selectPropriete= document.getElementById('selectPropriete');
var infoPropriete= document.getElementById('infoPropriete');
	function proprieteLibre(){
		selectPropriete.disabled=true;
		infoPropriete.disabled=false;

	};
	function proprieteExistante(){
		selectPropriete.disabled=false;
		infoPropriete.disabled=true;
	};

    

 var projetId = getParameterByName('projet');
 var openshiftWebSocketPort = 8000; // Or use 8443 for wss
 var wsUriDoapModel = "ws://" + window.location.hostname + ":" + openshiftWebSocketPort + "/doapmodelws";
 var wsUriPageProjet = "ws://" + window.location.hostname + ":" + openshiftWebSocketPort + "/pageprojetws/?projet="+projetId;
 var wsUriMethode = "ws://" + window.location.hostname + ":" + openshiftWebSocketPort + "/methodews/?projet="+projetId;
 var titrePage = document.getElementById('titrePage');
 var titreProjet = document.getElementById('titreProjet');
 var descriptionProjet = document.getElementById('descriptionProjet');
 var doapDiv = document.getElementById('doapDiv');
 var methodeDiv = document.getElementById('methode');
 var affichemethode = document.getElementById('affiche-methode');
 var statutTitre = document.getElementById('statutTitre');
 var statutDoap = document.getElementById('statutDoap');
 var statutMethode = document.getElementById('statutMethode');
 var websocketDoapModel = new WebSocket(wsUriDoapModel);
 var websocketPageProjet = new WebSocket(wsUriPageProjet);
 var websocketMethode = new WebSocket(wsUriMethode);
 var agentDiv= document.getElementById('agentDiv');
var objDoap;
var objDetail;
var nomDoap=null;
var descriptionDoap=null;
var urlProjet="http://"+window.location.hostname + ":" + openshiftWebSocketPort + "/projet.jsp?projet="+projetId;;
  var ajouteInfoForm = document.getElementById('ajouteInfo-form'); 
  
  
  // recupere les infos du formulaire ajouteInfo
ajouteInfoForm.onsubmit = function(e) {
    e.preventDefault();

  // Retrieve the message from the textarea.
  var ajouteInfoFormSujet = infoSujet.value;
  var ajouteInfoFormPropriete = infoPropriete.value;
  var ajouteInfoFormObjet = infoObjet.value;
  console.log(e);
  
  console.log(ajouteInfoFormSujet+" "+ajouteInfoFormPropriete+" "+ajouteInfoFormObjet);
/*
 if (websocketNouveauProjet.readyState === undefined || websocketNouveauProjet.readyState > 1) {
websocketNouveauProjet = new WebSocket(wsUriNouveauProjet);
 console.log("reconnexion websocketNouveauProjet");
   socketStatusNouveau.innerHTML = 'Connected to: ' + event.currentTarget.url;
  socketStatusNouveau.className = 'open';*/

 }
 
 
 //doapDiv.style.display="none";
	// setNomProjetPde('projetPde',projetId);
	// setIDProjetPde('projetPde',projetId);
 // ne passe pas ??? trop tot ? setIDProjetPde('projetPde',projetId);

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
var doapform = document.createElement("form");
doapform.setAttribute('method',"post");
doapform.setAttribute('action',"#");
doapDiv.appendChild(doapform);

var i=0;
for (var key in objDoap) {
  if (objDoap.hasOwnProperty(key)) {
  var ligne = document.createElement("li"); //input element, text
ligne.setAttribute('class',"received");
ligne.innerHTML='<span>'+objDoap[key].object+': </span></br>' +
'<textarea  id="'+objDoap[key].subject+'" placeholder="Aucune information pour l\'instant. Libre à vous de compléter"></textarea >'+
'<button type="submit">Enregistrer</button>'+
'<button type="button" id="ajouter">Ajouter</button>'+
'<button type="button" id="modifier">Modifier</button>'+
'<button type="button" id="effacer">Effacer</button>';
doapform.appendChild(ligne);

if (objDoap[key].subject=="name"){
doapform.insertBefore(ligne, doapform.childNodes[0]);
}
else if (objDoap[key].subject=="description"){

doapform.insertBefore(ligne, doapform.childNodes[0]);
}
else if (objDoap[key].subject=="Project"){

doapform.insertBefore(ligne, doapform.childNodes[0]);
}

else{
doapform.appendChild(ligne);
}
/*
doapDiv.innerHTML += '<li class="received"><span>'+objDoap[key].object+': </span></br>' +
'<textarea  id="'+objDoap[key].subject+'" placeholder="Aucune information pour l\'instant. Libre à vous de compléter"></textarea >'+
'<button type="submit">Enregistrer</button><button type="button" id="ajouter">Ajouter</button><button type="button" id="effacer">Effacer</button></li>';
*/
  }
} 

//doapDiv.innerHTML +='</form>';
  		document.getElementById("name").value  = nomDoap;
  		document.getElementById("Project").value = nomDoap;
  		document.getElementById("description").value = descriptionDoap;
        document.getElementById("infoSujet").value = projetId;
};

websocketPageProjet.onmessage = function(event) {
console.log("Page projet:" +event.data);
objDetail=JSON.parse(event.data);
//setProjetPde('projetPde',objDetail);
var i=0;
var lignesProjetsSimilaires=null;
for (var key in objDetail){
	if(objDetail.hasOwnProperty(key)){
	if(key=='messageUtilisateur'){
		console.log(objDetail[key]);
		agentDiv.innerHTML =objDetail[key];
		//setMessagePde('projetPde',objDetail[key]);
	}else if(key=='projetsSimilaires'){
	objProjetSimilaire=objDetail[key];
		console.log(objProjetSimilaire);
		for (var key in objProjetSimilaire){
	if(objProjetSimilaire.hasOwnProperty(key)){
	console.log(objProjetSimilaire[key]);
	lignesProjetsSimilaires+='<li>'+objProjetSimilaire[key].titre+
	//addProjetSimilairePde('projetPde',objProjetSimilaire[key]);
		'<span id="mini">'+objProjetSimilaire[key].description+'</span></li>'
	}}
		projetsSimilairesDiv.innerHTML =lignesProjetsSimilaires;	
	}else if (objDetail[key].propriete=='http://purl.org/dc/elements/1.1/title'){
	 nomDoap=objDetail[key].objet;
	 setNomProjetPde('projetPde',nomDoap);
	 setIDProjetPde('projetPde',projetId);
  		console.log("titre :"+nomDoap);
  		titreProjet.innerHTML=nomDoap;
  		titrePage.innerHTML=nomDoap;
  		if (document.getElementById("name")){
  		document.getElementById("name").value  = nomDoap;
  		document.getElementById("Project").value = nomDoap;
  		}

	}else if (objDetail[key].propriete=='http://purl.org/dc/elements/1.1/description'){
  		descriptionDoap=objDetail[key].objet;
  			// setDescriptionProjetPde('projetPde',descriptionDoap);
  		console.log("description : "+descriptionDoap);
  		descriptionProjet.innerHTML=descriptionDoap;
  		if (document.getElementById("description")){
  		document.getElementById("description").value = descriptionDoap;
  		}
	}	
}
}
};


    websocketMethode.onopen = function(evt) { onOpenMet(evt) };
    websocketMethode.onclose = function(evt) { onCloseMet(evt) };
    websocketMethode.onmessage = function(evt) { onMessageMet(evt) };
    websocketMethode.onerror = function(evt) { onErrorMet(evt) };
    
    function onOpenMet(evt){
console.log("onopenmet");
 websocketMethode.send("diamond");
}
function onCloseMet(evt){
console.log("onclosemet");
}
function onMessageMet(evt){
console.log("methode JSON reçue : "+evt.data);
//websocketMethode.send("subclasses");
/*websocketMethode.close();*/
obj = JSON.parse(evt.data);
drawMethode('projetPde',obj);
var i=0;
var reponse='';
var sujet='';
var types='subClassOf : ';
var hasPart='';
var first='';
var documentproduit='Document produit : ';
var autres='voir aussi : ';
var listeDiamond = document.createElement('ul');
for (var key in obj) {
  if (obj.hasOwnProperty(key)) {
  i++;
  if (obj[key].subject!=sujet){
  	sujet=obj[key].subject;
  	affichemethode.innerHTML=sujet; 	
  	affichemethode.appendChild(listeDiamond);
  }  
  if (obj[key].predicate=='type'){
  	console.log(obj[key].predicate+obj[key].object);
  	types+=obj[key].object+" ";
  }else if (obj[key].predicate=='hasPart'){
  	console.log(obj[key].predicate+obj[key].object);
  	hasPart+=" <a href=\""+urlProjet+"&etape="+obj[key].object+"\">"+obj[key].object+"</a> ";
  	var ligneDiamond = document.createElement('li');
  	listeDiamond.appendChild(ligneDiamond);
  	ligneDiamond.innerHTML+="<div id="+obj[key].object+"><a href=\""+urlProjet+"&etape="+obj[key].object+"\">"+obj[key].object+"</a></div>";
  	 websocketMethode.send(obj[key].object);
  }
  else if (obj[key].predicate=='first'){
  	console.log(obj[key].predicate+obj[key].object);
  	first+=obj[key].object+" ";
  }
    else if (obj[key].predicate=='documentproduit'){
  	console.log(obj[key].predicate+obj[key].object);
  	documentproduit+=" <a href=\""+urlProjet+"&etape="+obj[key].object+"\">"+obj[key].object+"</a> ";
  	  	var ligneDiamond = document.createElement('li');
  	listeDiamond.appendChild(ligneDiamond);
  	ligneDiamond.innerHTML+="<a href=\""+urlProjet+"&etape="+obj[key].object+"\">"+obj[key].object+"</a>";
  }
  else{
  	autres+=obj[key].object+" ";
  	}
  }
}
affichemethode.innerHTML+='</ul>'; 
reponse+=sujet+'(<a id = "mini" href="https://tel.archives-ouvertes.fr/tel-00189046/document" target="_blank">description de la methode</a>)';
reponse+=" ("+types+")";
//reponse+="<h2>"+first+"</h2>";
reponse+="<div>"+hasPart+"</div>";
reponse+="<div>"+documentproduit+"</div>";
reponse+=autres;
reponse+='<a id ="mini" href="http://smag0.rww.io/diamond.owl" target="_blank">détails de la méthode</a>';
reponse+='<a id ="mini" href="https://drive.google.com/file/d/0B0zEK4yLB5C6V0xaa3VtLXllQXM/view?usp=sharing" target="_blank">Smag0 sur votre mobile Android</a>';
       writeToMethode(reponse);
        

}
function onErrorMet(evt){
console.log("onerrormet");
writeToMethode(evt.data, 'error');
}

    function writeToMethode(message,rule)
  {
  console.log("ecriture dans methodetab : "+message);
  methodeDiv.innerHTML=message;
    methodeDiv.className=rule;
  }

};