package favedave.smag.jena.sparql;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.apache.jena.atlas.json.JSON;
import org.apache.jena.atlas.json.JsonObject;
import org.janusproject.kernel.address.Address;
import org.janusproject.kernel.address.AgentAddress;
import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.channels.Channel;
import org.janusproject.kernel.channels.ChannelInteractable;
import org.janusproject.kernel.message.Message;
import org.janusproject.kernel.message.StringMessage;
import org.janusproject.kernel.status.Status;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.query.ResultSetRewindable;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.query.ResultSet;

// JENA ne semble pas fonctionner, à cause de java 8 ?*/
public class Copy_2_of_JenaRequeteProjets extends Agent implements ChannelInteractable{
private String resultat;
/** First attribute in the state of the agent.
 */
private int firstAttribute = 1;
private String etat="not Actif";
private ResultSet resultats;
private ResultSetRewindable resultatJson;


/** Second attribute in the state of the agent.
 */
private final Collection<Object> secondAttribute = new ArrayList<Object>();

private String SOURCE;
private String NS;
private Query query;
//create a model using reasoner
//OntModel model1;
//create a model which doesn't use a reasoner
//OntModel model2;
// 

private static enum State {
    //PRESENTATION, WAIT_FOR_WELCOME, 
	CONSTRUCTION_REQUETE, EXECUTION_REQUETE, CONSTRUCTION_RESULTAT, RESULTAT_PRET,RESULTAT_TRANSMIS;
 }
 private State state;

