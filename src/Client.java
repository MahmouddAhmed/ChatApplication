import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame implements ActionListener{
	Socket clientSocket;
	DataInputStream inFromServer;
	DataOutputStream outToServer;
	String Clientname;
	String SendTo;
	List<String> members = new ArrayList<String>();
	JButton Send;
	JComboBox Clients;
	JTextArea chat;
	JTextField messages;
	JButton refresh;
	int TTL=1;
	int portnumber;
	JButton OK;
	JLabel curname;
	JScrollPane scroll;
	public void chat(String Source,String Destination,int TTL,String Message) throws IOException {
		Message="From "+Source+" :"+Message;
		Message+="#"+TTL+"#"+Destination;
		outToServer.writeBytes(Message+ '\n');
	}
	
	public Client(int portnumber) throws UnknownHostException, IOException{
		super();
		this.setSize(800, 700);
		this.setVisible(true);
		this.setLayout(null);
		this.setTitle("Chat Application");
		Send= new JButton("Send");
		Send.addActionListener(this);
		Send.setBounds(550, 490, 100, 20);
		Send.setVisible(false);
		this.getContentPane().add(Send);
		Clients=new JComboBox();
		Clients.setBounds(50, 150, 100, 30);
		Clients.addActionListener(this);
		this.getContentPane().add(Clients);
		chat=new JTextArea();
		chat.setBounds(400, 50, 250, 400);
		chat.setEditable(false);
		scroll =new JScrollPane (chat);
		scroll.setBounds(400, 50, 250, 400);
		this.getContentPane().add(scroll);
		//this.getContentPane().add(chat);
		messages=new JTextField();
		messages.setBounds(400,490, 150, 20);
		this.add(messages);
		refresh=new JButton("Refresh");
		refresh.addActionListener(this);
		refresh.setBounds(50, 50, 150, 50);
		this.add(refresh);
		OK= new JButton("OK");
		OK.addActionListener(this);
		OK.setBounds(550, 490, 100, 20);
		this.add(OK);
		
		clientSocket=new Socket("localhost",portnumber);
		inFromServer =new DataInputStream(clientSocket.getInputStream());
		outToServer=new DataOutputStream(clientSocket.getOutputStream());
		this.portnumber=portnumber;
		GetMemberList();
		chat.append("Please Enter Your Name"+"\n");
		curname=new JLabel();
		curname.setBounds(50, 200, 150, 20);
		this.add(curname);
		
		
		
		
		
	}
	
	public  void GetMemberList() {
		try {
			Socket MS= new Socket("localhost",portnumber);
			DataInputStream inFromServerM= new DataInputStream(MS.getInputStream());
			DataOutputStream outToServerM=new DataOutputStream(MS.getOutputStream());
			outToServerM.writeBytes("MEMBERS"+"\n");
			String S=inFromServerM.readUTF();
			String [] tmp=S.split(",");
			members=Arrays.asList(tmp);
			MS.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	public  void  Join(String name) throws IOException {
		
		
		while(true) {
			GetMemberList();
			if(name.equals("")||name==null) {
				chat.append("Please Enter A Username"+"\n");
				return;
			}
			
			else if(members.contains(name)) {
				chat.append("The username is already taken please choose another one"+"\n");
				return;
				
			}
			else {
				outToServer.writeBytes("ADD"+name+"\n");
				chat.append("connection Sucesfull"+"\n");
				OK.setVisible(false);
				Send.setVisible(true);
				Thread R=new Thread(new Recive(inFromServer,chat));
				R.start();
				break;
				
				
			}
				
		}
		refresh();
		curname.setText("Your username :"+name);
		this.Clientname=name;
	}
 
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource()==OK) {
			String name=messages.getText();
			try {
				Join(name);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		if(arg0.getSource()==Clients)
			SendTo=(String) ((JComboBox) arg0.getSource()).getSelectedItem();
		if(arg0.getSource()==refresh)
			refresh();
		
		
		if(arg0.getSource()==Send) {
			try {
				String inFromUser=messages.getText();
				chat.append("To :"+SendTo+":"+inFromUser+"\n");
				String sentence=inFromUser;
				if(sentence.equals("Bye")||sentence.equals("Quit")) {
					outToServer.writeBytes("ffdfdsfkjhdsnd");
					clientSocket.close();
					System.exit(0);
					
					
				}
				
				chat(Clientname,SendTo,TTL,sentence);
				
			}
			catch (Exception e) {
				e.printStackTrace();
			
			}
		}
		if(arg0.getSource()==Clients) {
			
		}
		
	}
	
	public void refresh() {
		GetMemberList();
		Clients.setModel(new DefaultComboBoxModel(members.toArray()));
	}
	

	public static void main(String argv[]) throws UnknownHostException, IOException  { 
		int portnumber=6788;
		//int portnumber=6789;
		new Client(portnumber);
		
	}

	

	

}
