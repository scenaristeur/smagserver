package favedave.smag;

import org.janusproject.kernel.address.AgentAddress;
import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.message.Message;
import org.janusproject.kernel.message.StringMessage;
import org.janusproject.kernel.status.Status;

public class AgentB extends Agent {
	  public Status activate(Object... parameters) {
		    print ("Je me presente");
		    return null;
		  }
	  public Status live() {
	    for(Message m : getMessages()) {
	    	print ("Tiens ! j'ai du courrier!");
	      if (m instanceof StringMessage &&
	          ((StringMessage)m).getContent().equals("hello")) {
	        replyToMessage(m, new StringMessage("welcome"));
	        AgentAddress a = (AgentAddress)m.getSender();
	        print ("J'ai recu un message qui dit 'hello' en provenance de "+a);
	        print ("Je lui repond 'welcome'");
	      }
	    }
	    return null;
	  }
	}