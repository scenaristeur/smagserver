class Noeud{
  PVector position=new PVector(random(10, width-10), random(10, height-10));
 Noeud(){
 } 
 
 void display(){
  ellipse(position.x, position.y, 10, 10); 
 }
}
