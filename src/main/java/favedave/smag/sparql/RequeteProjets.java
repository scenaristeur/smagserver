package favedave.smag.sparql;

import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;



/**
 * @author Simon Jupp
 * @date 11/09/2013
 * Functional Genomics Group EMBL-EBI
 *
 * Example of querying the Gene Expression Atlas SPARQL endpoint from Java
 * using the Jena API (http://jena.apache.org)
 *
 */
public class RequeteProjets {
  String sparqlEndpoint = "http://www.ebi.ac.uk/rdf/services/atlas/sparql";
String resultat="liste vide";
  // get expression values for uniprot acc Q16850
/*  String sparqlQuery = "" +
  "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
  "PREFIX atlasterms: <http://rdf.ebi.ac.uk/terms/atlas/>" +
  "SELECT distinct ?expressionValue ?pvalue \n" +
  "WHERE { \n" +
  "?value rdfs:label ?expressionValue . \n" +
  "?value atlasterms:pValue ?pvalue . \n" +
  "?value atlasterms:isMeasurementOf ?probe . \n" +
  "?probe atlasterms:dbXref ?uniprotAccession .\n" +
  "} \n" +
  "ORDER BY ASC(?pvalue)";
*/
  String url;

  public RequeteProjets() {
	  url="http://fuseki-smag0.rhcloud.com/ds/query?query=" +
			  "select+*+where+%7B%3Fs+%0D%0A%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23type%3E%3Chttp%3A%2F%2Fsmag0.blogspot.fr%2FNS%2FProjet%3E+%7D";

			  
	try {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		    HttpGet httpGet = new HttpGet( url ); 
		    //  ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(2); 
		    //    nameValuePairs.add(new BasicNameValuePair("q", "value1")); 
		    //   nameValuePairs.add(new BasicNameValuePair("param2", "value2"));
		    //   httpGet.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		    // println( "executing request: " + httpPost.getRequestLine() ); 
		   HttpResponse response = httpClient.execute( httpGet );
		   HttpEntity entity  = response.getEntity();

		    //text("----------------------------------------", 10, 10);
		    //text( response.getStatusLine().toString(), 10, 20);
		    //text("----------------------------------------", 10, 30);
		    if ( entity != null ) {
		      //   String reponse;
		      //  entity.writeTo( reponse );
		      // String reponse=new String(entity.getContent());
		      String reponse = new Scanner(entity.getContent(), "UTF-8").useDelimiter("\\A").next();
		      //text(reponse, 10, 100);
		      resultat=reponse;
		    }
		    //  if ( entity != null ) entity.consumeContent();

		    // When HttpClient instance is no longer needed,   
		    // shut down the connection manager to ensure 
		    // immediate deallocation of all system resources  
		    httpClient.getConnectionManager().shutdown(); 
		    
		      
		  } 
		  catch( Exception e ) { 
		    e.printStackTrace();
		   // err=e.toString();
		   // text(e.toString(), 100, 100);
		  }
   //   resultat=resultat+expressionValue+" "+pValue+"\n";
    
  }

public String getResultat() {
	// TODO Auto-generated method stub
	return this.resultat;
}
}