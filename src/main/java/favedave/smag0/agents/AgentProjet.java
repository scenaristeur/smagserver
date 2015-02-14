package favedave.smag0.agents;

import java.util.Collections;
import java.util.Set;
import java.util.Timer;

import org.janusproject.kernel.address.Address;
import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.channels.Channel;
import org.janusproject.kernel.status.Status;
import org.json.JSONObject;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

import favedave.smag0.agents.channels.AgentProjetChannel;

public class AgentProjet extends Agent {

	private String id;
	private State state;
	private String messageUtilisateur = "Salut, je suis l'agent qui gère ce projet,"
			+ "Patientez quelques instants, je recherche les informations concernant ce projet";
	Timer timerWaiting;
	boolean projetsSimilaires = false;
	JSONObject projetSimilairesJson = new JSONObject();
	private String projet;

	private static enum State {

		PRESENTATION, INITIALISATION, RECHERCHE_SIMILAIRE, WAITING, RECUP_INFOS, INFOS_RECUPEREES, PROBLEME;
	}

	public Status activate(Object... parameters) {
		id = this.getName();
		// System.out.print("Je suis l'agent qui gère le projet :" + id);
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
			this.state = State.WAITING;
			break;
		case INFOS_RECUPEREES:
			System.out.println("INFOS OK");
			killMe();
			break;
		case WAITING:
			messageUtilisateur = "J'attends de nouvelles instructions";
			// System.out.println("Waiting");
			try {
				Thread.sleep(10000);

				if (projetsSimilaires == true) {
					this.state = State.INFOS_RECUPEREES;
					JSONObject projetSimilairesJson = new JSONObject();
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		case RECHERCHE_SIMILAIRE:
			// System.out.println("Recherche Similaire");
			messageUtilisateur = "Je recherche des projets similaires";
			// projetsSimilaires = "liste des projets similaires";
			rechercheSimilaireParNom();
			this.state = State.WAITING;
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

		@Override
		public JSONObject getProjetsSimilaires(String _projet) {
			projet = _projet;
			if ((state == State.WAITING) && (projetsSimilaires == false)) {
				state = State.RECHERCHE_SIMILAIRE;
			} else {

			}
			// if (projetsSimilaires == true) {
			return projetSimilairesJson;
			/*
			 * } else { return null; }
			 */
		}

	}

	void rechercheSimilaireParNom() {

		String message = "Recherche des projets similaires à " + projet;
		System.out.println(message);
		String queryString = /*
							 * select * where
							 * {<http://smag0.blogspot.fr/ns/smag0#" + projet +
							 * "> ?propriete ?objet} ";
							 */
		"select ?titre ?similaire ?description where {"
				+ "<http://smag0.blogspot.fr/ns/smag0#"
				+ projet
				+ ">"
				+ "<http://purl.org/dc/elements/1.1/title> ?titre . "
				+ "?similaire <http://purl.org/dc/elements/1.1/title> ?titre . "
				+ "?similaire <http://purl.org/dc/elements/1.1/description> ?description . "
				+ "} ";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(
				"http://fuseki-smag0.rhcloud.com/ds/query", query);
		ResultSet results = qexec.execSelect();
		int i = 0;
		for (; results.hasNext();) {
			i++;
			QuerySolution soln = results.nextSolution();
			RDFNode titre = soln.get("titre");
			RDFNode similaire = soln.get("similaire");
			RDFNode description = soln.get("description");
			String descriptionResultat = null;
			if (description.isResource()) {
				descriptionResultat = description.asResource().toString();
			} else if (description.isLiteral()) {
				descriptionResultat = description.asLiteral().toString();
			}

			JSONObject jresult = new JSONObject();
			jresult.put("titre", titre);
			jresult.put("similaire", similaire);
			jresult.put("description", descriptionResultat);
			projetSimilairesJson.put(String.valueOf(i), jresult);
		}
		System.out.println(projetSimilairesJson.toString());
		projetsSimilaires = true;

	}

}
