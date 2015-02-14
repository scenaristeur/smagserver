package favedave.smag0;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.janusproject.kernel.Kernel;
import org.janusproject.kernel.agent.ChannelManager;
import org.janusproject.kernel.agent.Kernels;
import org.json.JSONObject;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

import favedave.smag0.agents.AgentProjet;
import favedave.smag0.agents.channels.AgentProjetChannel;

public class PageProjetSocket extends WebSocketServlet {
	private JSONObject projetJson = new JSONObject();
	private JSONObject projetJsonPrecedent = new JSONObject();
	private JSONObject projetsSimilaires = new JSONObject();
	private JSONObject projetsSimilairesPrecedent = new JSONObject();
	private String etape;
	private AgentProjetChannel channel;
	String projet;

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
		Enumeration<String> parameterNames = request.getParameterNames();

		while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			System.out.println(paramName);
			// System.out.println("\n");
			String[] paramValues = request.getParameterValues(paramName);
			for (int i = 0; i < paramValues.length; i++) {
				String paramValue = paramValues[i];
				System.out.println("\t" + paramValue);
				// System.out.println("\n");
			}
		}
		projetsSimilairesPrecedent.put("precedent", "vide");
		etape = request.getParameter("etape");
		projet = request.getParameter("projet");
		System.out.println(etape);
		return new PageProjet(projet);
	}

	public class PageProjet implements WebSocket.OnTextMessage {
		private Connection connection;
		private Timer timer;
		String messageUtilisateur = "L'agent qui gère se projet se met en route, merci de patienter";
		String messageUtilisateurPrecedent = new String();
		private String projetID;

		public PageProjet(String _projet) {
			projetID = _projet;
		}

		@Override
		public void onOpen(Connection connection) {
			// TODO Auto-generated method stub
			this.connection = connection;
			this.timer = new Timer();
			System.out.println("connexion page projet " + projetID);
			lanceAgentProjet(projetID);
			recupereDoapProjet(projetID);

		}

		private void lanceAgentProjet(String _projetID) {
			String _agentProjetID = projetID;
			Kernel k = Kernels.get();
			AgentProjet agentProjet = new AgentProjet();
			k.launchLightAgent(agentProjet, _agentProjetID);
			ChannelManager channelManager = k.getChannelManager();
			channel = agentProjet.getChannel(AgentProjetChannel.class);
			if (connection == null || !connection.isOpen()) {
				System.out.println("Connection is closed!!");
				return;
			}
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					if (channel != null) {
						// Display the agent's state
						// System.out.println("Etat " + channel.getEtat());
						messageUtilisateur = channel.getMessageUtilisateur();
						projetsSimilaires = channel
								.getProjetsSimilaires(_agentProjetID);

						if (!messageUtilisateur
								.equals(messageUtilisateurPrecedent)) {
							try {
								projetJson.put("messageUtilisateur",
										messageUtilisateur);
								System.out.println("message utilisateur : "
										+ messageUtilisateur);
								connection.sendMessage(projetJson.toString());
								messageUtilisateurPrecedent = messageUtilisateur;
								System.out
										.println("nouveau message utilisateur");
								System.out.println("Connection send "
										+ projetJson.toString());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if (projetsSimilaires != null)
						// && (!projetsSimilaires
						// .equals(projetsSimilairesPrecedent)))
						{
							try {
								projetJson.put("projetsSimilaires",
										projetsSimilaires);
								System.out.println("projetsSimilaires : "
										+ projetsSimilaires);
								connection.sendMessage(projetJson.toString());
								projetsSimilairesPrecedent = projetsSimilaires;
								System.out
										.println("VIRER le connection SEND , si la liste n'est pas modifiée. nouveau projetsSimilaires");
								System.out.println("Connection send "
										+ projetJson.toString());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else {
							System.out
									.println("pas de nouveau projet similaire");

						}

					} else {
						System.err
								.println("The agent does not accept to interact");
						timer.cancel();
					}
				}
			}, new Date(), 5000);
		}

		private void recupereDoapProjet(String _projetID2) {
			String doapID = _projetID2;
			String message = "Recherche des elements du projet " + doapID;
			System.out.println(message);
			String queryString = /*
								 * "PREFIX dc: <http://purl.org/dc/elements/1.1/> "
								 * +
								 * "PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
								 * +
								 * "PREFIX smag:   <http://smag0.blogspot.fr/ns/smag0#>"
								 * +
								 */
			"select * where {<http://smag0.blogspot.fr/ns/smag0#" + doapID
					+ "> ?propriete ?objet} ";
			Query query = QueryFactory.create(queryString);
			QueryExecution qexec = QueryExecutionFactory.sparqlService(
					"http://fuseki-smag0.rhcloud.com/ds/query", query);
			ResultSet results = qexec.execSelect();
			int i = 0;
			for (; results.hasNext();) {
				i++;
				QuerySolution soln = results.nextSolution();
				RDFNode propriete = soln.get("propriete");
				RDFNode objet = soln.get("objet");
				String objectResultat = null;
				if (objet.isResource()) {
					objectResultat = objet.asResource().toString();
				} else if (objet.isLiteral()) {
					objectResultat = objet.asLiteral().toString();
				}

				JSONObject jresult = new JSONObject();
				jresult.put("projet", doapID);
				jresult.put("propriete", propriete.toString());
				jresult.put("objet", objectResultat);
				projetJson.put(String.valueOf(i), jresult);
			}
			System.out.println(projetJson.toString());

			try {
				connection.sendMessage(projetJson.toString());

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void onClose(int closeCode, String message) {
			timer.cancel();

		}

		@Override
		public void onMessage(String data) {
			System.out.println("Page projet reçu " + data);

		}
	}
}
