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

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.query.ResultSetRewindable;
import com.hp.hpl.jena.rdf.model.Resource;

public class ListeObjetsConnectesSocket extends WebSocketServlet {
	JSONObject jsonListe = new JSONObject();

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
		// TODO Auto-generated method stub
		return new ListeObjetsConnectes();
	}

	public class ListeObjetsConnectes implements WebSocket.OnTextMessage {
		private Connection connection;
		private ResultSetRewindable resultats;
		private ResultSetRewindable resultatsJson;

		@Override
		public void onOpen(Connection connection) {
			this.connection = connection;

		}

		@Override
		public void onClose(int closeCode, String message) {
			System.out.println(closeCode + " : " + message);

		}

		@Override
		public void onMessage(String data) {
			System.out.println("Message reçu de la Page utilisateur : " + data);
			JSONObject receivedTemp = new JSONObject(data);
			Map<String, String> out = new HashMap<String, String>();
			parse(receivedTemp, out);
			String type = out.get("type");

			if (type.equals("update")) {
				String email = out.get("email");
				System.out.println("Recuperation liste objet connecté de "
						+ email);
				prepareRequete(email);

			} else {
				System.out
						.println("Sur la page liste objets connecteer, traiter les messages du type "
								+ type);
			}
		}

		public Map<String, String> parse(JSONObject json,
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

		private void prepareRequete(String email) {
			// select * where {?s ?p 'scenaristeur@gmail.com' }
			String queryString = "select * where {?objetconnecte ?propriete \'"
					+ email + "\'}";
			// +
			// "?projet <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://smag0.blogspot.fr/ns/smag0#Projet> ."
			// + "?projet <http://purl.org/dc/elements/1.1/title> ?titre ."
			// +
			// "?projet <http://purl.org/dc/elements/1.1/description> ?description ."
			// +"}" + "ORDER BY DESC(?s)";
			// + "}" + "ORDER BY DESC(?projet)" + "LIMIT 1000";
			System.out.println(queryString);
			Query query = QueryFactory.create(queryString);
			QueryExecution qexec = QueryExecutionFactory.sparqlService(
					"http://fuseki-smag0.rhcloud.com/ds/query", query);
			try {
				ResultSet results = qexec.execSelect();
				resultats = ResultSetFactory.copyResults(results);
				resultatsJson = ResultSetFactory.copyResults(results);
			} finally {
				qexec.close();
			}
			int i = 0;
			for (; resultats.hasNext();) {
				i++;
				QuerySolution soln = resultats.nextSolution();
				// RDFNode x = soln.get("varName") ; // Get a result variable by
				// name.
				Resource objetconnecte = soln.getResource("objetconnecte"); // Get
																			// a
																			// result
				// variable -
				// must be a
				// resource
				// Literal titre = soln.getLiteral("titre");
				// Literal description = soln.getLiteral("description"); // Get
				// a
				// result
				// variable
				// -
				// must
				// be a
				// literal
				JSONObject jresult = new JSONObject();
				jresult.put("objetConnecte", objetconnecte.getLocalName()
						.toString());
				// jresult.put("titre", titre);
				// jresult.put("description", description);
				System.out.println(jresult.toString());
				jsonListe.put(String.valueOf(i), jresult);
			}
			System.out.println(jsonListe.toString());
			try {
				connection.sendMessage("houlaHop");
				connection.sendMessage(jsonListe.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
