package favedave.smag.projets;

import org.janusproject.kernel.Kernel;
import org.janusproject.kernel.address.AgentAddress;
import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.agent.Kernels;
import org.janusproject.kernel.crio.capacity.CapacityContainer;
import org.janusproject.kernel.status.Status;



public class MethodeAgent extends Agent {
	private String nomMethode;
	private static enum State {
	    //PRESENTATION, WAIT_FOR_WELCOME, 
		CHOIX_METHODE, RECUP_INFOS_METHODE;
	 }
	 private State state;


	 public Status activate(Object... parameters) {
		 System.out.println("Methode Agent démarré");
		 this.state = State.CHOIX_METHODE;

		 
	   return null;
	 }
	 public Status live() {
		 print(this.state);
		switch(this.state) {
		case CHOIX_METHODE:
			//verification dans détails du projet si la methode est spécifiée, sinon
			 if (nomMethode==null){
				 nomMethode="diamond";
			 }
		this.state = State.RECUP_INFOS_METHODE;
		break;
		case RECUP_INFOS_METHODE:
			System.out.println("recup infos de la methode : "+nomMethode);
			   killMe();
		 break;
		   }
		return null;
	 
	 }
	public MethodeAgent() {
		// TODO Auto-generated constructor stub
	}

	public MethodeAgent(Boolean commitSuicide) {
		super(commitSuicide);
		// TODO Auto-generated constructor stub
	}

	public MethodeAgent(CapacityContainer capacityContainer) {
		super(capacityContainer);
		// TODO Auto-generated constructor stub
	}

	public MethodeAgent(AgentAddress address) {
		super(address);
		// TODO Auto-generated constructor stub
	}

	public MethodeAgent(CapacityContainer capacityContainer,
			Boolean commitSuicide) {
		super(capacityContainer, commitSuicide);
		// TODO Auto-generated constructor stub
	}

	public MethodeAgent(AgentAddress address, Boolean commitSuicide) {
		super(address, commitSuicide);
		// TODO Auto-generated constructor stub
	}

	public MethodeAgent(AgentAddress address,
			CapacityContainer capacityContainer) {
		super(address, capacityContainer);
		// TODO Auto-generated constructor stub
	}

	public MethodeAgent(AgentAddress address,
			CapacityContainer capacityContainer, Boolean commitSuicide) {
		super(address, capacityContainer, commitSuicide);
		// TODO Auto-generated constructor stub
	}

}
