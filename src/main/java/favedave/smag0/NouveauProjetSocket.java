package favedave.smag0;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.json.JSONException;
import org.json.JSONObject;

public class NouveauProjetSocket extends WebSocketServlet {

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

			String type = out.get("type");
			String titre = out.get("titre");
			String description = out.get("description");
			String date = out.get("date");

			System.out
					.println("Recherche pour savoir si un projet avec le même nom existe ou un nom proche"
							+ type
							+ " "
							+ titre
							+ " "
							+ description
							+ " "
							+ date);

			/*
			 * Iterator<String> keys = dataJson.keys(); String type = null;
			 * String titre = null; String description = null; String date =
			 * "12";
			 * 
			 * while (keys.hasNext()) { String key = keys.next();
			 * System.out.println("##" + key + "##"); if (key == "\"type\"") {
			 * type = dataJson.getString(key); System.out.println(key + " dd: "
			 * + type); } if (key == "\"titre\"") { titre =
			 * dataJson.getString(key); System.out.println(key + " dd: " +
			 * titre); } if (key == "description") { description =
			 * dataJson.getString(key); System.out.println(key + " dd: " +
			 * description); } if (key == "date") { date =
			 * dataJson.getString(key); System.out.println(key + " dd: " +
			 * date); } if (dataJson.get(key) instanceof JSONObject) {
			 * System.out.println("instance of json " + dataJson.get(key)); }
			 * else { System.out.println(key + " : " + dataJson.get(key)); }
			 * System.out.println(type + " " + titre + " " + description + " " +
			 * date); }
			 */
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
