import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class ServerAR implements Runnable{
	Socket socket;
	static ArrayList<clientInfo> abc=new ArrayList<clientInfo>();
	
	
	
	public ServerAR(Socket socket)  
	{
		this.socket=socket;
	
	}
	public void run() {
		while(true) {
		try {
		BufferedReader inFromClient= new BufferedReader(new InputStreamReader(socket.getInputStream())); 
		String clientSentence = inFromClient.readLine();

		if(clientSentence.contains("FROMOTHERSERVER")) {
			
			int i;
			String cn="";
			for( i=clientSentence.length()-1;i>-1;i--) {
				if(clientSentence.charAt(i)=='F')
					break;
			}
			
			for(i=i-1;i>-1;i--) {
				if(clientSentence.charAt(i)=='#')
					break;
				else
					cn=clientSentence.charAt(i)+cn;
			}
			clientSentence=clientSentence.substring(0,i);
			
			Socket socket2 =null;
			int j;
			for(j=0;j<abc.size();j++) {
				if(abc.get(j).getname().equals(cn)) {
					socket2=abc.get(j).getSocket();
					break;
				}
			}
			if(j==abc.size()) {
				DataOutputStream X= new DataOutputStream(socket.getOutputStream());
				X.writeUTF("The Client doesnt Exist or is offline");
			}
			else {
				DataOutputStream  outToClient = new DataOutputStream(socket2.getOutputStream());
				DataOutputStream X= new DataOutputStream(socket.getOutputStream());
				X.writeUTF("Message Sent Succefully");
				
				outToClient.writeUTF(clientSentence);
				
			}
			socket.close();
			return;
		}
		
		
		
		
		else if(clientSentence.equals("MEMBERS")) {
			DataOutputStream X= new DataOutputStream(socket.getOutputStream());
			Socket SS=new Socket("localhost",6788);
			DataInputStream IFS =new DataInputStream(SS.getInputStream());
			DataOutputStream OTS=new DataOutputStream(SS.getOutputStream());
			OTS.writeBytes("MEMBERS2"+"\n");
			String res=IFS.readUTF();
			String res2="";
			for(int i=0;i<abc.size();i++)
				res2+=abc.get(i).getname()+",";
			X.writeUTF(res2+res);;
			SS.close();
			socket.close();
			return;
		}
		else if(clientSentence.equals("MEMBERS2")) {
			DataOutputStream X= new DataOutputStream(socket.getOutputStream());
			String res="";
			for(int i=0;i<abc.size();i++)
				res+=abc.get(i).getname()+",";
			X.writeUTF(res+"\n");
			socket.close();
			return;
		}
		
		
		
		else if(clientSentence.contains("ADD")&&!clientSentence.contains("#")) {
			String n=clientSentence.substring(3);
			clientInfo A=new clientInfo(this.socket,n);
			abc.add(A);
			
		}
		
		else if(clientSentence.equals("ffdfdsfkjhdsnd")) {
			int i;
			for(i=0;i<abc.size();i++) {
				if(abc.get(i).getSocket()==this.socket)
					break;
				
			}
			abc.remove(i);
			throw new Exception();
		}
		
		
		else {
			
		int i;
		String SentTo="";
		String TTL1="";
		int TTL;
		for(i=clientSentence.length()-1;i>=0;i--) {
			if(clientSentence.charAt(i)=='#')
				break;
			else
				SentTo=clientSentence.charAt(i)+SentTo;
		}
		clientSentence=clientSentence.substring(0,i);
		i--;
		while(i>=0) {
			
			if(clientSentence.charAt(i)=='#')
				break;
			else
				TTL1=clientSentence.charAt(i)+TTL1;
		i--;
		}
		
		TTL=Integer.parseInt(TTL1);
		
		if(TTL==0) {
			DataOutputStream X= new DataOutputStream(socket.getOutputStream());
			X.writeUTF("The Client doesnt Exist or is offline");
			continue;
		}
		
		clientSentence=clientSentence.substring(0,i);
		Socket socket2 =null;
		int j;
		for(j=0;j<abc.size();j++) {
			if(abc.get(j).getname().equals(SentTo)) {
				socket2=abc.get(j).getSocket();
				break;
			}
			
		}
		TTL+=-1;
		if(j==abc.size()) {
			
			if(TTL==0) {
				DataOutputStream X= new DataOutputStream(socket.getOutputStream());
				X.writeUTF("The Client doesnt Exist or is offline");
				continue;
			}
			
			Socket SS=new Socket("localhost",6788);
			DataInputStream IFS =new DataInputStream(SS.getInputStream());
			DataOutputStream OTS=new DataOutputStream(SS.getOutputStream());
			OTS.writeBytes(clientSentence+"#"+SentTo+"FROMOTHERSERVER"+"\n");
			String res=IFS.readUTF();
			DataOutputStream X= new DataOutputStream(socket.getOutputStream());
			X.writeUTF(res);
			SS.close();
			
			continue;
		}
		
		
		DataOutputStream  outToClient = new DataOutputStream(socket2.getOutputStream());
		DataOutputStream  outToUser=new DataOutputStream(socket.getOutputStream());
		outToUser.writeUTF("Message Sent Sucessfully");
		
		outToClient.writeUTF(clientSentence);
		
		}
		}
		catch(Exception E) {		 
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		}
		
	}
		
	
}


