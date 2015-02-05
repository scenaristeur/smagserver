package favedave.smag.projets;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.json.JSONObject;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
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
	// retourne la m�thode (Diamond ou Aspecs ) d'un projet
	// Create the Ontology Model
	OntModel model = ModelFactory.createOntologyModel();
	String diamondDefaultSource = "http://smag0.rww.io/diamond.owl";
	// Read the ontology file
	// model.begin();
	String resultat = "resultat vide";
	JSONObject j = new JSONObject();

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
		System.out.println("servlet Methode activ�e");
		Model m = FileManager.get().loadModel(diamondDefaultSource);

		if (m == null) {
			throw new IllegalArgumentException("File: " + diamondDefaultSource
					+ " not found");
		} else {
			model.add(m);
		}
		// model.read(in,"");
		// model.commit();

		// Get the base namespace
		String namespace = "http://smag0.blogspot.fr/ontologies/Diamond#";
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
			// TODO Auto-generated method stub
			System.out.println("Methode message" + data);
			try {
				// connection.sendMessage("re�u Methode"+data);
				// connection.sendMessage(resultat);
				connection.sendMessage(j.toString());
				// System.out.println("RESULTAT ENVOYE : "+resultat);
				System.out.println("RESULTAT ENVOYE en JSON : " + j.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
