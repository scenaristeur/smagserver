package favedave.smag.projets;

import java.net.URI;

import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.status.Status;





public class DoapAgent extends Agent {
	private String doapUri="http://usefulinc.com/ns/doap#";
	
	private static enum State {
	    //PRESENTATION, WAIT_FOR_WELCOME, 
		TEST_DOAP_URI,RECUPERATION_DOAP_SCHEMA, RECUPERATION_DOAP_PROJET;
	 }
	 private State state;


	 public Status activate(Object... parameters) {
System.out.println("Doap Agent démarré");
		 this.state = State.TEST_DOAP_URI;

	   return null;
	 }
	 public Status live() {
		 print(this.state);
		switch(this.state) {
		case TEST_DOAP_URI:
			//verification dans détails du projet si la methode est spécifiée, sinon

		this.state = State.RECUPERATION_DOAP_SCHEMA;
		break;
		case RECUPERATION_DOAP_SCHEMA:
			System.out.println("recup infos de la methode : "+doapUri);
			   killMe();
		 break;
		   }
		return null;
	 
	 }
}
