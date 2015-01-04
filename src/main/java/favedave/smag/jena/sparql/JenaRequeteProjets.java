package favedave.smag.jena.sparql;

import org.janusproject.kernel.address.AgentAddress;
import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.message.Message;
import org.janusproject.kernel.message.StringMessage;
import org.janusproject.kernel.status.Status;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Model;

public class JenaRequeteProjets extends Agent{
private String resultat;

String SOURCE;
String NS;
//create a model using reasoner
OntModel model1;
//create a model which doesn't use a reasoner
OntModel model2;

private static enum State {
    //PRESENTATION, WAIT_FOR_WELCOME, 
	CONSTRUCTION_REQUETE, EXECUTION_REQUETE, CONSTRUCTION_RESULTAT, RESULTAT_PRET,RESULTAT_TRANSMIS;
 }
 private State state;
 public Status activate(Object... parameters) {
   this.state = State.CONSTRUCTION_REQUETE;
   print ("Je construit la requete 1");
	System.out.println("Je construit la requete 2");
	SOURCE = "http://www.opentox.org/api/1.1";
	NS = SOURCE + "#";
	model1 = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_MICRO_RULE_INF);
	model2 = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM);
	// Create a new query
	String queryString =        
	"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "+
	"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>  "+
	"select ?uri "+
	"where { "+
	 "?uri rdfs:subClassOf <http://www.opentox.org/api/1.1#Feature>  "+
	"} \n ";
	Query query = QueryFactory.create(queryString);
	
	// Execute the query and obtain results
	QueryExecution qe = QueryExecutionFactory.create(query, model1);
	com.hp.hpl.jena.query.ResultSet results =  qe.execSelect();

	// Output query results    
	ResultSetFormatter.out(System.out, results, query);
	// read the RDF/XML file
	model1.read( SOURCE, "RDF/XML" );
	model2.read( SOURCE, "RDF/XML" );
	//prints out the RDF/XML structure
	qe.close();
	System.out.println(" ");
	
	
   return null;
 }
 public Status live() {
	 print(this.state);
   switch(this.state) {
   case CONSTRUCTION_REQUETE:
     broadcastMessage(new StringMessage("hello JENAREQUETEPROJET construit la requete") );
     print("j'envoie un message JENAREQUETEPROJET construit la requete");
     this.state = State.EXECUTION_REQUETE;
     break;
   case EXECUTION_REQUETE:

	   this.state = State.EXECUTION_REQUETE;
     break;
   case CONSTRUCTION_RESULTAT:
	   
	   this.state = State.RESULTAT_PRET;
	   break;
	   
   case RESULTAT_PRET:
	   
	   this.resultat="voilà";
	   break;
	   
   case RESULTAT_TRANSMIS:
	     broadcastMessage(new StringMessage("JENAREQUETEPROJET resultat transmis, je me kill") );
	     print("JENAREQUETEPROJET resultat transmis, je me kill");
	   killMe();
	   break;
   }
   return null;
 }
	public String getResultat() {
		// TODO Auto-generated method stub
		this.state = State.RESULTAT_TRANSMIS;
		return this.resultat;
	}

}
/*
String SOURCE = "http://www.opentox.org/api/1.1";
String NS = SOURCE + "#";
//create a model using reasoner
OntModel model1 = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_MICRO_RULE_INF);
//create a model which doesn't use a reasoner
OntModel model2 = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM);

// read the RDF/XML file
model1.read( SOURCE, "RDF/XML" );
model2.read( SOURCE, "RDF/XML" );
//prints out the RDF/XML structure
qe.close();
System.out.println(" ");


// Create a new query
String queryString =        
"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "+
"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>  "+
"select ?uri "+
"where { "+
 "?uri rdfs:subClassOf <http://www.opentox.org/api/1.1#Feature>  "+
"} \n ";
Query query = QueryFactory.create(queryString);

System.out.println("----------------------");

System.out.println("Query Result Sheet");

System.out.println("----------------------");

System.out.println("Direct&Indirect Descendants (model1)");

System.out.println("-------------------");


// Execute the query and obtain results
QueryExecution qe = QueryExecutionFactory.create(query, model1);
com.hp.hpl.jena.query.ResultSet results =  qe.execSelect();

// Output query results    
ResultSetFormatter.out(System.out, results, query);

qe.close();

System.out.println("----------------------");
System.out.println("Only Direct Descendants");
System.out.println("----------------------");

// Execute the query and obtain results
qe = QueryExecutionFactory.create(query, model2);
results =  qe.execSelect();

// Output query results    
ResultSetFormatter.out(System.out, results, query);  
qe.close();
*/