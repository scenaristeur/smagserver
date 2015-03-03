window.onload = function() {

		
	function getParameterByName(name) {
		name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
		var regex = new RegExp("[\\?&]" + name + "=([^&#]*)");
		results = regex.exec(location.search);
		return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
	};

	// global var
		var openshiftWebSocketPort = 8000; // Or use 8443 for wss
		var lienAjouteLien=null;
		var vocabulaire=null;
		var vocabulaireSource=null;
		var vocabulaireType=null;

	// Get references to elements on the page.
		var ajouteObjetForm = document.getElementById('ajouteObjetForm');
		var ajouteLienForm = document.getElementById('ajouteLienForm');
		var nomObjetField = document.getElementById('nomObjet');
		var adresseIpObjetField = document.getElementById('adresseIpObjet');
		var portObjetField = document.getElementById('portObjet');
		var commentaireObjetField = document.getElementById('commentaireObjet');
		var emailNouvelObjetField = document.getElementById('emailNouvelObjet');
		var listeObjetConnecteDiv = document.getElementById('listeObjetConnecteDiv');
		var lienAjouteField = document.getElementById('lienAjoute');
		var lienObjetField = document.getElementById('lienObjet');
		var listeLiensUtilisateur=document.getElementById('listeLiensUtilisateur');
		var emailNouveauLienField = document.getElementById('emailNouveauLien');
		var lienDansVoc = document.getElementById('lienDansVoc');
		var email = getParameterByName('email');
		var uri = getParameterByName('projet');
		var projetId = getParameterByName('projet');
		var titre = document.getElementById('titre');
		var message = document.getElementById('message');

	//Sockets
		var wsUtilisateur = "ws://" + window.location.hostname + ":" + openshiftWebSocketPort + "/utilisateurws";
		var wsListeObjetsConnectes = "ws://" + window.location.hostname + ":" + openshiftWebSocketPort + "/listeobjetsconnectesws";
		var wsRelation = "ws://" + window.location.hostname + ":" + openshiftWebSocketPort + "/relationws";
		var websocketUtilisateur = new WebSocket(wsUtilisateur);
		var websocketListeObjetsConnectes = new WebSocket(wsListeObjetsConnectes);
		var websocketRelation = new WebSocket(wsRelation);

	//Affichage
		toggleMe('ajouteServiceDiv');
		lienDansVoc.style.visibility="hidden";
		console.log(email);
		
		if (email==""){
				titre.innerHTML="Ceci est la page d'un utilisateur anonyme,";
				message.innerHTML+="vous pouvez créer votre page utilisateur en saisissant votre e-mail <br>";
				message.innerHTML+="ou l'identifiant d'une ressource RDF unique du style : <http://smag0.blogspot.fr/ns/smag0#David></a> </br>";
				my_form=document.createElement('FORM');
				my_form.name='anonymeUser';
				my_form.method='POST';
				my_form.action='#';

				my_tb=document.createElement('INPUT');
				my_tb.type='TEXT';
				my_tb.name='email';
				my_tb.id='email';
				my_tb.placeholder='email ou ressource unique de type rdf';
				my_form.appendChild(my_tb);

				my_tb=document.createElement('INPUT');
				my_tb.type='HIDDEN';
				my_tb.name='hidden1';
				my_tb.value=Date.now();
				my_form.appendChild(my_tb);
				
				my_button=document.createElement('button');
				my_button.type='submit';
				my_button.value='Creer une page utilisateur';
				my_button.innerHTML='Creer une nouvelle page utilisateur';
				my_form.appendChild(my_button);
				
				message.appendChild(my_form);
				
				
				anonymeUser.onsubmit = function(e) {
					e.preventDefault();
					console.log("test");
					console.log(anonymeUser.email.value);
					window.location.href ="/utilisateur.jsp?email="+anonymeUser.email.value;
					};
				/*
				message.innerHTML+="vous pouvez créer votre page utilisateur en saisissant votre e-mail : </br>";
				message.innerHTML+="<
				message.innerHTML+="<a href=\"utilisateur.jsp?email=blabla\">Creer ma page</a>";*/
			}else{
				titre.innerHTML=email;
				message.disable='disabled';
			};
		
	//FORMULAIRES
	ajouteObjetForm.onsubmit = function(e) {
		e.preventDefault();
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
		websocketUtilisateur.send(data);
		ajouteObjetConnecteDiv.disable="true";
		nomObjet.value = '';
		ajouterObjet.value='Creation de votre objet connecté en cours';
		toggleMe('ajouteObjetConnecteDiv');
		demandeUpdateObjetsConnectes();
		return false;
	};

		
	//Fonction des Sockets    
		websocketUtilisateur.onopen = function(evt) { onOpenUtilisateur(evt) };
		websocketUtilisateur.onclose = function(evt) { onCloseUtilisateur(evt) };
		websocketUtilisateur.onmessage = function(evt) { onMessageUtilisateur(evt) };
		websocketUtilisateur.onerror = function(evt) { onErrorUtilisateur(evt) };
		
		websocketListeObjetsConnectes.onopen = function(evt) { onOpenListeObjetsConnectes(evt) };
		websocketListeObjetsConnectes.onclose = function(evt) { onCloseListeObjetsConnectes(evt) };
		websocketListeObjetsConnectes.onmessage = function(evt) { onMessageListeObjetsConnectes(evt) };
		websocketListeObjetsConnectes.onerror = function(evt) { onErrorListeObjetsConnectes(evt) };
		
		websocketRelation.onopen = function(evt) { onOpenRelation(evt) };
		websocketRelation.onclose = function(evt) { onCloseRelation(evt) };
		websocketRelation.onmessage = function(evt) { onMessageRelation(evt) };
		websocketRelation.onerror = function(evt) { onErrorRelation(evt) };

	//SOCKET UTILISATEUR
	function onOpenUtilisateur(evt){
		console.log("onOpenUtilisateur");
		if (email!=""){
			console.log("envoi de l'email : "+email);
			emailNouvelObjetField.value=email;
			var data = "{ \"type\" : \"email\", "+
						"\"email\": \""+ email +"\", "+
						"\"date\": \""+Date.now()+"\""+
						"}";
			console.log(data);
			websocketUtilisateur.send(data);
		};
	};
		
	function onCloseUtilisateur(evt){
		console.log("onCloseUtilisateur");
	};
	function onErrorUtilisateur(evt){
		console.log("onErrorUtilisateur");
	};
	function onMessageUtilisateur(evt){
		console.log("onMessageUtilisateur");
		console.log(evt.data);
		ajouterObjet.value=evt.data;	
	};

	// SOCKET OBJETS CONNECTES
	function onOpenListeObjetsConnectes(evt){
		console.log("onOpenListeObjetsConnectes");
		demandeUpdateObjetsConnectes();
	};
	function onCloseListeObjetsConnectes(evt){
		console.log("onCloseListeObjetsConnectes");
	};
	function onErrorListeObjetsConnectes(evt){
		console.log("onErrorListeObjetsConnectes");
	};
	function onMessageListeObjetsConnectes(evt){
		console.log("onMessageListeObjetsConnectes");
		console.log(evt.data);
		var listeObjetsUtilisateur = document.createElement('ul');
		listeObjetConnecteDiv.appendChild(listeObjetsUtilisateur);
		var ligneObjetConnecte = document.createElement('li');
		listeObjetsUtilisateur.appendChild(ligneObjetConnecte);
		obj = JSON.parse(event.data);
		var i=0;
		ligneObjetConnecte.innerHTML+="<div id="+obj.objetConnecte+" title=\""+obj.description+"\"><a href=\""+obj.adresseIpObjet+":"+obj.portObjet+"\" target=\"_blank\">"+obj.titre+"</a> </div>";
		  //	ligneObjetConnecte.innerHTML+="   ......   indicateur dispo ou non</br>... Laisser un message à cet objet connecté / à son gestionnaire ";
	};
	
	function demandeUpdateObjetsConnectes(){
		if (email!=""){
			console.log("envoi de l'email : "+email);
			emailNouvelObjetField.value=email;
			var data = "{ \"type\" : \"update\", "+
							"\"email\": \""+ email +"\", "+
							"\"date\": \""+Date.now()+"\""+
							"}";
			//console.log(data);	
			listeObjetConnecteDiv.innerHTML = "";										 
			websocketListeObjetsConnectes.send(data); 
			return false;
		};
	};

	// gestion du formulaire d'ajout d'un lien

	var radiosValeur = document.getElementsByName("saisieValeur");
		for(var i = 0; i < radiosValeur.length; i++){
			if(radiosValeur[i].checked){
				var val = radiosValeur[i].value;
				console.log(val);
			};
		};

	ajouteLienForm.onsubmit= function(e) {
		e.preventDefault();
		var lienAjouteValue=lienAjouteField.value;
		var lienObjetValue=lienObjetField.value;
		var debutObjet=lienObjetValue.slice(0, 1);
		var finObjet=lienObjetValue.slice(-1);
		var typeObjet="texte";
		if ((debutObjet=="<")&&(finObjet==">")){
				typeObjet="ressource";
		};
		//test pour voir si on a affaire à une REssource (caractères < et > )
		console.log(lienObjetValue.slice(-1)+" "+lienObjetValue.slice(0, 1));
		var emailNouveauLienValue = email;
		var data = "{ \"type\" : \"nouveauLien\", "+
					"\"lienAjouteValue\": \""+ lienAjouteValue +"\", "+
					"\"lienObjetValue\": \""+ lienObjetValue +"\", "+
					"\"emailNouveauLienValue\": \""+ emailNouveauLienValue +"\", "+
					"\"typeObjet\": \""+ typeObjet +"\", "+
					"\"date\": \""+Date.now()+"\""+
					"}";
		console.log(data);
		websocketRelation.send(data);
		/*ajouteObjetConnecteDiv.disable="true";
		nomObjet.value = '';
		ajouterObjet.value='Creation de votre objet connecté en cours';
		toggleMe('ajouteObjetConnecteDiv');*/
		demandeUpdateLiens();
		return false;
	};


	lienSimple.onchange=function(){
		console.log(lienSimple.value);
		switch (lienSimple.value){
			case "sameAs":
				/*var propPrefix = document.createElement('b');
				var propExpliq = document.createElement('i');
				propPrefix.innerHTML="owl:sameAs";
				propExpliq.innerHTML=" est identique à";
				lienAjouteLien.appendChild(propPrefix);
				lienAjouteLien+="http://www.w3.org/2002/07/owl#sameAs";
				lienAjouteLien.appendChild(propExpliq);*/
				lienAjouteLien="http://www.w3.org/2002/07/owl#sameAs";
				break;
			case "type":   	
				lienAjouteLien="http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
				break;
			case "isPartOf":
				lienAjouteLien="http://purl.org/dc/terms/isPartOf";
				break;
			case "isInterestedIn":
				lienAjouteLien="http://www.ontotext.com/proton/protonkm#isInterestedIn";
				break;
			default : 
				lienAjouteLien="http://smag0.blogspot.fr/ns/smag0#relationIndeterminee";
				break;
		};
		lienAjoute.value=lienAjouteLien;
		lienComplexe.value="";
		lienDansVoc.style.visibility="hidden";
	};


	lienComplexe.onchange=function(){
		console.log(lienComplexe.value);
		lienAjoute.value="";
		switch (lienComplexe.value){
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
				vocabulaireSource="http://purl.org/wai#";
				vocabulaireType="rdf/xml";
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
		};

		var data = "{ \"type\" : \"listeProprietes\", "+
						"\"vocabulaire\": \""+ vocabulaire +"\", "+
						"\"vocabulaireSource\": \""+ vocabulaireSource +"\", "+
						"\"vocabulaireType\": \""+ vocabulaireType +"\", "+
						"\"date\": \""+Date.now()+"\""+
						"}";
		console.log(data);
		lienDansVoc.options.length = 0;
		var option = document.createElement("option");
		option.text = "Chargement des proprietes en cours, Veuillez patienter";
		lienDansVoc.add(option);
		websocketRelation.send(data);
		lienSimple.value="";
		lienDansVoc.style.visibility="hidden";
		lienDansVoc.value="";
		lienAjoute.value+=vocabulaire;
		//lienAjoute.value=lienComplexe.value;
	};

	lienDansVoc.onchange=function(){
		console.log(lienDansVoc.value);
		lienAjoute.value=lienDansVoc.value;
		lienAjoute.text=lienDansVoc.value;
	};

	function onOpenRelation(evt){
			console.log("onOpenRelation");
			demandeUpdateLiens();
		};
	function onCloseRelation(evt){
			console.log("onCloseRelation");
		};
	function onErrorRelation(evt){
			console.log("onErrorRelation");
		};
	function onMessageRelation(evt){
		lienDansVoc.options.length = 0;
		console.log("onMessageRelation");
		console.log(evt.data);
		obj = JSON.parse(evt.data);
		if (obj.hasOwnProperty('type')){
			console.log("reponse de update"+obj['type']);
			if (obj['type']=="update"){
				delete obj['type'];
				console.log("reponse de update apres remove"+obj['type']);
				for (var key in obj) {
					if (obj.hasOwnProperty(key)) {
						console.log ( obj[key].ressourceShort+" "+obj[key].lien+" "+obj[key].proprieteEtendue+" "+obj[key].objetEtendu);
						var ligneLien = document.createElement('li');
						ligneLien.innerHTML+="<div id="+obj[key].ressourceShort+" title=\""+obj[key].ressourceShort+"\"><a href=\"ressource.jsp?ressource="+obj[key].ressourceShort+"\" target=\"_blank\">"+obj[key].ressourceShort+"</a> <span id=\"mini\"><i> lien avec cet utilisateur :</i></span> "+obj[key].lien+"</br>";						
						ligneLien.innerHTML+="<i><span id=\"mini\">Caractéristique de cette ressource :</span></i> "+obj[key].proprieteEtendue+"</br>  <span id=\"mini\"><i>a pour valeur :</i></span> "+obj[key].objetEtendu+"</div>";
						listeLiensUtilisateur.appendChild(ligneLien);	
					};
				};
			}else{
				console.log("le type n'est pas update mais "+obj['type'])
			};
		}else{
			for (var key in obj) {
				if (obj.hasOwnProperty(key)) {
					var option = document.createElement("option");
					option.text = obj[key].propriete;
					option.value = obj[key].propriete;
					option.id = obj[key].propriete;
					lienDansVoc.add(option);
				}
			}
			lienDansVoc.style.visibility="visible";
		};
	};
	
	function demandeUpdateLiens(){
			if (email!=""){
				console.log("demande update liens : "+email);
				//emailNouvelObjetField.value=email;
				var data = "{ \"type\" : \"update\", "+
							"\"email\": \""+ email +"\", "+
							"\"date\": \""+Date.now()+"\""+
							"}";
				//console.log(data);	
				listeObjetConnecteDiv.innerHTML = "";										 
				websocketRelation.send(data); 
				return false;
			};
		};
};