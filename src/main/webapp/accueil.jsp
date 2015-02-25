<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="utf-8">
  <title>Smag 0, résoudre un problème complexe à l'aide d'ontologies et de système multi-agent</title>

  <link rel="stylesheet" href="/css/style.css">
</head>
<body>
  <div id="page-wrapper">
    <h1>Smag0 enregistrement des projets</h1>
    <form id="message-form" action="#" method="post">
      <input id="nomProjet" placeholder="Nom du projet..." required></input>
      <textarea id="message" placeholder="Entrez la description de votre projet... Eviter guillemets, parenthèses... (ce problème sera résolu dès que possible)" required></textarea>
      <button type="submit">Enregistrer</button>
      <button type="button" id="close">Close Connection</button>
    </form>
    <div id="status">Connecting...</div>
    <div id="statusNouveau">Connecting...</div>
    <div id="statusListe">Connecting...</div>

    <ul id="messages"></ul>


  </div>

  <script src="/js/accueil.js"></script>
</body>
</html>

<!--  http://blog.teamtreehouse.com/an-introduction-to-websockets -->