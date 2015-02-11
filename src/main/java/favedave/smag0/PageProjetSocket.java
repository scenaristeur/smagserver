package favedave.smag0;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.json.JSONObject;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class PageProjetSocket extends WebSocketServlet {
	JSONObject projetJson = new JSONObject();

	private String projet;

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

		projet = request.getParameter("projet");
		return new PageProjet();
	}

	public class PageProjet implements WebSocket.OnTextMessage {
		private Connection connection;

		@Override
		public void onOpen(Connection connection) {
			// TODO Auto-generated method stub
			this.connection = connection;
			System.out.println("connexion page projet " + projet);

			recupereDoapProjet();

		}

		private void recupereDoapProjet() {
			String message = "Recherche des elements du projet " + projet;
			System.out.println(message);
			String queryString = /*
								 * "PREFIX dc: <http://purl.org/dc/elements/1.1/> "
								 * +
								 * "PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
								 * +
								 * "PREFIX smag:   <http://smag0.blogspot.fr/ns/smag0#>"
								 * +
								 */
			"select * where {<http://smag0.blogspot.fr/ns/smag0#" + projet
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
				jresult.put("projet", projet);
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
			// TODO Auto-generated method stub

		}

		@Override
		public void onMessage(String data) {
			System.out.println("Page projet reçu " + data);
			/*
			 * try { // connection.sendMessage(doapJson.toString());
			 * connection.sendMessage("test doapjson");
			 * System.out.println("ENVOI : " + doapJson.toString()); } catch
			 * (IOException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
			 */
		}
	}
}
