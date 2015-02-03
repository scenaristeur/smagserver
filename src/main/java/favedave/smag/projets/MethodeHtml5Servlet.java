package favedave.smag.projets;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.eclipse.jetty.websocket.WebSocket.Connection;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Ontology;
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

public class MethodeHtml5Servlet extends WebSocketServlet{
	// retourne la méthode (Diamond ou Aspecs ) d'un projet
	//Create the Ontology Model
	OntModel model = ModelFactory.createOntologyModel();
	String diamondDefaultSource="http://smag0.rww.io/diamond.owl";
	//Read the ontology file
	//model.begin();
	String resultat="messageResultat";
	
	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
		System.out.println("servlet Methode activée");
		Model m = FileManager.get().loadModel(diamondDefaultSource);
		
		if (m == null) {
		    throw new IllegalArgumentException("File: " + diamondDefaultSource + " not found");
		}  else
		{
			model.add(m);
		}
//		model.read(in,"");
		//model.commit();
		
		//Get the base namespace
		String namespace = "http://smag0.blogspot.fr/ontologies/Diamond#";
		String procedureDiamondUri = namespace+"ProcedureDiamond";
		// rechercher la ressource MethodeDiamond
        Resource procedureDiamondRessource = model.getResource(procedureDiamondUri);
		//Ontology ontologie=model.getOntology(namespace);
       // Resource activiteResource=model.getResource("http://smag0.blogspot.fr/ontologies/Diamond#Activite");
        SimpleSelector selector = new SimpleSelector(procedureDiamondRessource, (Property) null, (RDFNode)null);
        //SimpleSelector selector = new SimpleSelector(null, (Property) null, activiteResource);
        StmtIterator iter = model.listStatements(selector);
        resultat+="\n";
        while(iter.hasNext()) {
           Statement stmt = iter.nextStatement();
           System.out.println(stmt.getPredicate().toString());
           System.out.println(stmt.getObject().toString());
           resultat+=   stmt.getPredicate().toString()+"\t"+stmt.getObject().toString()+"\n";
        }
       System.out.println(resultat);
		ExtendedIterator<OntClass> rootClasses = model.listHierarchyRootClasses();

while (rootClasses.hasNext()){
	OntClass rootClass = rootClasses.next();
	System.out.println("####RootClasse : "+rootClass.toString());
	if (namespace== null){
		 namespace= model.getNsPrefixURI(rootClass.toString());
		 System.out.println("NS :"+namespace);
	}
}

	//    model.write(System.out);
	 //   System.out.println("########################\n"+namespace);
		return new MethodeSocket();
	}
	public class MethodeSocket implements WebSocket.OnTextMessage{
		private Connection connection;
		@Override
		public void onOpen(Connection connection) {
			this.connection=connection;
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
			System.out.println("Methode message"+data);
			try {
				connection.sendMessage("reçu Methode"+data);
				 connection.sendMessage(resultat);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

}
