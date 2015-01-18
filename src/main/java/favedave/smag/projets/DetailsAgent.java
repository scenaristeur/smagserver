/**
 * 
 */
package favedave.smag.projets;

import java.util.Collections;
import java.util.Set;

import org.janusproject.kernel.address.Address;
import org.janusproject.kernel.address.AgentAddress;
import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.channels.Channel;
import org.janusproject.kernel.crio.capacity.CapacityContainer;
import org.janusproject.kernel.message.StringMessage;
import org.janusproject.kernel.status.Status;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.query.ResultSetRewindable;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;




/**
 * @author David
 *
 */
public class DetailsAgent extends Agent {
	private static enum State {
	    //PRESENTATION, WAIT_FOR_WELCOME, 
		CONSTRUCTION_REQUETE, EXECUTION_REQUETE, CONSTRUCTION_RESULTAT, RESULTAT_PRET,RESULTAT_TRANSMIS;
	 }
	 private State state;
	private String idProjet;
	private Query query;
	private ResultSetRewindable resultats;
	private String resultat;



	 public Status activate(Object... parameters) {
		idProjet= (String) parameters[0];
System.out.println("Details Agent démarré pour le projet : "+idProjet);
		 this.state = State.CONSTRUCTION_REQUETE;

	   return null;
	 }
	 public Status live() {
		 print(this.state);
		   switch(this.state) {
		   case CONSTRUCTION_REQUETE:
			 	System.out.println("Je construit la requete ");
			 /*	String queryString = "select ?* where {"+
			 	 		"?projet <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://smag0.blogspot.fr/ns/smag0#Projet> ."+
			 	 		"?projet <http://purl.org/dc/elements/1.1/title> ?titre ."+
			 	 		"?projet <http://purl.org/dc/elements/1.1/description> ?description ."+
			 	 		"}"+
			 	 		"ORDER BY DESC(?projet)"+
			 	 		"LIMIT 100";*/
			 	String queryString = "select * where {<http://smag0.blogspot.fr/ns/smag0#"+idProjet+"> ?propriete ?objet} ";
			 	query = QueryFactory.create(queryString);
			 	System.out.println(queryString+" "+query);
			 	this.state = State.EXECUTION_REQUETE;
			   break;
		   case EXECUTION_REQUETE:
			   QueryExecution qexec = QueryExecutionFactory.sparqlService("http://fuseki-smag0.rhcloud.com/ds/query", query);
			     try {
			          ResultSet results = qexec.execSelect();
			          resultats=ResultSetFactory.copyResults(results);
			         // resultatsJson=ResultSetFactory.copyResults(results);	   
			     }
			     finally {
			        qexec.close();
			     }
			     this.state = State.CONSTRUCTION_RESULTAT;
			     break;
			   case CONSTRUCTION_RESULTAT:   
				   System.out.println("Construction Resultat");
			        resultat="<table border=\"1\">\n<tr><th>Propriété</th><th>Objet</th></tr>\n";
			        for ( ; resultats.hasNext() ; )
			        {
			          QuerySolution soln = resultats.nextSolution() ;
			         // RDFNode x = soln.get("varName") ;       // Get a result variable by name.
			        // Resource projet = soln.getResource("projet") ; // Get a result variable - must be a resource
			         // propriete = soln.getProperty() ;
			         // Literal objet = soln.getLiteral("objet") ;   // Get a result variable - must be a literal
			      //    RDFNode s=result.get("s");
			          RDFNode p=soln.get("propriete");
			          RDFNode o=soln.get("objet");
			          
			          resultat=resultat+"<tr><td>"+p+"</a></td><td>"+o+"</td></tr>\n";
			        }
			        resultat=resultat+"</table>";
			 	   this.state = State.RESULTAT_PRET;
				   break;
				   
			   case RESULTAT_PRET:	 
				   print("envoi de l'info du resultat prêt");
				   System.out.println(resultat);
				   
				   break;
			   case RESULTAT_TRANSMIS:
				   killMe();
				   break;
		   }
		return null;
	 
	 }


	 public Set<? extends Class<? extends Channel>> getSupportedChannels() {
			return Collections.singleton(AgentProjetChannel.class);
		}
		public <C extends Channel> C getChannel(Class<C> channelClass,
				Object... params) {
			  // Check if the given channel type is supported by the agent.
		    if (AgentDetailProjetChannel.class.isAssignableFrom(channelClass)) {
		 
		      // Create the instance of the channel.
		      AgentDetailProjetChannel channelInstance = new StateChannelImplementation();
		 
		      // Reply the channel instance.
		      return channelClass.cast(channelInstance);
		 
		    }
		 
		    // The given channel type is not supported
		    throw new IllegalArgumentException("channelClass");
		}
		 /** This inner class is the implementation of the channel
		   *  for this agent implementation.
		   */
		  private class StateChannelImplementation implements AgentDetailProjetChannel {
		 

		    public String getEtat() {
			      return state.toString();
			    }

			@Override
			public Address getChannelOwner() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getDetails() {
				// TODO Auto-generated method stub
				if (resultat!=null){
					state = State.RESULTAT_TRANSMIS;
					System.out.println("transmission :"+resultat);
				}
				return resultat;
			}

		  }
	

}
