package favedave.smag.jena.sparql;

import java.util.Collection;

import org.janusproject.kernel.channels.Channel;

public interface AgentStateChannel extends Channel {
	 
	  /** Replies the value of the first attribute of the agent.
	   */
	  public int getFirstAttribute();
	 
	  /** Replies the value of the second attribute of the agent.
	   */
	  public Collection<Object> getSecondAttribute();
	  
	  public String getEtat();

	public String getResultat();
	 
	}