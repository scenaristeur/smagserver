package favedave.smag.projets;

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
	// retourne la méthode (Diamond ou Aspecs ) d'un projet
	// Create the Ontology Model
	OntModel model = ModelFactory.createOntologyModel();
	String diamondDefaultSource = "https://raw.githubusercontent.com/scenaristeur/smagserver/master/src/main/webapp/ontologies/diamond.owl";
	/*
	 * Source alternative : http://smag0.rww.io/diamond.owl
	 */

	// Read the ontology file
	// model.begin();
	String resultat = "resultat vide";
	JSONObject j = new JSONObject();
	// Get the base namespace
	String namespace = "http://smag0.blogspot.fr/ontologies/Diamond#";

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
		System.out.println("servlet Methode activée");
		Model m = FileManager.get().loadModel(diamondDefaultSource);

		if (m == null) {
			throw new IllegalArgumentException("File: " + diamondDefaultSource
					+ " not found");
		} else {
			model.add(m);
		}
		// model.read(in,"");
		// model.commit();

		String procedureDiamondUri = namespace + "ProcedureDiamond";
		// rechercher la ressource MethodeDiamond
		Resource procedureDiamondRessource = model
				.getResource(procedureDiamondUri);
		// Ontology ontologie=model.getOntology(namespace);
		// Resource
		// activiteResource=model.getResource("http://smag0.blogspot.fr/ontologies/Diamond#Activite");
		SimpleSelector selector = new SimpleSelector(procedureDiamondRessource,
				(Property) null, (RDFNode) null);
		// SimpleSelector selector = new SimpleSelector(null, (Property) null,
		// activiteResource);
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
				// recuperation des sous classes
				/*
				 * Resource objectResource=model.getResource(objectResultat);
				 * SimpleSelector selectorSubclasses = new
				 * SimpleSelector(objectResource, (Property) null, (RDFNode)
				 * null); StmtIterator iterSubclasses =
				 * model.listStatements(selectorSubclasses);
				 * System.out.println("liste sous classes de" +objectResource);
				 * int j = 0; while (iterSubclasses.hasNext()) { j++; Statement
				 * stmtSubclasses = iterSubclasses.nextStatement(); Resource
				 * subjectSub = stmtSubclasses.getSubject(); Property
				 * predicateSub = stmtSubclasses.getPredicate(); RDFNode
				 * objectSub = stmtSubclasses.getObject();
				 * System.out.println("Detail des sous classes de la méthode"
				 * +subjectSub + "\t" + predicateSub + "\t" + objectSub); }
				 */
			}
			System.out.println(subject + "\t" + predicate + "\t" + object);

			resultat += subject + " " + predicate + "\t" + object + "\n";
			JSONObject jresult = new JSONObject();
			jresult.put("subject", subject.getLocalName().toString());
			jresult.put("predicate", predicate.getLocalName().toString());
			jresult.put("object", objectResultat);
			System.out.println(jresult.toString());
			j.put(String.valueOf(i), jresult);
		}
		System.out.println(resultat);
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
		// System.out.println("########################\n"+namespace);
		return new MethodeSocket();
	}

	public class MethodeSocket implements WebSocket.OnTextMessage {
		private Connection connection;

		@Override
		public void onOpen(Connection connection) {
			this.connection = connection;
			System.out.println("Methode open");

		}

		@Override
		public void onClose(int closeCode, String message) {
			// TODO Auto-generated method stub
			System.out.println("Methode close");
		}

		@Override
		public void onMessage(String data) {
			if (data.equals("diamond")) {
				System.out.println("\n\nRecuperation de la methode" + data);
				System.out
						.println("récupération des classes principales de la méthode");
				try {
					// connection.sendMessage("reçu Methode"+data);
					// connection.sendMessage(resultat);
					connection.sendMessage(j.toString());
					// System.out.println("RESULTAT ENVOYE : "+resultat);
					System.out.println("RESULTAT ENVOYE en JSON : "
							+ j.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				System.out.println(data);
				// String etapeDiamondUri = namespace + data;
				// rechercher la ressource MethodeDiamond
				String etapeDiamondUri = namespace + data;
				Property smagHasPart = model.getProperty(namespace + "hasPart");

				// OK String queryString =
				// "select * where {<http://smag0.blogspot.fr/ontologies/Diamond#"+
				// data + "> ?propriete ?objet} ";
				String queryString = "PREFIX smag0: <" + namespace + "> "
						+ "select ?etape ?propriete ?objet where {<"
						+ etapeDiamondUri + "> ?propriete ?objet} ";
				System.out.println("\n\nRecuperation du detail de la méthode"
						+ queryString);
				Query query = QueryFactory.create(queryString);
				try (QueryExecution qexec = QueryExecutionFactory.create(query,
						model)) {
					ResultSet results = qexec.execSelect();
					for (; results.hasNext();) {
						QuerySolution soln = results.nextSolution();
						RDFNode propriete = soln.get("propriete");
						RDFNode x = soln.get("objet");
						Resource r = soln.getResource("etape");
						if (propriete == smagHasPart) {
							System.out.println(x.toString());
						} else {
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
							System.out.println(soln);
						}
					}
				}
			}
		}

	}

}
