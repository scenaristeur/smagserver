package org.ajeesh.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

public class Html5Servlet extends WebSocketServlet {

	private static final long serialVersionUID = 1L;
        
        private int i = 1;

	public WebSocket doWebSocketConnect(HttpServletRequest req, String resp) {
		System.out.println("On server");
		return new StockTickerSocket();
	}
	protected String getMyJsonTicker(){
		return "{\"hello\": \"world " + i++ +  "\"}";
	}
	public class StockTickerSocket implements WebSocket.OnTextMessage{
		private Connection connection;
		private Timer timer; 


		@Override
		public void onClose(int arg0, String arg1) {
			System.out.println("Web socket closed!");
		}

		@Override
		public void onOpen(Connection connection) {
			this.connection=connection;
			this.timer=new Timer();
		}

		@Override
		public void onMessage(String data) {
			if(data.indexOf("disconnect")>=0){
				connection.close();
				timer.cancel();
			}else{
				sendMessage();

			}			
		}

		private void sendMessage() {
			if(connection==null||!connection.isOpen()){
				System.out.println("Connection is closed!!");
				return;
			}
			timer.schedule(new TimerTask() {
					
					@Override
					public void run() {
						try{
							System.out.println("Running task");
							connection.sendMessage(getMyJsonTicker());
						}
						catch (IOException e) {
							e.printStackTrace();
						}
					}
				}, new Date(),5000);
		}
		
	}
}
