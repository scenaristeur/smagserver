package favedave.smag0;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.json.JSONObject;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;

public class PageProjetSocket extends WebSocketServlet {
	JSONObject projetJson = new JSONObject();
	JSONObject doapJson = new JSONObject();
	JSONObject methodeJson = new JSONObject();
	String langue = "fr";
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
			recupereDOAPModel();
			recupereDoapProjet();
			recupereMethode();
		}

		private void recupereDoapProjet() {
			String message = "Recherche des elements du projet " + projet;
			System.out.println(message);

		}

		private void recupereDOAPModel() {
			String doapDefaultSource = "http://usefulinc.com/ns/doap#";
			Model model = FileManager.get().loadModel(doapDefaultSource);
			// Model m = ModelFactory.createDefaultModel();
			// RDFReader reader = model.getReader("fr");
			// model.write(System.out);
			Property propertyLabel = model
					.getProperty("http://www.w3.org/2000/01/rdf-schema#label");
			StmtIterator it = model.listStatements(null, propertyLabel, null,
					langue);
			// display the 3 statements
			int i = 0;
			for (; it.hasNext();) {
				i++;
				Statement doapLigne = it.next();
				System.out.println(doapLigne);
				Resource subject = doapLigne.getSubject();
				Property predicate = doapLigne.getPredicate();
				RDFNode object = doapLigne.getObject();
				String objectResultat = null;
				if (object.isResource()) {
					objectResultat = object.asResource().getLocalName()
							.toString();
				} else if (object.isLiteral()) {
					objectResultat = object.asLiteral().toString();
				}
				// System.out.println(subject + "\t" + predicate + "\t" +
				// object);

				JSONObject jresult = new JSONObject();
				jresult.put("subject", subject.getLocalName().toString());
				jresult.put("predicate", predicate.getLocalName().toString());
				jresult.put("object", objectResultat);

				doapJson.put(String.valueOf(i), jresult);

			}
			// SimpleSelector selector = new SimpleSelector((Resource) null,
			// (Property) null, (RDFNode) null, "fr");
			// SimpleSelector((Resource) null, (Property) null, (String) null,
			// (String) null);
			System.out.println(doapJson.toString());
			try {
				connection.sendMessage(doapJson.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void recupereMethode() {
			// TODO Auto-generated method stub

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
