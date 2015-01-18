/**
 * 
 */
package favedave.smag.projets;

import org.janusproject.kernel.address.AgentAddress;
import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.crio.capacity.CapacityContainer;
import org.janusproject.kernel.status.Status;


/**
 * @author David
 *
 */
public class DetailsAgent extends Agent {
	private static enum State {
	    //PRESENTATION, WAIT_FOR_WELCOME, 
		RECUP_TITRE_DESCRIPTION, RECHERCHE_PROJET_SIMILAIRE, AFFICHE_DETAIL;
	 }
	 private State state;


	 public Status activate(Object... parameters) {
System.out.println("Details Agent démarré");
		 this.state = State.RECUP_TITRE_DESCRIPTION;

	   return null;
	 }
	 public Status live() {
		return null;
	 
	 }
	
	
	/**
	 * 
	 */
	public DetailsAgent() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param commitSuicide
	 */
	public DetailsAgent(Boolean commitSuicide) {
		super(commitSuicide);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param capacityContainer
	 */
	public DetailsAgent(CapacityContainer capacityContainer) {
		super(capacityContainer);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param address
	 */
	public DetailsAgent(AgentAddress address) {
		super(address);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param capacityContainer
	 * @param commitSuicide
	 */
	public DetailsAgent(CapacityContainer capacityContainer,
			Boolean commitSuicide) {
		super(capacityContainer, commitSuicide);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param address
	 * @param commitSuicide
	 */
	public DetailsAgent(AgentAddress address, Boolean commitSuicide) {
		super(address, commitSuicide);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param address
	 * @param capacityContainer
	 */
	public DetailsAgent(AgentAddress address,
			CapacityContainer capacityContainer) {
		super(address, capacityContainer);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param address
	 * @param capacityContainer
	 * @param commitSuicide
	 */
	public DetailsAgent(AgentAddress address,
			CapacityContainer capacityContainer, Boolean commitSuicide) {
		super(address, capacityContainer, commitSuicide);
		// TODO Auto-generated constructor stub
	}

}
