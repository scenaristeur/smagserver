package favedave.smag0;

import java.io.IOException;

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
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.query.ResultSetRewindable;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;

public class ListeProjetsSocket extends WebSocketServlet {

	JSONObject jsonListe = new JSONObject();

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
		// TODO Auto-generated method stub
		return new ListeProjets();
	}

	public class ListeProjets implements WebSocket.OnTextMessage {
		private Connection connection;
		private ResultSetRewindable resultats;
		private ResultSetRewindable resultatsJson;

		@Override
		public void onOpen(Connection connection) {
			this.connection = connection;
			System.out.println("Liste Projet open");
			prepareRequete();
		}

		private void prepareRequete() {

			String queryString = "select ?projet ?titre ?description where {"
					+ "?projet <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://smag0.blogspot.fr/ns/smag0#Projet> ."
					+ "?projet <http://purl.org/dc/elements/1.1/title> ?titre ."
					+ "?projet <http://purl.org/dc/elements/1.1/description> ?description ."
					+ "}" + "ORDER BY DESC(?projet)";
			// + "}" + "ORDER BY DESC(?projet)" + "LIMIT 1000";
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
				Resource projet = soln.getResource("projet"); // Get a result
																// variable -
																// must be a
																// resource
				Literal titre = soln.getLiteral("titre");
				Literal description = soln.getLiteral("description"); // Get a
																		// result
																		// variable
																		// -
																		// must
																		// be a
																		// literal
				JSONObject jresult = new JSONObject();
				jresult.put("projet", projet.getLocalName().toString());
				jresult.put("titre", titre);
				jresult.put("description", description);
				System.out.println(jresult.toString());
				jsonListe.put(String.valueOf(i), jresult);
			}
			try {
				connection.sendMessage(jsonListe.toString());
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
			System.out.println("Liste projets message reçu " + data);

		}
	}
}
