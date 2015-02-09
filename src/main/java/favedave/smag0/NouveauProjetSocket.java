package favedave.smag0;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.json.JSONException;
import org.json.JSONObject;

import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;

public class NouveauProjetSocket extends WebSocketServlet {

	public String type;
	public String titre;
	public String description;
	public String date;
	public String id;

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
		// TODO Auto-generated method stub
		return new NouveauProjet();
	}

	public class NouveauProjet implements WebSocket.OnTextMessage {
		private Connection connection;

		@Override
		public void onOpen(Connection connection) {
			this.connection = connection;
			System.out.println("Nouveau Projet open");

		}

		@Override
		public void onClose(int closeCode, String message) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onMessage(String data) {
			System.out.println("Nouveau projet message" + data);
			JSONObject dataJson = new JSONObject(data);

			Map<String, String> out = new HashMap<String, String>();

			parse(dataJson, out);

			type = out.get("type");
			titre = out.get("titre");
			description = out.get("description");
			date = out.get("date");
			id = "P" + date;

			System.out
					.println("Recherche pour savoir si un projet avec le même nom existe ou un nom proche"
							+ type
							+ " "
							+ titre
							+ " "
							+ description
							+ " "
							+ date);

			insertionProjet();
		}

		private void insertionProjet() {
			UpdateRequest ur = new UpdateRequest();
			UpdateProcessor up;
			String service = "http://fuseki-smag0.rhcloud.com/ds/update";
			String update;
			update = "PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";
			update += "PREFIX smag:   <http://smag0.blogspot.fr/ns/smag0#>";
			update += "PREFIX dc: <http://purl.org/dc/elements/1.1/>";

			update += "INSERT DATA {";
			update += "GRAPH <http://smag0.blogspot.fr/GraphTest>{";

			update += "smag:" + id + "    rdf:type         smag:Projet .";
			update += "smag:" + id + "   dc:title         '" + titre + "' .";
			update += "smag:" + id + "   dc:description         '"
					+ description + "' .";
			/*
			 * update += "ex:cat     rdfs:subClassOf  ex:animal ."; update +=
			 * "zoo:host   rdfs:range       ex:animal ."; update +=
			 * "ex:zoo1    zoo:host         ex:cat2 ."; update +=
			 * "ex:cat3    owl:sameAs       ex:cat2 .";
			 */
			update += "}}";
			// ur.add("INSERT {<bouuula> <bouuuulb> <bouuulc>} WHERE {?s ?p ?o}");
			// ur.add("INSERT DATA { <http://website.com/exmp/something> http://purl.org/dc/elements/1.1/title ‘Some title’}");
			ur.add(update);

			up = UpdateExecutionFactory.createRemote(ur, service);
			up.execute();
			System.out.println("test inséré");

		}

	}

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
