package favedave.smag0;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.json.JSONException;
import org.json.JSONObject;

public class SocketModel extends WebSocketServlet {
	JSONObject jsonListe = new JSONObject();

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
		// TODO Auto-generated method stub
		return new Relation();
	}

	public class Relation implements WebSocket.OnTextMessage {
		private Connection connection;

		@Override
		public void onOpen(Connection connection) {
			this.connection = connection;

		}

		@Override
		public void onClose(int closeCode, String message) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onMessage(String data) {
			System.out.println(data);
			System.out.println("Message reçu : " + data);
			JSONObject receivedTemp = new JSONObject(data);
			Map<String, String> out = new HashMap<String, String>();
			parse(receivedTemp, out);
			String type = out.get("type");

			if (type.equals("update")) {
				System.out.println("traitement du message de type  : " + type);
			} else {
				System.out.println("traiter les lessages de type : " + type);
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
	}
}
