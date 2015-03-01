package favedave.smag0;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.json.JSONException;
import org.json.JSONObject;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.query.ResultSetRewindable;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;
import com.hp.hpl.jena.util.FileManager;

public class RelationSocket extends WebSocketServlet {
	JSONObject jsonListe = new JSONObject();

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
		// TODO Auto-generated method stub
		return new Relation();
	}

	public class Relation implements WebSocket.OnTextMessage {
		private Connection connection;
		private ResultSetRewindable resultats;
		private ResultSetRewindable resultatsJson;

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

			if (type.equals("listeProprietes")) {
				System.out.println("traitement du message de type  : " + type);
				String vocabulaire = out.get("vocabulaire");
				String vocabulaireSource = out.get("vocabulaireSource");
				String vocabulaireType = out.get("vocabulaireType");
				if (vocabulaireType.equals("sparqlEndpoint")) {
					prepareRequeteSparql(vocabulaire, vocabulaireSource);
				} else {
					prepareRequeteRdf(vocabulaire, vocabulaireSource);
				}

			} else if (type.equals("update")) {
				System.out.println("traitement de la demande : " + type);
			} else if (type.equals("nouveauLien")) {

				System.out.println("traiter les lessages de type : " + type);
				String lienAjouteValue = out.get("lienAjouteValue");
				String lienObjetValue = out.get("lienObjetValue");
				// Test a faire si lienObjetValue est une ressource / ou le
				// recuperer du formulaire
				String emailNouveauLienValue = out.get("emailNouveauLienValue");
				String date = out.get("date");
				String id = "U" + date;
				System.out.println(lienAjouteValue + " " + lienObjetValue);
				UpdateRequest ur = new UpdateRequest();
				UpdateProcessor up;
				String service = "http://fuseki-smag0.rhcloud.com/ds/update";
				String update;
				update = "PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n";
				update += "PREFIX smag:   <http://smag0.blogspot.fr/ns/smag0#> \n";
				update += "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n";
				update += "PREFIX foaf: <http://xmlns.com/foaf/0.1/> \n";

				update += "INSERT DATA {";
				update += "GRAPH <http://smag0.blogspot.fr/GraphTest>{";

				update += "smag:" + id
						+ "    rdf:type         smag:Utilisateur . \n";
				update += "smag:" + id + "   foaf:mbox         \""
						+ emailNouveauLienValue + "\" . \n";
				update += "smag:" + id + "   <" + lienAjouteValue
						+ ">         \"" + lienObjetValue + "\" . \n";
				/*
				 * update += "ex:cat     rdfs:subClassOf  ex:animal ."; update
				 * += "zoo:host   rdfs:range       ex:animal ."; update +=
				 * "ex:zoo1    zoo:host         ex:cat2 ."; update +=
				 * "ex:cat3    owl:sameAs       ex:cat2 .";
				 */
				update += "}} \n";

				System.out.println(update);
				// ur.add("INSERT {<bouuula> <bouuuulb> <bouuulc>} WHERE {?s ?p ?o}");
				// ur.add("INSERT DATA { <http://website.com/exmp/something> http://purl.org/dc/elements/1.1/title ‘Some title’}");
				ur.add(update);

				up = UpdateExecutionFactory.createRemote(ur, service);
				up.execute();
				System.out.println("lien inséré");
			}
			System.out.println("traiter les lessages de type : " + type);
		}

		private void prepareRequeteRdf(String vocabulaire,
				String vocabulaireSource) {
			OntModel model = ModelFactory.createOntologyModel();
			String source = vocabulaireSource;
			// Read the ontology file
			String resultat = "resultat vide";
			JSONObject j = new JSONObject();
			// Get the base namespace
			String namespace = vocabulaire;
			Model m = FileManager.get().loadModel(source);
			if (m == null) {
				throw new IllegalArgumentException("File: " + source
						+ " not found");
			} else {
				model.add(m);
				model.write(System.out);
				// REQUETE EN Doublon pour test, a fusionner ensuite avec le
				// requete de prepareRequeteSparql
				String queryString = "PREFIX smag:   <http://smag0.blogspot.fr/ns/smag0#> \n";
				queryString += "PREFIX prefixAchanger:   <" + vocabulaire
						+ "> \n";
				queryString += "PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n";
				queryString += "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n";
				queryString += "SELECT ?propriete WHERE {?propriete rdf:type <http://www.w3.org/1999/02/22-rdf-syntax-ns#Property> } \n";
				queryString += "GROUP BY ?propriete ORDER BY ?propriete";
				System.out.println(queryString);
				Query query = QueryFactory.create(queryString);
				QueryExecution qexec = QueryExecutionFactory.create(query,
						model);
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
					RDFNode propriete = soln.getResource("propriete");
					String proprieteResultat = null;
					String ns = null;
					if (propriete.isResource()) {
						proprieteResultat = propriete.asResource()
								.getLocalName().toString();
						ns = propriete.asResource().getNameSpace();

					} else if (propriete.isLiteral()) {
						proprieteResultat = propriete.asLiteral().toString();
					}
					JSONObject jresult = new JSONObject();
					jresult.put("propriete", propriete.toString());
					jresult.put("proprieteLocalName",
							proprieteResultat.toString());
					jresult.put("ns", ns);

					/*
					 * try { connection.sendMessage(jresult.toString()); } catch
					 * (IOException e) {
					 * 
					 * e.printStackTrace(); }
					 */
					jsonListe.put(String.valueOf(i), jresult);
				}
				System.out.println(jsonListe.toString());
				try {
					connection.sendMessage(jsonListe.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		private void prepareRequeteSparql(String vocabulaire,
				String vocabulaireSource) {
			// String source = "http://fuseki-smag0.rhcloud.com/ds/query";
			String source = vocabulaireSource;
			String queryString = "PREFIX smag:   <http://smag0.blogspot.fr/ns/smag0#> \n";
			queryString += "PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n";
			queryString += "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n";
			queryString += "select ?propriete where {?s ?propriete ?o} \n";
			queryString += "GROUP BY ?propriete";
			System.out.println(queryString);
			Query query = QueryFactory.create(queryString);
			QueryExecution qexec = QueryExecutionFactory.sparqlService(source,
					query);
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
				RDFNode propriete = soln.getResource("propriete");
				JSONObject jresult = new JSONObject();
				jresult.put("propriete", propriete.toString());
				System.out.println(jresult.toString());
				/*
				 * try { connection.sendMessage(jresult.toString()); } catch
				 * (IOException e) {
				 * 
				 * e.printStackTrace(); }
				 */
				jsonListe.put(String.valueOf(i), jresult);
			}
			try {
				connection.sendMessage(jsonListe.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
