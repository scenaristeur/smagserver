package favedave.smag0.agents.channels;

import org.janusproject.kernel.channels.Channel;
import org.json.JSONObject;

public interface AgentProjetChannel extends Channel {

	public String getEtat();

	public String getMessageUtilisateur();

	public JSONObject getProjetsSimilaires(String projet);

}
