<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="utf-8">
  <meta http-equiv="Content-Script-Type" content="text/javascript">
  <title>Smag 0, résoudre un problème complexe à l'aide d'ontologies et de système multi-agent</title>
  <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<script type="text/javascript">
	function toggleMe(a){
		var e=document.getElementById(a);
		if(!e)return true;
			if(e.style.display=="none"){
			e.style.display="block"
			}
			else{
				e.style.display="none"
			}
			return true;
	}  
</script>
<!--<script src="/js/processing.min.js"></script>-->
  <div id="page-wrapper">
    <h1 id="titre">Smag0</h1>
  <!--  <canvas id="projetsPde"  width="480" height="320" data-processing-sources="pde/projets/projets.pde pde/projets/Projet.pde"></canvas>
    -->
     <input type="button" value="Tous les projets / Proposer un projet" onclick="location.href='accueil.jsp'">
   
           <!-- LISTE Liens Utilisateur -->
        <input type="button" id="listerObjet" onclick="return toggleMe('lienUtilisateurDiv')" value="Les liens cet utilisateur" >
       <div id="lienUtilisateurDiv">
       Liens de cet utilisateur , Chargement des informations
       </div>
    	<input type="button" id="ajouterLien" onclick="return toggleMe('ajouteLienUtilisateurDiv')" value="Ajouter un nouveau lien pour cet utilisateur" >
       <div id="ajouteLienUtilisateurDiv">
       <form id="ajouteLienForm" action="#" method="post">
       <input id="emailUtilisateur" type="hidden"></input>
       <fieldset>
  			<legend>  Selection du lien :  </legend>
  					<a href="http://lov.okfn.org/dataset/lov/terms" target="_blank">Rechercher une propriété ou un concept</a></br>
        <select id="lienSimple">
       		<option value="">Lien simple :</option>  
  			<option value="sameAs">Est identique à</option>
  			<option value="type"> Est un/une (=est de type)</option>
  			<option value="isPartOf">Est un composant de</option>
  			<option value="isInterestedIn">Est interessé par</option>
  			<option value="lookFor">Rechercher si une propriété existe (dev en cours)</option>
  			<option value="other">Ajouter une nouvelle propriété (dev en cours)</option>
		</select></br>
		<select id="lienComplexe">
			<option value="">Lien complexe :</other>
			<option value="foaf">Friend Of A Friend - Vocabulaire des relations entre les personnes</option>
			<option value="wai">Rôles et profiles dans une organisation</option>
			<option value="doap">DOAP - vocabulaire d'un projet</option>
			<option value="diamond">Diamond - vocabulaire de la méthode Diamond</option>
			<option value="smag">Smag0 -  Euh là c'est pour enregistrer une nouvelle propriete sur le serveur de l'appli</option>
		</select>
		<select id="lienDansVoc">
			<option value="">Choisissez une propriété dans ce vocabulaire :</option>
			<option value="prop1">prop1</option>
			<option value="prop2">prop2</option>
			<option value="prop3">prop3</option>
		</select>
		<input id="lienAjoute" placeholder="Saisissez un lien (une relation), ou selectionnez le grâce aux listes déroulantes" required></input>
 	</fieldset>
      
<fieldset>
	<legend>  Entrez ou selectionnez la destination du lien :  </legend>
      <textarea id="lienObjet" placeholder="Entrez ou selectionnez la destination du lien (les doubles quotes ne passent pas)" required></textarea>
     </fieldset>
      <button type="submit">Enregistrer</button>
    <!--  <button type="button" id="close">Close Connection</button> -->
    </form>
    </div>
   
        <!-- LISTE OBJETS CONNECTES -->
        <input type="button" id="listerObjet" onclick="return toggleMe('listeObjetConnecteDiv')" value="Les objets connectes de cet utilisateur" >
       <div id="listeObjetConnecteDiv">
       Liste des objets connectes , Chargement des informations
       </div>
    <input type="button" id="ajouterObjet" onclick="return toggleMe('ajouteObjetConnecteDiv')" value="Ajouter un nouvel objet connecté ou service" >
       <div id="ajouteObjetConnecteDiv">
       <form id="ajouteObjetForm" action="#" method="post">
       <input id="emailNouvelObjet" type="hidden"></input>
      <input id="nomObjet" placeholder="Nom de l'objet..." required></input>
       <input id="adresseIpObjet" placeholder="Adresse IP" required></input>
        <input id="portObjet" placeholder="port" required></input>
      <textarea id="commentaireObjet" placeholder="Entrez la description de votre objet connecté... (les doubles quotes ne passent pas)" required></textarea>
      <button type="submit">Enregistrer</button>
    <!--  <button type="button" id="close">Close Connection</button> -->
    </form>
    </div>
 <!--  AJOUTE SERVICE -->   
        <input type="button" id="ajouterService" onclick="return toggleMe('ajouteServiceDiv')" value="Ajouter un nouveau service" >
      <div id="ajouteServiceDiv">
       <form id="ajouteServiceForm" action="#" method="post">
       <input id="emailNouvelObjet" type="hidden"></input>
      <input id="nomObjet" placeholder="Nom de l'objet..." required></input>
       <input id="adresseIpObjet" placeholder="Adresse IP ou socket" required></input>
        <input id="portObjet" placeholder="Port (éventuellement)"></input>
      <textarea id="commentaireObjet" placeholder="Entrez la description de votre objet connecté... (les doubles quotes ne passent pas)" required></textarea>
      <button type="submit">Enregistrer</button>
    <!--  <button type="button" id="close">Close Connection</button> -->
    </form>
    </div>
 <!--   <div id="status">Connecting...</div>
    <div id="statusNouveau">Connecting...</div>
    <div id="statusListe">Connecting...</div>-->

    <ul id="messages"></ul>


  </div>

  <script src="/js/utilisateur.js"></script>
</body>
</html>
