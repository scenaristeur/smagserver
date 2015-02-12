package favedave.smag0.agents.sockets;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.json.JSONObject;

public class AgentProjetSocket extends WebSocketServlet {
	JSONObject projetJson = new JSONObject();

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
		// TODO Auto-generated method stub
		return null;
	}

}
