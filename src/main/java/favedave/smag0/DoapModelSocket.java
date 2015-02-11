package favedave.smag0;

import java.io.IOException;

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

public class DoapModelSocket extends WebSocketServlet {
	JSONObject doapJson = new JSONObject();
	String langue = "fr";

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
		// TODO Auto-generated method stub
		return new DoapModel();
	}

	public class DoapModel implements WebSocket.OnTextMessage {
		private Connection connection;

		@Override
		public void onOpen(Connection connection) {
			this.connection = connection;
			recupereDOAPModel();
		}

		@Override
		public void onClose(int closeCode, String message) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onMessage(String data) {
			// TODO Auto-generated method stub

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
	}
}
