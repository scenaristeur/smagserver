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
import com.hp.hpl.jena.rdf.model.Resource;
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
				jsonListe.put("type", "update");
				System.out.println("traitement de la demande : " + type);
				String email = out.get("email");
				System.out.println("Recuperation des liens de " + email);
				// select * where {?s ?p 'scenaristeur@gmail.com' }
				String queryString = "PREFIX smag:   <http://smag0.blogspot.fr/ns/smag0#> \n";
				queryString += "PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n";
				queryString += "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n";
				queryString += "SELECT ?ressource ?lien ?proprieteEtendue ?objetEtendu \n";
				queryString += "WHERE {?ressource ?lien \'" + email + "\' . \n";
				queryString += "?ressource ?proprieteEtendue ?objetEtendu . \n";
				queryString += "}";
				// queryString +=
				// "?objetconnecte dc:description ?description . \n";
				// queryString +=
				// "?objetconnecte smag:adresseIpObjet ?adresseIpObjet . \n";
				// queryString +=
				// "?objetconnecte smag:portObjet ?portObjet . \n";

				// ?objetconnecte smag:emailGestionnaire
				// 'scenaristeur@gmail.com' .
				// ?objetconnecte ?propriete ?valeur .
				// +
				// "?projet <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://smag0.blogspot.fr/ns/smag0#Projet> ."
				// + "?projet <http://purl.org/dc/elements/1.1/title> ?titre ."
				// +
				// "?projet <http://purl.org/dc/elements/1.1/description> ?description ."
				// +"}" + "ORDER BY DESC(?s)";
				// + "}" + "ORDER BY DESC(?projet)" + "LIMIT 1000";
				System.out.println(queryString);
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
					// RDFNode x = soln.get("varName") ; // Get a result
					// variable by
					// name.
					Resource ressource = soln.getResource("ressource");
					// RDFNode propriete = soln.get("propriete");// Get
					// RDFNode valeur = soln.get("valeur");
					RDFNode lien = soln.get("lien");
					RDFNode proprieteEtendue = soln.get("proprieteEtendue");
					RDFNode objetEtendu = soln.get("objetEtendu");
					String objetEtenduResultat = null;
					if (objetEtendu.isResource()) {
						objetEtenduResultat = objetEtendu.asResource()
								.toString();
					} else if (objetEtendu.isLiteral()) {
						objetEtenduResultat = objetEtendu.asLiteral()
								.toString();
					}
					JSONObject jresult = new JSONObject();
					jresult.put("ressourceShort", ressource.getLocalName()
							.toString());
					jresult.put("ressource", ressource.toString());
					jresult.put("lien", lien.toString());
					jresult.put("proprieteEtendue", proprieteEtendue.toString());
					jresult.put("objetEtendu", objetEtenduResultat);
					/*
					 * System.out.println(jresult.toString()); try {
					 * connection.sendMessage(jresult.toString()); } catch
					 * (IOException e) { // TODO Auto-generated catch block
					 * e.printStackTrace(); }
					 */
					jsonListe.put(String.valueOf(i), jresult);
				}
				/*
				 * try { connection.sendMessage("fin de la liste"); } catch
				 * (IOException e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); }
				 */

				System.out.println(jsonListe.toString());
				try {
					// connection.sendMessage("houlaHop");
					connection.sendMessage(jsonListe.toString());
				} catch (IOException e) { // TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (type.equals("nouveauLien")) {

				System.out.println("traiter les lessages de type : " + type);
				String lienAjouteValue = out.get("lienAjouteValue");
				String lienObjetValue = out.get("lienObjetValue");
				String typeObjet = out.get("typeObjet");
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
				if (typeObjet.equals("ressource")) {
					update += "smag:" + id + "   <" + lienAjouteValue + "> "
							+ lienObjetValue + " . \n";
					System.out.println(lienObjetValue
							+ " est inseré comme une ressource");
				} else {
					update += "smag:" + id + "   <" + lienAjouteValue
							+ ">         \"" + lienObjetValue + "\" . \n";
					System.out.println(lienObjetValue
							+ " est inseré comme du texte");
				}
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
			} else {
				System.out.println("!!!!! traiter les les messages de type : "
						+ type);
			}
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
