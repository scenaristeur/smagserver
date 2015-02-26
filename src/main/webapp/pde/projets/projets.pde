ArrayList<Projet> projets=new ArrayList();

void setup(){
size(600, 400, OPENGL);
//noStroke();
background(50);
  stroke(#FFEE88);
  fill(#88EE88);
  background(#000033);
  text("", 0, 0);          // force Processing to load a font
  textSize(18);
lights();
translate(width/2+30, height/2, 0);
rotateX(-PI/6);
rotateY(PI/3 + 210/float(height) * PI);
box(45);
translate(0, 0, -50);
box(30);

}

void draw(){
    for (int i=0; i<projets.size (); i++) {
    Projet projet=projets.get(i);
    projet.display();
    }
}

Projet addProjet(String _id) {
  Projet projet= new Projet(_id); 
  projets.add(projet);
  return projet;
}
