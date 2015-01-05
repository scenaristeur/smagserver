package favedave.smag;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.janusproject.kernel.Kernel;
import org.janusproject.kernel.address.Address;
import org.janusproject.kernel.agent.AgentLifeState;
import org.janusproject.kernel.agent.Kernels;

import favedave.smag.jena.sparql.JenaRequeteProjets;
import favedave.smag.sparql.RequeteProjets;



public class ProjetsServlet extends HttpServlet
{
	 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	    {
	        response.setContentType("text/html");
	        response.setStatus(HttpServletResponse.SC_OK);
	        response.getWriter().println("<h1>Creation de deux agents !</h1>");
	        response.getWriter().println("session=" + request.getSession(true).getId());
	        
	        /* Agents -*/
	        

	    // ServletOutputStream out = response.getOutputStream();
	     
	     AgentA a = new AgentA();
	     AgentB b = new AgentB();
	     JenaRequeteProjets  jenarequeteprojets=new JenaRequeteProjets();//??? pb avec Jena
			Kernel k = Kernels.get();
			k.launchLightAgent(a,"Albert");
			k.launchLightAgent(b,"Bernardo");
			k.launchLightAgent(jenarequeteprojets,"JenaRequeteProjets");
			Address addressA = a.getAddress();
			String addresseA = addressA.toString();
			
			Address addressB = b.getAddress();
			String addresseB = addressB.toString();
			
	     
			//response.getWriter().println("Developpement agents avec Janus-project".getBytes());
			response.getWriter().println("<h3>"+addresseA+"</h3>");
			response.getWriter().println("<h3>"+addresseB+"</h3>");
			response.getWriter().println("<h3>Liste des projets récupérée par l'agent Projets sur \n"+
			"http://fuseki-smag0.rhcloud.com/</h3>");
			String resultat = null;
			//RequeteProjets requeteProjets= new RequeteProjets();
			//while(resultat.isEmpty()){
			//	resultat=requeteProjets.getResultat();
			//}
			response.getWriter().println("<div>"+resultat+"</div>");

	 }
}
