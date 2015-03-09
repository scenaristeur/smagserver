package favedave.smag0;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.json.JSONObject;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class DiamondSocketServlet extends WebSocketServlet {
	private JSONObject messageJson = new JSONObject();
	OntModel model = ModelFactory.createOntologyModel();
	String diamondDefaultSource = "https://raw.githubusercontent.com/scenaristeur/smagserver/master/src/main/webapp/ontologies/diamond.owl";
	/*
	 * Source alternative : http://smag0.rww.io/diamond.owl
	 */
	String namespace = "http://smag0.blogspot.fr/ontologies/Diamond#";

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
		// TODO Auto-generated method stub
		return new DiamondSocket();
	}

	public class DiamondSocket implements WebSocket.OnTextMessage {
		private Connection connection;

		// messageJson = new JSONObject();
		// messageJson.put("message","pas de message");
		@Override
		public void onOpen(Connection connection) {
			this.connection = connection;
			System.out.println("DSS : Connection DiamondSocketServlet");
			messageJson.put("message", "Connexion à la Socket méthode Diamond");
			envoiMessage();
		}

		@Override
		public void onClose(int closeCode, String message) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onMessage(String data) {
			if (data.equals("diamond")) {
				System.out.println("DSS : message :" + data);
				messageJson.put("message",
						"Construction de la requete pour Diamond");
				envoiMessage();
				constructionRequete();
				messageJson.put("message", "Execution de la requete Diamond");
				envoiMessage();
				executionRequete();
				messageJson.put("message",
						"Construction message pour la réponse Diamond");
				envoiMessage();
				constructionMessage();
				messageJson.put("message", "OK");
				envoiMessage();

			} else {
				System.out.println("DSS : traiter les messages du type :"
						+ data);
			}
		}

		private void constructionRequete() {
			Model m = FileManager.get().loadModel(diamondDefaultSource);
			if (m == null) {
				throw new IllegalArgumentException("File: "
						+ diamondDefaultSource + " not found");
			} else {
				model.add(m);
			}
			ExtendedIterator<OntClass> rootClasses = model
					.listHierarchyRootClasses();
			while (rootClasses.hasNext()) {
				OntClass rootClass = rootClasses.next();
				System.out.println("####RootClasse : " + rootClass.toString());
				if (namespace == null) {
					namespace = model.getNsPrefixURI(rootClass.toString());
					System.out.println("NS :" + namespace);
				}
			}
			// String etapeDiamondUri = namespace + data;
			String procedureDiamondUri = namespace + "ProcedureDiamond";
			Resource procedureDiamondRessource = model
					.getResource(procedureDiamondUri);
			Property smagHasPart = model.getProperty(namespace + "hasPart");
			String queryString = "PREFIX diamond: <" + namespace + "> \n";
			queryString += "select ?etape ?propriete ?objet . \n";
			queryString += "where {";
			queryString += "<" + procedureDiamondRessource
					+ "> ?propriete ?objet. \n";
			queryString += "}";
			System.out.println("requete" + queryString);
			Query query = QueryFactory.create(queryString);
			QueryExecution qexec = QueryExecutionFactory.create(query, model);
			ResultSet results = qexec.execSelect();
			for (; results.hasNext();) {
				QuerySolution soln = results.nextSolution();
				RDFNode propriete = soln.get("propriete");
				RDFNode x = soln.get("objet");
				Resource r = soln.getResource("etape");
				/*
				 * if (propriete == smagHasPart) {
				 * System.out.println(x.toString()); } else {
				 */
				// RDFNode x = soln.get("objet"); // Get a result
				// variable by name.
				// Resource r = soln.getResource("etape"); // Get a
				// result
				// variable -
				// must be a
				// resource
				// Literal l = soln.getLiteral("objet"); // Get a
				// result
				// variable -
				// must be a
				// literal
				System.out.println("DSS " + soln);
				// }
			}
		}

		private void executionRequete() {

		}

		private void constructionMessage() {

		}

		private void envoiMessage() {

			try {
				connection.sendMessage(messageJson.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
}
