class Projet{
  PVector position=new PVector(random(10, width-10), random(10, height-10));
  String id=null;
  String titre=null;
  
 Projet(){
 } 
  Projet(String _id){
   id=_id;
 }
   Projet(String _id, String _titre){
   id=_id;
   titre=_titre;

 }
 Projet(String _id,float _x,float _y){
   id=_id;
   position.x=_x;
   position.y=_y;
 }
 
 void display(){
  ellipse(position.x, position.y, 10, 10);
  if (titre!=null){
    float twidth = textWidth(id);
  text(titre, position.x - twidth/2, position.y);
 // println(id);
  }else  if (id!=null){
    float twidth = textWidth(id);
  text(id, position.x - twidth/2, position.y);
 // println(id);
  }
 }
}
