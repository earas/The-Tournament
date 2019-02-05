
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Main {
	

	static Scanner scan = new Scanner(System.in);
	static InetAddress IP = null;
	
	public static void main(String[] args) {
	
		try {			
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress("google.com", 80));
			IP = socket.getLocalAddress();
			socket.close();					
		} catch (IOException e) {			
			e.printStackTrace();
		}
					
		welcomeConversation();
		
		
	}
	
	
	public static void welcomeConversation() {
						
		System.out.println("[0] - Join a Tournament");
		System.out.println("[1] - Start a Tournament");		
				
		int choice = scan.nextInt();
		if(choice == 0) {
			System.out.println("enter your name:");
			String name = scan.next();
			String ipAgent = IP.getHostAddress();
			Agent agent = new Agent(name,ipAgent);
			agent.start();
			
			System.out.println("enter introducing agent ip:");
			String ip = scan.next();
			System.out.println("enter introducing agent port:");
			int port = scan.nextInt();
			agent.sendMessage("JOIN", ip, port);
									
		}
		else if(choice == 1) {
			
			System.out.println("enter your name:");
			String name = scan.next();
			String ip = IP.getHostAddress();
			Agent introAgent = new Agent(name,ip);
			introAgent.start();	
						
		}
		else {
			System.out.println("Not exceptable value");
			welcomeConversation();
		}
	
		
	}
	
	
}
