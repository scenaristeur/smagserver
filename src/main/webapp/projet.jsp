<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="utf-8">
  <title id="titrePage">Smag</title>

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

<div id="page-left">Emplacement Processing</div>
<div id="page-right">Emplacement Agent Projet<div id="agentDiv"></div></div>
<div id="methode"></div>


<div id="affiche-methode">
Affichage de la méthode</div>
<div id="liaisons">Liaisons entre les projets<br>
<ul>
<li>Projets avec un nom similaire</li>
</ul>
</div>


  <div id="page-wrapper">
      <input type="button" value="Tous les projets / Proposer un projet" onclick="location.href='accueil.jsp'">
    <ul id="statutTitre">veuillez patienter, chargement en cours</ul>
    <h1 id="titreProjet">Smag0 détail d'un projet </h1>
    <ul id="descriptionProjet">description </ul>
    <input type="button" onclick="return toggleMe('doapDiv')" value="Afficher/Cacher DOAP (description of a Project)"><br>
    <ul id="doapDiv"><li>Recherche du DOAP du projet, veuillez patienter, <div id="statutDoap">chargement en cours</div></li></ul>
      </div>

  <script src="/js/projet.js"></script>
</body>
</html>