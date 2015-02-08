package favedave.smag.projets;

import org.janusproject.kernel.channels.Channel;

public interface AgentDetailProjetChannel extends Channel {
	public String getDetails();

	public String getEtat();

}
