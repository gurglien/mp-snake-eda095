package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Än så länge bara ett exempel på hur det här ska fungera. Bör väl se till att exceptions kastas ordentligt osv
 * @author oscar
 *
 */

public class MessageHandlerExample {


		private Socket socket; // the connection
		private Logger logWindow; // the log window

		private InputStream istream;
		private OutputStream ostream;
		private PrintWriter out;
		private BufferedReader reader;
	
	
		public MessageHandlerExample(Socket socket) {
			this.socket = socket;

			try {
				out = new PrintWriter(socket.getOutputStream(), true);
				istream = socket.getInputStream();
				ostream = socket.getOutputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			reader = new BufferedReader(new InputStreamReader(istream));
		}
		
		/*
		 * Send code defined in class Protocol
		 */
		public void sendCode(int code){
			
			out.println(code);
		}
		
		/*
		 * recieve code defined in class Protocol
		 */
		public int recieveCode() {
			int code = -1;
			try {
				code = Integer.parseInt(reader.readLine());
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return code;
			
		}
		
		/*
		 * Sends a String containing position information
		 * Using a string for position is only for example.
		 */
		public void sendPosition(String position){
			out.println(position);
		}
		
		/**
		 * Vetifan hur snyggt det är egentligen att använda bufferedreader. är det bättre att använda något annat?
		 * nu läses en line och returnerar. Viss overhead blir det ju men jag kan inte tänka mig att det spelar någon roll. 
		 * 
		 * FÖr tillfället får det vara så. ersätter kanske senare med att läsa bytevektor direkt från inputstreamen
		 */
		public String recievePosition(){
			String pos = null;
			try {
				pos = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return pos;
		}
	
	
}
