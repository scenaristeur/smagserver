<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="utf-8">
  <title id="titrePage">Smag</title>

  <link rel="stylesheet" href="/css/style.css">
</head>
<body>
	<script src="/js/processing.min.js"></script>
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

	<div id="page-left"><!--Visualisation</br>-->
		<canvas id="projetPde" width="300" height="300" data-processing-sources="pde/smag/smag.pde /pde/smag/Noeud.pde"></canvas></br>
		<!--<button type="button" onclick="drawSomeText('projetPde')">Actualiser</button>-->
	</div>
	<div id="page-right">Emplacement Agent Projet<div id="agentDiv"></div></div>
			      <input type="button" value="Cliquez ici pour voir tous les projets ou proposer un projet, vous pouvez aussi descendre pour voir les détails de celui-ci" onclick="location.href='accueil.jsp'">
		<div id="methode">
		</div>

		<div class="gauche">
			  DOAP : description of a Project ( <a href="https://github.com/edumbill/doap/wiki">?</a> )<br>
			  (dev en cours : les boutons seront bientôt actif, pour modifier le projet)
			    <input type="button" onclick="return toggleMe('doapDiv')" value="Afficher/Cacher DOAP">
			 <ul id="doapDiv">
					<li>Recherche du DOAP du projet, veuillez patienter, <div id="statutDoap">chargement en cours</div>
					</li>
			</ul>


	</div>

	<div id="liaisons">Liaisons entre les projets<br>
		<ul id="projetsSimilairesDiv">
			<li >Projets avec un nom similaire</li>
		</ul>
	</div>


  <div id="page-wrapper">

      <ul id="statutTitre">veuillez patienter, chargement en cours</ul>
		<h1 id="titreProjet">Smag0 détail d'un projet </h1>
		<ul id="descriptionProjet">description </ul>
     </br>
     <ul> 
		<input type="button"  id="ajouterInfo" onclick="return toggleMe('ajouteInfoDiv')" value="Ajouter une information en rapport avec ce projet (dev en cours)" >
		<div id="ajouteInfoDiv">
			<form id="ajouteInfo-form" action="#" method="post">
				Insérez une info au format rdf : Sujet / Propriété / Objet </br>(exemple : <i>mon robot / possède la capacité de / se déplacer</i> ou </br>
				<i>mon projet / complément d'information / les infos que j'ai à rajouter, blablabla...</i>)</br>
				<input type="text" id="infoSujet" placeholder="Sujet" required> </input>
				   <!-- disabled=false    <div>
					  <input type="radio" name="checkPropriete" value="libre" onclick="proprieteLibre()" checked="true">Texte Libre
						<input type="radio" name="checkPropriete" value="existante" onclick="proprieteExistante()">Propriété existante
				   </div> -->
				<select id="selectPropriete" >
					<option value="indetermine">Lien indéterminé (Dev en cours)</option>
					<option value="sameAS">Est identique à</option>
					<option value="partOf">Est un composant de</option>
					<option value="other">Selectionner une les proprriétés d'une ontologie</option>
				</select>
				<input type="text" id="infoPropriete" placeholder="Entrez une propriété qui relie le sujet et l'objet" required></input>
				<textarea id="infoObjet" placeholder="Entrez l'objet" required></textarea>
				<button type="submit">Ajouter</button>
				<button type="button" id="effaceInfo">Effacer</button>
			</form>
		</div>
	</ul>
			<canvas id="methodePde"  width="100" height="100" data-processing-sources="pde/diamond/diamond.pde /pde/diamond/Noeud.pde"></canvas></br>
			<div id="affiche-methode">
			</div>

      </div>
 <!-- <script src="/js/projetPde.js"></script> -->
  <script src="/js/projet.js"></script>

</body>
</html>