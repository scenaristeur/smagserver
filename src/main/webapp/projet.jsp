<!DOCTYPE html>
<html lang="en">
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

  <div id="page-wrapper">
      <input type="button" value="Tous les projets / Proposer un projet" onclick="location.href='accueil.jsp'">
    <ul id="statutTitre">veuillez patienter, chargement en cours</ul>
    <h1 id=titre">Smag0 détail d'un projet </h1>
    <ul id="description">description </ul>
    <input type="button" onclick="return toggleMe('doapDiv')" value="Afficher/Cacher DOAP"><br>
    <ul id="doapDiv"><li>Recherche du DOAP du projet, veuillez patienter, <div id="statutDoap">chargement en cours</div></li></ul>
    <ul id="methodeDiv">Methode de resolution du projet, <div id="statutMethode">chargement en cours</div></ul>
      </div>

  <script src="/js/projet.js"></script>
</body>
</html>