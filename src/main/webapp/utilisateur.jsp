<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="utf-8">
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
    <!-- AJOUTE OBJET CONNECTE -->
    <input type="button" type="button" id="ajouterObjet" onclick="return toggleMe('ajouteObjetConnecteDiv')" value="Ajouter un nouvel objet connecté" >
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
        <input type="button" type="button" id="ajouterService" onclick="return toggleMe('ajouteServiceDiv')" value="Ajouter un nouveau service" >
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
