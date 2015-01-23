package favedave.smag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.eclipse.jetty.websocket.WebSocket.Connection;

import favedave.smag.projets.ProjetsHtml5Servlet.StockTickerSocket;

@ServerEndpoint("/testjson") 
public class CopyOfTestJson extends WebSocketServlet{

        @OnMessage 
        public String handleMessage(String message){
                return "Thanks for the message: " + message;
        }

		@Override
		public WebSocket doWebSocketConnect(HttpServletRequest request,
				String protocol) {
			// TODO Auto-generated method stub
			System.out.println("On server");
			return new TestJsonSocket();
		}
		public class TestJsonSocket implements WebSocket.OnTextMessage{
			private Connection connection;
			private String message;
			@Override
			public void onOpen(Connection connection) {
				this.connection=connection;
				
			}

			@Override
			public void onClose(int closeCode, String message) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onMessage(String data) {
				
				sendMessage(data);
			}

			private void sendMessage(String data) {
				if(connection==null||!connection.isOpen()){
					System.out.println("Connection is closed!!");
					return;
				}
				message=data;
				try {
					System.out.println("message retour : "+data);
					connection.sendMessage("Thanks for the message: " + message);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

}}