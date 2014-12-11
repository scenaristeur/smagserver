package com.example;

import org.janusproject.kernel.address.AgentAddress;
import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.message.Message;
import org.janusproject.kernel.message.StringMessage;
import org.janusproject.kernel.status.Status;

public class AgentA extends Agent {
	  private static enum State {
	     PRESENTATION, WAIT_FOR_WELCOME;
	  }
	  private State state;
	  public Status activate(Object... parameters) {
	    this.state = State.PRESENTATION;
	    print ("Je me présente");
	    return null;
	  }
	  public Status live() {
	    switch(this.state) {
	    case PRESENTATION:
	      broadcastMessage(new StringMessage("hello") );
	      print("j'envoie un message hello");
	      this.state = State.WAIT_FOR_WELCOME;
	      break;
	    case WAIT_FOR_WELCOME:
	    	print ("j'attend en retour un message de bienvenue");
	      for(Message m : getMessages()) {
	    	  print ("Oh! j'ai des messages!");
	    	// print (m);
	         if (m instanceof StringMessage &&
	             ((StringMessage)m).getContent().equals("welcome")) {
	           // Someone welcome me
	        	 AgentAddress a = (AgentAddress)m.getSender();
	        	 print ("Oh! j'ai un message qui dit '"+((StringMessage)m).getContent()+"' en provenance de "+a+" !");
	           killMe();
	        	 print ("c'est bon, le test est terminé, je peux me tuer... ARRggggHHH !!!");
	         }
	      }
	      break;
	    }
	    return null;
	  }
	}