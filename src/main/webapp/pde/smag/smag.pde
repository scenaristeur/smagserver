ArrayList <Noeud> noeuds= new ArrayList();
String nomProjet;
String idProjet;

String texte="recherche en cours";
void setup() {
  size(500, 500);
  for (int i=0; i<10; i++) {
    Noeud noeud= new Noeud(); 
    noeuds.add(noeud);
  }
//  noLoop();            // turn off animation, since we won't need it
  stroke(#FFEE88);
  fill(#FFEE88);
  background(#000033);
  text("", 0, 0);          // force Processing to load a font
  textSize(24);
}

void draw () {
  background(#330033);
   for (int i=0; i<noeuds.size();i++){
   Noeud noeud=noeuds.get(i);
   noeud.display(); 
   }
   drawText(texte);
/*
  float twidth = textWidth(texte);
  text(texte, (width - twidth)/2, height/2);*/
}
void drawText(String t)
{
  //background(#000033);
  // get the width for the text
  float twidth = textWidth(t);
  // place the text centered on the drawing area
  text(t, (width - twidth)/2, height/2);
}
void drawDetails(JSONObject detailJson) {
  println(detailJson);
  //ackground(#000033);
  texte=detailJson.toString();
  float twidth = textWidth(texte);
  // place the text centered on the drawing area
  text(texte, (width - twidth)/2, height/2);
}
void setProjetPde(JSONObject detailJson) {
 /* for (int i=0; i<detailJson.size (); i++) {
    JSONArray test=detailJson.getJSONArray(str(i) );
    println(test);
  }*/
  println(detailJson.toString());

  texte=detailJson.toString();
  float twidth = textWidth(texte);
  // place the text centered on the drawing area
  text(texte, (width - twidth)/2, height/2);
}
void setNomProjetPde(String nomProjet){
  texte=nomProjet;
  println("nom du projet :"+texte);

}
void setIDProjetPde(String _idProjet){
    idProjet=_idProjet;
  println("id du projet :"+idProjet);
      Noeud noeud= new Noeud(idProjet,width/2,height/2); 
    noeuds.add(noeud);
}
