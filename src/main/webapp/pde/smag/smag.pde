ArrayList <Noeud> noeuds= new ArrayList();
String nomProjet;
String idProjet;
ArrayList points;


String texte="recherche en cours";
void setup() {
  size(500, 500);
  points = new ArrayList();
  for (int i=0; i<10; i++) {
    Noeud noeud= new Noeud(); 
    noeuds.add(noeud);
  }
  //  noLoop();            // turn off animation, since we won't need it
  stroke(#FFEE88);
  fill(#FFEE88);
  background(#000033);
  text("", 0, 0);          // force Processing to load a font
  textSize(18);
}

void draw () {
  background(#330033);
  for (int i=0; i<noeuds.size (); i++) {
    Noeud noeud=noeuds.get(i);
    noeud.display();
  }
/*
  for (int p=0, end=points.size (); p<end; p++) {
    Point pt = (Point) points.get(p);
    if (p<end-1) {
      Point next = (Point) points.get(p+1);
      line(pt.x, pt.y, next.x, next.y);
    }
    pt.draw();
  }*/
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
  text(texte, (width - twidth)/2, height/2+20);
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
  text(texte, (width - twidth)/2, height/2-20);
}
void setNomProjetPde(String nomProjet) {
  texte=nomProjet;
  println("nom du projet :"+texte);
}
void setIDProjetPde(String _idProjet) {
  idProjet=_idProjet;
  println("id du projet :"+idProjet);
  Noeud noeud= new Noeud(idProjet, width/2, height/2+40); 
  noeuds.add(noeud);
}
/*void drawMethode(String _id, String _methodeJson) {
 String methodeJSON=_methodeJson;
 println("ajout de la méthode"+methodeJSON);
 Noeud noeud= new Noeud("methode"+methodeJSON, width/2, height/2-40); 
 noeuds.add(noeud);
 // noeud.addJson(_methodeJson);
 }*/
 /*
void mouseClicked() {
  addPoint(mouseX, mouseY);
}

Point addPoint(int x, int y) {
  Point pt = new Point(x, y);
  points.add(pt);
  return pt;
}*/
Noeud addNoeud(String _id, float x, float y) {
  Noeud noeud= new Noeud(_id, x, y); 
  noeuds.add(noeud);
  return noeud;
}
/*
class Point {
  int x, y;
  Point(int x, int y) { 
    this.x=x; 
    this.y=y;
  }
  void draw() {
    stroke(255, 0, 0);
    fill(255);
    ellipse(x, y, 10, 10);
  }
}
*/