 public Status activate(Object... parameters) {
   this.state = State.CONSTRUCTION_REQUETE;

   return null;
 }
 public Status live() {
	 firstAttribute++;
	 print(this.state);
   switch(this.state) {
   case CONSTRUCTION_REQUETE:
     broadcastMessage(new StringMessage("hello JENAREQUETEPROJET construit la requete") );
     print("j'envoie un message JENAREQUETEPROJET construit la requete");
 	System.out.println("Je construit la requete ");
 	//SOURCE = "http://www.opentox.org/api/1.1";
 	SOURCE = "http://fuseki-smag0.rhcloud.com/ds";
 	NS = SOURCE + "#";
// 	model1 = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_MICRO_RULE_INF);
// 	model2 = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM);

 	// Create a new query
 /*	String queryString =        
 	"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "+
 	"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>  "+
 	"select ?uri "+
 	"where { "+
 	 "?uri rdfs:subClassOf <http://www.opentox.org/api/1.1#Feature>  "+
 	"} \n ";*/
 	/*select ?projet ?titre ?description where {
 ?projet <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://smag0.blogspot.fr/ns/smag0#Projet> .
 ?projet <http://purl.org/dc/elements/1.1/title> ?titre .
 ?projet <http://purl.org/dc/elements/1.1/description> ?description .
 }
 ORDER BY DESC(?projet)
 LIMIT 100*/
 	
 	String queryString = "select ?projet ?titre ?description where {"+
 		"?projet <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://smag0.blogspot.fr/ns/smag0#Projet> ."+
 		"?projet <http://purl.org/dc/elements/1.1/title> ?titre ."+
 		"?projet <http://purl.org/dc/elements/1.1/description> ?description ."+
 		"}"+
 		"ORDER BY DESC(?projet)"+
 		"LIMIT 100";
 			
 			/*?projet <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://smag0.blogspot.fr/ns/smag0#Projet> }";
 	/* query = QueryFactory.create(queryString);
     */
    /* String queryString=
     		 
             "PREFIX owl: <http://www.w3.org/2002/07/owl#>"+
                 "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"+
                 "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
                 "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
                 "PREFIX foaf: <http://xmlns.com/foaf/0.1/>"+
                 "PREFIX dc: <http://purl.org/dc/elements/1.1/>"+
                 "PREFIX : <http://dbpedia.org/resource/>"+
                 "PREFIX dbpedia2: <http://dbpedia.org/property/>"+
                 "PREFIX dbpedia: <http://dbpedia.org/>"+
                 "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>"+
                 "PREFIX dbpedia-owl: <http://dbpedia.org/ontology/>"+
                 "SELECT DISTINCT ?syn"+
                         "WHERE {"+
                           "{"+
                            "?disPage dbpedia-owl:wikiPageDisambiguates <http://dbpedia.org/resource/Apple> ."+
                            "?disPage dbpedia-owl:wikiPageDisambiguates ?syn ."+
                           "}"+
                           "UNION"+
                           "{"+
                            "<http://dbpedia.org/resource/Apple> dbpedia-owl:wikiPageDisambiguates ?syn ."+
                           "}"+
                         "}";*/
  query = QueryFactory.create(queryString);
 	
      //QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/snorql", query);



     this.state = State.EXECUTION_REQUETE;
     break;
   case EXECUTION_REQUETE:
		// Execute the query and obtain results
		//QueryExecution qe = QueryExecutionFactory.create(query, model1);
		//ResultSet results =  qe.execSelect();
	   QueryExecution qexec = QueryExecutionFactory.sparqlService("http://fuseki-smag0.rhcloud.com/ds/query", query);
	     try {
	          ResultSet results = qexec.execSelect();
	          resultats=ResultSetFactory.copyResults(results);
	          resultatJson=ResultSetFactory.copyResults(results);
			//  ResultSetFormatter.out(System.out, results, query);
	        /* while ( results.hasNext()) {
QuerySolution result = results.next();
resultat=resultat+" <h3>"+result.toString()+"</h3> ";
	         System.out.println(result);
	         }*/
	       /*  ByteArrayOutputStream b = new ByteArrayOutputStream();
	         ResultSetFormatter.outputAsJSON(b, results);
*/
	       // this.resultat = b.toString();
	     }
	     finally {
	        qexec.close();
	     }
		// Output query results    
		//ResultSetFormatter.out(System.out, results, query);
		// read the RDF/XML file
		//model1.read( SOURCE, "RDF/XML" );
		//model2.read( SOURCE, "RDF/XML" );
		//prints out the RDF/XML structure
		//model1.write(System.out);
		//model2.write(System.out);
		
		
		//qe.close();

	   this.state = State.CONSTRUCTION_RESULTAT;
     break;
   case CONSTRUCTION_RESULTAT:
		System.out.println("Construction Resultat");
        resultat="<table border=\"1\">\n<tr><th>PROJET</th><th>DESCRIPTION</th></tr>\n";
        for ( ; resultats.hasNext() ; )
        {
          QuerySolution soln = resultats.nextSolution() ;
         // RDFNode x = soln.get("varName") ;       // Get a result variable by name.
          Resource projet = soln.getResource("projet") ; // Get a result variable - must be a resource
          Literal titre = soln.getLiteral("titre") ;
          Literal description = soln.getLiteral("description") ;   // Get a result variable - must be a literal
      resultat=resultat+"<tr><td><a href=\"projet.jsp?projet="+projet.getLocalName()+"\">"+titre+"</a></td><td>"+description+"</td></tr>\n";
        }
        resultat=resultat+"</table>";
        
        // au format JSON
	/*         ByteArrayOutputStream b = new ByteArrayOutputStream();
        ResultSetFormatter.outputAsJSON(b, resultatJson);

       this.resultat = b.toString();
        */
        
	   this.state = State.RESULTAT_PRET;
	   break;
	   
   case RESULTAT_PRET:
	   
	  // this.resultat="voilà";
	   print("envoi de l'info du resultat prêt");
	   //simule ma récupération du résultat par un autre agent
	  // getResultat();
	   this.state = State.RESULTAT_TRANSMIS;
	   break;
	   
   case RESULTAT_TRANSMIS:
	     broadcastMessage(new StringMessage("JENAREQUETEPROJET resultat transmis, je me kill") );
	     print("JENAREQUETEPROJET resultat transmis, je me kill");
	   killMe();
	   break;
   }
   return null;
 }
/*	public String getResultat() {
		// TODO Auto-generated method stub
		this.state = State.RESULTAT_TRANSMIS;
		return this.resultat;
	}*/
	@Override
	public Set<? extends Class<? extends Channel>> getSupportedChannels() {
		return Collections.singleton(AgentStateChannel.class);
	}
	@Override
	public <C extends Channel> C getChannel(Class<C> channelClass,
			Object... params) {
		  // Check if the given channel type is supported by the agent.
	    if (AgentStateChannel.class.isAssignableFrom(channelClass)) {
	 
	      // Create the instance of the channel.
	      AgentStateChannel channelInstance = new StateChannelImplementation();
	 
	      // Reply the channel instance.
	      return channelClass.cast(channelInstance);
	 
	    }
	 
	    // The given channel type is not supported
	    throw new IllegalArgumentException("channelClass");
	}
	 /** This inner class is the implementation of the channel
	   *  for this agent implementation.
	   */
	  private class StateChannelImplementation implements AgentStateChannel {
	 
	    public int getFirstAttribute() {
	      return Copy_2_of_JenaRequeteProjets.this.firstAttribute;
	    }
	    public String getEtat() {
		      return Copy_2_of_JenaRequeteProjets.this.state.toString();
		    }
	 
	    public Collection<Object> getSecondAttribute() {
	      // You must reply an object that is read-only to
	      // ensure that the agent's attribute cannot be changed
	      // from the outside of the agent.
	      return Collections.unmodifiableCollection(Copy_2_of_JenaRequeteProjets.this.secondAttribute);
	    }

		@Override
		public Address getChannelOwner() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public String getResultat() {
			
			return resultat;
			
		}
		@Override
		public ByteArrayOutputStream getResultatJson() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public JSON getProjetDetails() {
			// TODO Auto-generated method stub
			return null;
		}
	 
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