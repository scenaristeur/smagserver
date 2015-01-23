package favedave.smag;

import java.io.IOException;






import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.eclipse.jetty.websocket.WebSocket.Connection;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.query.ResultSetRewindable;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.resultset.JSONOutputResultSet;

import favedave.smag.projets.ProjetsHtml5Servlet.StockTickerSocket;

@ServerEndpoint("/testjson") 
public class TestJson extends WebSocketServlet{
 	String queryString = "select ?projet ?titre ?description where {"+
 	 		"?projet <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://smag0.blogspot.fr/ns/smag0#Projet> ."+
 	 		"?projet <http://purl.org/dc/elements/1.1/title> ?titre ."+
 	 		"?projet <http://purl.org/dc/elements/1.1/description> ?description ."+
 	 		"}"+
 	 		"ORDER BY DESC(?projet)"+
 	 		"LIMIT 200";
 	
        @OnMessage 
        public String handleMessage(String message){
                return "Thanks for the message: " + message;
        }

		@Override
		public WebSocket doWebSocketConnect(HttpServletRequest request,
				String protocol) {
			// TODO Auto-generated method stub
			System.out.println("On server");
			return new TestJsonSocket();
		}
		public class TestJsonSocket implements WebSocket.OnTextMessage{
			private Connection connection;
			private String message;
			private Query query;
			private ResultSet results;

			private JSONOutputResultSet  resultatsJson;
			private ResultSetRewindable resultats;
			private JSONObject j;
			@Override
			public void onOpen(Connection connection) {
				this.connection=connection;
				  query = QueryFactory.create(queryString);
				   QueryExecution qexec = QueryExecutionFactory.sparqlService("http://fuseki-smag0.rhcloud.com/ds/query", query);
				     try {
				          results = qexec.execSelect();
				          resultats=ResultSetFactory.copyResults(results);// duplicate pour garder après cloture de qexec
				        //  System.out.println(results);
				          j=new JSONObject();
				          int i=0;
						    // j=(JSONObject) resultats;
						    // j.put("JOON", "Hello");
						         while ( resultats.hasNext()) {
						     i++;
						        	 JSONObject jresult = new JSONObject();
						     QuerySolution result = resultats.next();
						     String projet = result.getResource("projet").getLocalName().toString();
						     String titre = result.getLiteral("titre").toString();
						     String description=result.getLiteral("description").toString();
						     jresult.put("projet", projet);
						     jresult.put("titre",titre);
						     jresult.put("description", description);
						     j.put(String.valueOf(i),jresult);
						     
						    System.out.println(i);
						     	         }
						        // OutputStream jsonResult = null;
								
				     }  finally {
				 	        qexec.close();
				 	     }
				     
						//System.out.println("1 "+results);
//pour debug 						//ResultSetFormatter.outputAsJSON(resultats);
						//System.out.println("2 "+resultats.getResultVars());
						//System.out.println("3 "+results.toString());
						//System.out.println("4 "+resultats);
					
						//	System.out.println(resultatsJson.toString());
				     
				   sendMessage(j.toString());
			}

			@Override
			public void onClose(int closeCode, String message) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onMessage(String data) {
				if (results != null){
					 try {
						// String resultatstring =  resultat.toString();
						connection.sendMessage(j.toString());

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
				sendMessage(data);
				}
			}



			private void sendMessage(String data) {
				if(connection==null||!connection.isOpen()){
					System.out.println("Connection is closed!!");
					return;
				}
				message=data;
				try {
					System.out.println("message retour : "+data);
					connection.sendMessage(message);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

}}