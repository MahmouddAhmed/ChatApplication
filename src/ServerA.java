import java.net.ServerSocket;
import java.net.Socket;

public class ServerA {
	
	private static ServerSocket welcomeSocket;

	public static void main(String args[]) throws Exception { 
		
		welcomeSocket = new ServerSocket(6789);
		
		while(true) { 
			Socket connectionSocket= welcomeSocket.accept();
			Thread t=new Thread(new ServerAR(connectionSocket));
			t.start();			
		}
		
	}
}
