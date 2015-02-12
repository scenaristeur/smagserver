package favedave.smag0.agents;

import java.util.Collections;
import java.util.Set;

import org.janusproject.kernel.address.Address;
import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.channels.Channel;
import org.janusproject.kernel.status.Status;

import favedave.smag0.agents.channels.AgentProjetChannel;

public class AgentProjet extends Agent {

	private String id;
	private State state;
	private String messageUtilisateur = "Salut, je suis l'agent qui gère ce projet";

	private static enum State {

		PRESENTATION, INITIALISATION, RECUP_INFOS, INFOS_RECUPEREES, PROBLEME;
	}

	public Status activate(Object... parameters) {
		id = this.getName();
		System.out.print("Je suis l'agent qui gère le projet :" + id);
		this.state = State.PRESENTATION;

		return null;
	}

	public Status live() {
		print(this.state);
		switch (this.state) {
		case PRESENTATION:
			System.out.println(messageUtilisateur);
			this.state = State.INITIALISATION;
			break;
		case INITIALISATION:
			messageUtilisateur = "je lance l'initialisation";
			System.out.println(messageUtilisateur);
			this.state = State.INFOS_RECUPEREES;
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
		public String getMessageUtilisateur() {
			// TODO Auto-generated method stub
			return messageUtilisateur;
		}

	}
}
