package favedave.smag.projets;

// nouvelle version en cours de developpement : favedave.smag0.DiamondSocketServlet
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
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class MethodeHtml5Servlet extends WebSocketServlet {

	OntModel model = ModelFactory.createOntologyModel();
	String diamondDefaultSource = "https://raw.githubusercontent.com/scenaristeur/smagserver/master/src/main/webapp/ontologies/diamond.owl";
	/*
	 * Source alternative : http://smag0.rww.io/diamond.owl
	 */
	String resultat = "resultat vide";
	JSONObject j = new JSONObject();
	String namespace = "http://smag0.blogspot.fr/ontologies/Diamond#";

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
		// System.out.println("servlet Methode activée");
		Model m = FileManager.get().loadModel(diamondDefaultSource);
		if (m == null) {
			throw new IllegalArgumentException("File: " + diamondDefaultSource
					+ " not found");
		} else {
			model.add(m);
		}
		String procedureDiamondUri = namespace + "ProcedureDiamond";
		Resource procedureDiamondRessource = model
				.getResource(procedureDiamondUri);
		SimpleSelector selector = new SimpleSelector(procedureDiamondRessource,
				(Property) null, (RDFNode) null);
		StmtIterator iter = model.listStatements(selector);
		resultat = namespace + "\n";
		int i = 0;
		while (iter.hasNext()) {
			i++;
			Statement stmt = iter.nextStatement();
			Resource subject = stmt.getSubject();
			Property predicate = stmt.getPredicate();
			RDFNode object = stmt.getObject();
			String objectResultat = null;
			if (object.isResource()) {
				objectResultat = object.asResource().getLocalName().toString();
			} else if (object.isLiteral()) {
				objectResultat = object.asLiteral().toString();
			}
			// System.out.println(subject + "\t" + predicate + "\t" + object);
			resultat += subject + " " + predicate + "\t" + object + "\n";
			JSONObject jresult = new JSONObject();
			jresult.put("subject", subject.getLocalName().toString());
			jresult.put("predicate", predicate.getLocalName().toString());
			jresult.put("object", objectResultat);
			// System.out.println(jresult.toString());
			j.put(String.valueOf(i), jresult);
		}
		// System.out.println(resultat);
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

		// model.write(System.out);
		return new MethodeSocket();
	}

	public class MethodeSocket implements WebSocket.OnTextMessage {
		private Connection connection;

		@Override
		public void onOpen(Connection connection) {
			this.connection = connection;
			// System.out.println("Methode open");

		}

		@Override
		public void onClose(int closeCode, String message) {
			// System.out.println("Methode close");
		}

		@Override
		public void onMessage(String data) {
			if (data.equals("diamond")) {
				// System.out.println("\n\nRecuperation de la methode" + data);
				System.out
						.println("récupération des classes principales de la méthode");
				try {
					connection.sendMessage(j.toString());
					// System.out.println("RESULTAT ENVOYE en JSON : " +
					// j.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("message recu par Socket Diamond" + data);
				String etapeDiamondUri = namespace + data;
				Resource rechercheResource = model.getResource(etapeDiamondUri);
				Property smagHasPart = model.getProperty(namespace + "hasPart");
				String queryString = "PREFIX smag0: <" + namespace + "> "
						+ "select  ?propriete ?objet where {<"
						+ rechercheResource + "> ?propriete ?objet} ";
				System.out.println("\n\nRequete DETAILS" + queryString);
				Query query = QueryFactory.create(queryString);
				QueryExecution qexec = QueryExecutionFactory.create(query,
						model);
				ResultSet results = qexec.execSelect();
				int i = 0;
				for (; results.hasNext();) {

					QuerySolution soln = results.nextSolution();
					RDFNode propriete = soln.get("propriete");
					RDFNode objet = soln.get("objet");
					// Resource ressource = rechercheResource;
					System.out.println(soln);
					String objetResultat = null;
					if (objet.isResource()) {
						objetResultat = objet.asResource().toString();
					} else if (objet.isLiteral()) {
						objetResultat = objet.asLiteral().toString();
					}
					JSONObject jresult = new JSONObject();
					jresult.put("ressourceShort", rechercheResource
							.getLocalName().toString());
					jresult.put("ressource", rechercheResource.toString());
					jresult.put("lien", propriete.toString());
					jresult.put("objet", objetResultat);
					j.put(String.valueOf(i), jresult);
					i++;
				}

				j.put("message", "detailDiamond");

				System.out.println(j.toString());
				try {
					connection.sendMessage(j.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
