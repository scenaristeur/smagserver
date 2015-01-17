ArrayList <Noeud> noeuds= new ArrayList();

String texte="vide";
void setup(){
  size(800,600);
  for (int i=0; i<10;i++){
   Noeud noeud= new Noeud(); 
   noeuds.add(noeud);
  }
  noLoop();            // turn off animation, since we won't need it
    stroke(#FFEE88);
    fill(#FFEE88);
   background(#000033);
   text("",0,0);          // force Processing to load a font
   textSize(24);
}

void draw (){
 /* background(0);
  text("Affichage des projets sous forme de graphe interactif",100,100);
  text("---> chargement des données",100,200);
  for (int i=0; i<noeuds.size();i++){
   Noeud noeud=noeuds.get(i);
    noeud.display(); 
  }
  drawText(texte);
*/
}
void drawText(String t)
   {
     background(#000033);
     // get the width for the text
     float twidth = textWidth(t);
     // place the text centered on the drawing area
     text(t, (width - twidth)/2, height/2);
   }
