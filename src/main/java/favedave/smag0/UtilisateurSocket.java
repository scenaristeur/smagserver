package favedave.smag0;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.json.JSONException;
import org.json.JSONObject;

import favedave.smag.objets.ObjetConnecte;

public class UtilisateurSocket extends WebSocketServlet {
	private String email;
	JSONObject received = new JSONObject();

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
		/*
		 * Enumeration<String> parameterNames = request.getParameterNames();
		 * 
		 * while (parameterNames.hasMoreElements()) { String paramName =
		 * parameterNames.nextElement(); System.out.println(paramName); //
		 * System.out.println("\n"); String[] paramValues =
		 * request.getParameterValues(paramName); for (int i = 0; i <
		 * paramValues.length; i++) { String paramValue = paramValues[i];
		 * System.out.println("\t" + paramValue); // System.out.println("\n"); }
		 * } // projetsSimilairesPrecedent.put("precedent", "vide"); email =
		 * request.getParameter("email"); // projet =
		 * request.getParameter("projet"); System.out.println(email); // return
		 * new PageProjet(projet);
		 */
		return new Utilisateur();
	}

	public class Utilisateur implements WebSocket.OnTextMessage {
		private Connection connection;

		@Override
		public void onOpen(Connection connection) {
			this.connection = connection;

		}

		@Override
		public void onClose(int closeCode, String message) {

		}

		@Override
		public void onMessage(String data) {
			System.out.println("Message reçu de la Page utilisateur : " + data);
			JSONObject receivedTemp = new JSONObject(data);
			Map<String, String> out = new HashMap<String, String>();
			parse(receivedTemp, out);
			String type = out.get("type");

			if (type.equals("nouvelObjetConnecte")) {
				System.out
						.println("Procedure de creation d'un nouvel objet connecté");
				/*
				 * connection à nouveau Objet socket, puis envoi du receivedTemp
				 */
				ObjetConnecte objetnouveau = new ObjetConnecte();
				String resultatCreationObjet = objetnouveau
						.setJsonParamaters(receivedTemp);
				try {
					System.out.println("envoi du message OK objet cree");
					connection.sendMessage(resultatCreationObjet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				System.out
						.println("Creer une methode pour traiter le type de message "
								+ type);
			}
		}
	}

	/*
	 * requete pour modifier une info sur le serveur : PREFIX dc:
	 * <http://purl.org/dc/elements/1.1/> PREFIX smag:
	 * <http://smag0.blogspot.fr/ns/smag0#>
	 * 
	 * WITH <http://smag0.blogspot.fr/GraphTest> DELETE { ?objet
	 * dc:emailGestionnaire 'hjb@jk.ki' } INSERT { ?objet smag:emailGestionnaire
	 * 'scenaristeur@gmail.com' } WHERE { ?objet dc:emailGestionnaire
	 * 'hjb@jk.ki' }
	 */
	public static Map<String, String> parse(JSONObject json,
			Map<String, String> out) throws JSONException {
		Iterator<String> keys = json.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			String val = null;
			try {
				JSONObject value = json.getJSONObject(key);
				parse(value, out);
			} catch (Exception e) {
				val = json.getString(key);
			}

			if (val != null) {
				out.put(key, val);
			}
		}
		return out;
	}

}
