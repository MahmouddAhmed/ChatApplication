import java.net.ServerSocket;
import java.net.Socket;

public class ServerB {
	
	private static ServerSocket welcomeSocket;

	public static void main(String args[]) throws Exception { 
		
		welcomeSocket = new ServerSocket(6788);
		
		while(true) { 
			Socket connectionSocket= welcomeSocket.accept();
			Thread t=new Thread(new ServerBR(connectionSocket));
			t.start();			
		}
		
	}
}
