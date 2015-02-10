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
      <input type="button" name="b1" value="Tous les projets / Proposer un projet" onclick="location.href='accueil.jsp'">
    <h1><div id=titre">Smag0 détail d'un projet</div> <div id="statutTitre">veuillez patienter, chargement en cours</div></h1>
    <div id="description">description </div>
    <input type="button" onclick="return toggleMe('doapDiv')" value="Afficher/Cacher DOAP"><br>
    <ul id="doapDiv"><li>Recherche du DOAP du projet, veuillez patienter, <div id="statutDoap">chargement en cours</div></li></ul>
    <h1 id="methodeDiv">Methode de resolution du projet, <div id="statutMethode">chargement en cours</div></h1>
      </div>

  <script src="/js/projet.js"></script>
</body>
</html>