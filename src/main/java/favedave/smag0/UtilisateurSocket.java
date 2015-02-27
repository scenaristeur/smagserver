package favedave.smag0;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

public class UtilisateurSocket extends WebSocketServlet {
	private String email;

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
			System.out.println("Page utilisateur reçu " + data);

		}
	}
}
