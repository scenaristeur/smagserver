package favedave.smag.projets;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.eclipse.jetty.websocket.WebSocket.Connection;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.util.FileManager;

import favedave.smag.projets.MethodeHtml5Servlet.MethodeSocket;

public class DoapHtml5Servlet extends WebSocketServlet{
	// retourne le doap d'un projet
	String doapDefaultSource="http://usefulinc.com/ns/doap#";
	  // FileManager.get().addLocatorClassLoader(main.class.getClassLoader());	
	    Model model= FileManager.get().loadModel(doapDefaultSource);
	    
	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
		model.write(System.out);
		System.out.println("servlet Doap activée");
		return new DoapSocket();
	}
public class DoapSocket implements WebSocket.OnTextMessage{
	private Connection connection;
	@Override
	public void onOpen(Connection connection) {
		this.connection=connection;
		System.out.println("servlet Doap open");
	}

	@Override
	public void onClose(int closeCode, String message) {
		System.out.println("servlet Doap close");
		
	}

	@Override
	public void onMessage(String data) {
		System.out.println("servlet Doap message"+data);
		try {
			connection.sendMessage("reçu Doap"+data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
}
