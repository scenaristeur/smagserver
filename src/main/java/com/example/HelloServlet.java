package com.example;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.janusproject.kernel.Kernel;
import org.janusproject.kernel.address.Address;
import org.janusproject.kernel.agent.Kernels;

public class HelloServlet extends HttpServlet {

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ServletOutputStream out = resp.getOutputStream();
        
        AgentA a = new AgentA();
        AgentB b = new AgentB();
		Kernel k = Kernels.get();
		k.launchLightAgent(a,"Albert");
		k.launchLightAgent(b,"Bernardo");
		Address addressA = a.getAddress();
		String addresseA = addressA.toString();
		
		Address addressB = b.getAddress();
		String addresseB = addressB.toString();
		
        
        out.write("Developpement agents avec Janus-project".getBytes());
        out.write(addresseA.getBytes());
        out.write(addresseB.getBytes());
        
        out.flush();
        out.close();
        
    }
    
}
