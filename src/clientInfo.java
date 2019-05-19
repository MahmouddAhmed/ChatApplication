import java.net.Socket;

public class clientInfo {
	private String name;
	static int id;
	private Socket socket;
	
	public clientInfo(Socket socket,String name) {
		id=id++;
		this.socket=socket;
		this.name=name;
		
	}

	public int getId() {
		return id;
	}

	public Socket getSocket() {
		return socket;
	}
	public String getname() {
		return name;
	}
	
	public String toString() {
		return name;
	}

}
