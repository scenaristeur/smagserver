package favedave.smag.projets;

import java.util.Collections;
import java.util.Set;

import org.janusproject.kernel.Kernel;
import org.janusproject.kernel.address.Address;
import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.agent.ChannelManager;
import org.janusproject.kernel.agent.Kernels;
import org.janusproject.kernel.channels.Channel;
import org.janusproject.kernel.status.Status;

public class ProjetAgent extends Agent {

	private static enum State {
		// PRESENTATION, WAIT_FOR_WELCOME,
		INITIALISATION, RECUP_INFOS, INFOS_RECUPEREES, PROBLEME;
	}

	private State state;
	private String projetDetail;
	private String projetMethode;
	private String projetDoap;
	private int infos_recuperees;
	private String etat;
	private String id;
	private AgentDetailProjetChannel channelDetails;

	public Status activate(Object... parameters) {
		id = this.getName();
		System.out.print("ID du projet :" + id);
		this.state = State.INITIALISATION;

		return null;
	}

	public Status live() {
		print(this.state);
		switch (this.state) {
		case INITIALISATION:
			Kernel k = Kernels.get();
			DetailsAgent detailsAgent = new DetailsAgent();
			MethodeAgent methodeAgent = new MethodeAgent();
			DoapAgent doapAgent = new DoapAgent();
			k.launchLightAgent(detailsAgent, "detailsAgent", id);
			ChannelManager channelManager = k.getChannelManager();
			channelDetails = detailsAgent
					.getChannel(AgentDetailProjetChannel.class);
			k.launchLightAgent(methodeAgent, "methodeAgent");
			k.launchLightAgent(doapAgent, "doapAgent");
			infos_recuperees = 0;
			this.state = State.RECUP_INFOS;
			break;
		case RECUP_INFOS:
			System.out.println("recup infos");

			if (projetDetail == null) {
				System.out.println("recuperation des details du projet");

				if (channelDetails != null) {
					projetDetail = channelDetails.getDetails();
					// projetDetailJson = channelDetails.getJson();
					System.out.println("Details " + projetDetail);
				} else {
					System.err.println("The agent does not accept to interact");
				}

			} else {
				infos_recuperees++;
			}
			if (projetMethode == null) {
				System.out
						.println("simulation de la recuperation de la methode du projet");
				projetMethode = "Recuperation de la méthode du projet";
				// JenaRequete jenaRequete = new JenaRequete();
				infos_recuperees++;
			}
			if (projetDoap == null) {
				System.out
						.println("simulation de la recuperation du DOAP du projet");
				projetDoap = "Recuperation du doap";
				infos_recuperees++;
			}
			if (infos_recuperees == 3) {
				this.state = State.INFOS_RECUPEREES;
			} else {
				// this.state = State.PROBLEME;
				System.out.println(state + " " + infos_recuperees);

			}
			break;
		case INFOS_RECUPEREES:

			System.out.println("INFOS OK");
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
		if (AgentProjetChannel.class.isAssignableFrom(channelClass)) {

			// Create the instance of the channel.
			AgentProjetChannel channelInstance = new StateChannelImplementation();

			// Reply the channel instance.
			return channelClass.cast(channelInstance);

		}

		// The given channel type is not supported
		throw new IllegalArgumentException("channelClass");
	}

	/**
	 * This inner class is the implementation of the channel for this agent
	 * implementation.
	 */
	private class StateChannelImplementation implements AgentProjetChannel {

		public String getEtat() {
			return state.toString();
		}

		@Override
		public Address getChannelOwner() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getProjetDetail() {
			// TODO Auto-generated method stub
			return projetDetail;
		}

		@Override
		public String getProjetMethode() {
			// TODO Auto-generated method stub
			return projetMethode;
		}

		@Override
		public String getProjetDoap() {
			// TODO Auto-generated method stub
			return projetDoap;
		}

	}
}
