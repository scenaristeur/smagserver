package favedave.smag;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.janusproject.kernel.Kernel;
import org.janusproject.kernel.address.Address;
import org.janusproject.kernel.agent.Kernels;

import org.eclipse.jetty.server.Server;
 
public class HelloServlet extends HttpServlet
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
		Kernel k = Kernels.get();
		k.launchLightAgent(a,"Albert");
		k.launchLightAgent(b,"Bernardo");
		Address addressA = a.getAddress();
		String addresseA = addressA.toString();
		
		Address addressB = b.getAddress();
		String addresseB = addressB.toString();
		
     
		//response.getWriter().println("Developpement agents avec Janus-project".getBytes());
		response.getWriter().println("<h3>"+addresseA+"</h3");
		response.getWriter().println("<h3>"+addresseB+"</h3");

     
 }
    }
   
