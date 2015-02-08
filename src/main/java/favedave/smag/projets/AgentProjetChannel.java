package favedave.smag.projets;

import org.janusproject.kernel.channels.Channel;

public interface AgentProjetChannel extends Channel {
	public String getProjetDetail();

	public String getProjetMethode();

	public String getProjetDoap();

	public String getEtat();

}
