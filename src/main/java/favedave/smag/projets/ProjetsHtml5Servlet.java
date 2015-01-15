package favedave.smag.projets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.janusproject.kernel.Kernel;
import org.janusproject.kernel.agent.ChannelManager;
import org.janusproject.kernel.agent.Kernels;

import favedave.smag.jena.sparql.AgentStateChannel;
import favedave.smag.jena.sparql.JenaRequeteProjets;

public class ProjetsHtml5Servlet extends WebSocketServlet {

	private static final long serialVersionUID = 1L;
        
        private int i = 1;
        AgentStateChannel channel;

	public WebSocket doWebSocketConnect(HttpServletRequest req, String resp) {
		System.out.println("On server");
	     JenaRequeteProjets  jenarequeteprojets=new JenaRequeteProjets();//??? pb avec Jena
			Kernel k = Kernels.get();
			k.launchLightAgent(jenarequeteprojets,"JenaRequeteProjets");
			System.out.println("Agent Jena Requete Projet créé");
			//recuperation des infos de l'agent
		    // Get the channel manager of the kernel.
		    ChannelManager channelManager = k.getChannelManager();
		 
		    // Ask for a channel for interacting with the agent.
		    // The first parameter is the agent to interact with
		    // The second parameter is the type of the channel to use.
		    channel = jenarequeteprojets.getChannel( AgentStateChannel.class);
		return new StockTickerSocket();
	}
	protected String getMyJsonTicker(){
		return "{\"hello\": \"projets " + i++ +  "\"}";
	}
	public class StockTickerSocket implements WebSocket.OnTextMessage{
		private Connection connection;
		private Timer timer; 


		@Override
		public void onClose(int arg0, String arg1) {
			System.out.println("Web socket closed!");
		}

		@Override
		public void onOpen(Connection connection) {
			this.connection=connection;
			this.timer=new Timer();
		}

		@Override
		public void onMessage(String data) {
			if(data.indexOf("disconnect")>=0){
				connection.close();
				timer.cancel();
			}else{
				sendMessage();

			}			
		}

		private void sendMessage() {
			if(connection==null||!connection.isOpen()){
				System.out.println("Connection is closed!!");
				return;
			}
			timer.schedule(new TimerTask() {
					
					private Object exFirstAtt;
					private String firstAtt;

					@Override
					public void run() {
						try{
						//	System.out.println("Running task");
						//	connection.sendMessage(getMyJsonTicker());
						    // Check if the agent accept to interact
						    if (channel!=null) {
						      // Display the agent's state
						      System.out.println("First attribute is "+
						           channel.getFirstAttribute());
						      //retour de l'attribut
						     exFirstAtt=firstAtt;
						   firstAtt="Retour\n"+channel.getFirstAttribute()+"\n"+channel.getEtat()+"\n"+channel.getResultat();
						   // firstAtt=channel.getResultat();
						     if (exFirstAtt!=firstAtt){
						      connection.sendMessage(firstAtt);
						     }
						      System.out.println("Second attribute is "+
							           channel.getSecondAttribute());
						    }
						    else {
						      System.err.println("The agent does not accept to interact");
						    }
							
							
						}
						catch (IOException e) {
							e.printStackTrace();
						}
					}
				}, new Date(),5000);
		}
		
	}
}
