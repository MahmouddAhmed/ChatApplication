import java.io.DataInputStream;

import javax.swing.JTextArea;

public class Recive implements Runnable{

	DataInputStream  inFromServer;
	JTextArea chat;
	
	public Recive(DataInputStream  inFromServer,JTextArea chat){
		this.inFromServer=inFromServer;
		this.chat=chat;
	}
	
	public void run() {
		while(true) {
			try {
				
					String recivedsentence=inFromServer.readUTF();
					chat.append(recivedsentence+"\n");
					
			} catch (Exception e) {
				System.exit(0);
			}
				
				
		}
	}
	
	

}
