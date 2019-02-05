
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;



public class Agent extends Thread {
	
	 private ServerSocket server;	 
	 private int port, currentPlayerCounter = 1, number = 0, opPort; 
	 private String name, ip, opName, opIp;;	 
	 private boolean available = true, waitingFor = false, duelStarted = false;
	 private List<Player> allPlayers = new ArrayList<>();;
	 private List<Player> oldCompetitor  = new ArrayList<>();
	 private List<Player> futureCompetitor = new ArrayList<>();
	 private List<Player> pendingOps = new ArrayList<>();
	 private List<Summary> mySummary = new ArrayList<>();
	 private Set<String> uniquePlayers = new HashSet<String>();
	 private Scanner scan = new Scanner(System.in);
	 private Player p1;
	 
	public Agent(String name, String ip){
		
		this.name = name;
		this.ip = ip;
		try {
            server = new ServerSocket(0);
            port = server.getLocalPort();
            System.out.println("ip: "+ip);
            System.out.println("port: "+port);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		
		p1 = new Player (this.name,this.ip,this.port);
		
		
		allPlayers.add(p1);
		
	}
	
	

	@Override
    public void run(){
        Socket clientSocket;
        Message message = null;
	
        try{
        while((clientSocket = server.accept()) != null){
            InputStream is = clientSocket.getInputStream();      
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            
            String line = br.readLine();
            if(line != null){
            	String[] msg = line.split("&");	
            	
            	if(line.startsWith("JOIN")) {
            		
            	
                String mess = "Accepted&You are accepted in Tournament now";
                broadcast(msg[1]+" joined the game");
        		allPlayers.add(new Player (msg[1],msg[2],Integer.parseInt(msg[3])));
        		uniquePlayers.add(msg[1]);
                message = new Message(mess,msg[2],Integer.parseInt(msg[3]));
				message.run();
				
				startConditions();
				
                
            	}
            	else if(msg[0].equalsIgnoreCase("Accepted")) {
                    System.out.println(msg[1]);
                    duelStarted = true;
                    currentPlayerCounter = 10;
                    
                	}
            	else if(msg[0].equalsIgnoreCase("AddF")) {
            				
            		futureCompetitor.add(new Player(msg[1],msg[2],Integer.parseInt(msg[3])));                 		
            			
            		System.out.println(futureCompetitor);
            		
            	}
            	else if(msg[0].equalsIgnoreCase("AddAll")) {
            			
            			if(!uniquePlayers.contains(msg[1])) {
            				allPlayers.add(new Player(msg[1],msg[2],Integer.parseInt(msg[3])));
            				
            				uniquePlayers.add(msg[1]);
            			}
            	          	
            		
            	}
            	else if(msg[0].equalsIgnoreCase("duelstarted!")) {
            		duelStarted = true;            		
            	}
            	else if(msg[0].equalsIgnoreCase("DUEL!")) {
            		if(available && futureCompetitor.size()!=0) {           			 
                		matchup();
                		}
            	}
            	else if(msg[0].equalsIgnoreCase("shallWeDuel?")) { 
            		
            		if(available && !waitingFor) {
            			
            			message = new Message("YesDuel!",msg[2],Integer.parseInt(msg[3]));
            			message.run();
            			available=false;
            			opIp = msg[2];
            			opPort = Integer.parseInt(msg[3]);
            			opName = msg[1];
            			
            		}
            		else if(available && waitingFor) {
            			
            			boolean exist = false;
			
            			for(Player p : pendingOps) {
            				if(p.getName().equalsIgnoreCase(msg[1])) {
            					exist = true;           					
            				}
            				        				
            			}
            			if(exist) {           				
            			message = new Message("YesDuel!",msg[2],Integer.parseInt(msg[3]));
            			message.run();
            			available=false;
            			opIp = msg[2];
            			opPort = Integer.parseInt(msg[3]);
            			opName = msg[1];          			           			
            			}
            			else {
            				message = new Message("NoDuel!",msg[2],Integer.parseInt(msg[3]));
                			message.run();
            			}
            			
            		}
            		else if(available == false){
            			message = new Message("NoDuel!",msg[2],Integer.parseInt(msg[3]));
            			message.run();
            			
            		}
            	}
            	else if(msg[0].equalsIgnoreCase("YesDuel!")) {           		
            		Duel newDuel = new Duel(true);
            		available = false;          		
            	}
            	else if(msg[0].equalsIgnoreCase("NoDuel!")) {           		
            		Duel newDuel = new Duel(false);
            	}
            	else if(msg[0].equalsIgnoreCase("DellOp!")) {
 		  
         		   for(Player p : futureCompetitor) {
         			   if(p.getName().equalsIgnoreCase(msg[1])) {
         				  oldCompetitor.add(p);
         			   }
         		   }
         		   
         		for(Iterator<Player> futureiterator = futureCompetitor.iterator(); futureiterator.hasNext();) {
         			Player p = futureiterator.next();
         			if(p.getName().equalsIgnoreCase(msg[1])) {
         				
         				futureiterator.remove();
         			}
         		}
        
         		if(futureCompetitor.size() == 0 && pendingOps.size() == 0) {
         			System.out.println("You duelled all your opponents. do you want to quit? or wait for new potantial players?");
         			String close = scan.next();
         			if(close.equalsIgnoreCase("QUIT")) {
         				callSummary();
         				broadcast("Dellme&"+p1.getName());
         				
         				try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							//e.printStackTrace();
						}
         				System.exit(-1);
         				
         			
         			}
         			else {
         				System.out.println("Waiting for new players...");
         			}
         		}
         		
         		
         	}
            	else if(msg[0].equalsIgnoreCase("WaitReciever!")) {
            		
            		boolean exist = false;
            		for(Player p : pendingOps) {
            			if(p.getName().equalsIgnoreCase(msg[1])) {
            				exist = true;
            			}
            		}
            		
            		for(Player p : oldCompetitor) {
            			if(p.getName().equalsIgnoreCase(msg[1])) {
            				exist = true;
            			}
            		}
            		
            		if(!exist) { 		       		
            		pendingOps.add(new Player(msg[1],msg[2],Integer.parseInt(msg[3])));           		
            		}
            	}
            	
            	else if(msg[0].equalsIgnoreCase("makeAvailable!")) {
            		available = true;
            	}
            	else if(msg[0].equalsIgnoreCase("makeDisable!")) {
            		available = false;
            	}
            	else if(msg[0].equalsIgnoreCase("waitForOff!")) {
            		waitingFor = false;
            	}
            	else if(msg[0].equalsIgnoreCase("waitForOn!")) {
            		waitingFor = true;
            	}
            	
            	else if(msg[0].equalsIgnoreCase("deletePending!")) {
            		
   		         for(Iterator<Player> pendingiterator = pendingOps.iterator(); pendingiterator.hasNext();) {
   		        	 Player p = pendingiterator.next();
   		        	 if(p.getName().equalsIgnoreCase(msg[1])) {   		        	
   		        		 pendingiterator.remove();
   		        	 }
   		         }

   		         if(pendingOps.size()==0)
   		        	 waitingFor = false;     
            	}
            	else if(msg[0].equalsIgnoreCase("started!")) {
            		 		duel(); 		
            	}
            	else if(msg[0].equalsIgnoreCase("key")) {
            		
            		Security sec = new Security();
            		String newNum = sec.decrypt(msg[1]);
            	
            		number = Integer.parseInt(newNum);
            			
    		 		
            	}
            	else if(msg[0].equalsIgnoreCase("keyAnswer")) {
            		Security sec = new Security();
            		String newNum = sec.encrypt(msg[1]);
            		
            		Duel newDuel = new Duel(newNum);		 		
            	}           
            	else if(msg[0].equalsIgnoreCase("Dellme")) {
            		
            		for(Iterator<Player> alliterator = allPlayers.iterator(); alliterator.hasNext();) {
             			Player p = alliterator.next();
             			if(p.getName().equalsIgnoreCase(msg[1])) {
             				
             				alliterator.remove();
             			}
             		}
            		
            		
        		
        		
            	}
            	else if(msg[0].equalsIgnoreCase("winner!")) {
            				 System.out.println("You won against "+msg[4]);	
            				 System.out.println("My number: "+msg[2]+" | Op number: "+msg[3]+" | target number: "+msg[1]);          				 
            				 mySummary.add(new Summary(p1.getName(),msg[4],Integer.parseInt(msg[1]),Integer.parseInt(msg[2]),Integer.parseInt(msg[3]),"Win"));
            	}
            	else if(msg[0].equalsIgnoreCase("looser!")) {
            		System.out.println("You lost against "+msg[4]);		
            		System.out.println("My number: "+msg[2]+" | Op number: "+msg[3]+" | target number: "+msg[1]);
            		mySummary.add(new Summary(p1.getName(),msg[4],Integer.parseInt(msg[1]),Integer.parseInt(msg[2]),Integer.parseInt(msg[3]),"Lose"));
            	}
            	
            
            	else {
            		System.out.println(line);
            	}     
            }

        }
        } catch (IOException e) {
                e.printStackTrace();
            }
        }
	

	
	public void duel() {
		System.out.println("Duel started between you and "+opName);
		Thread duelling = new Thread(new Runnable() {
			 @Override
	         public void run() {
				 System.out.println("Wait the number from your opponent...");
				 while(true) {
					 if(number != 0)
					 break;
					 try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
				 }
				 System.out.println("number has been taken from your oppenent");
				 System.out.println("Your turn! enter your number: ");
				 int mynumber = scan.nextInt();
				 String key ="keyAnswer&"+mynumber;
				 
				 Message msg = new Message(key,opIp,opPort);
				 msg.run();	
				 
					}
			 }); 
		
		duelling.start();
		
		number = 0;
	}
	
	
	
	public void startConditions() {
		currentPlayerCounter++;
		
		if(duelStarted) {		
				Player p = allPlayers.get(allPlayers.size()-1);
				for(Player allP : allPlayers) {
				if(!allP.getName().equalsIgnoreCase(p.getName())) {
					
				sendPlayers(p,"AddF",allP.getIp(),allP.getPort());
				sendPlayers(allP,"AddF",p.getIp(),p.getPort());
				
				sendPlayers(p,"AddALL",allP.getIp(),allP.getPort());
				sendPlayers(allP,"AddALL",p.getIp(),p.getPort());
				
				}
			}
			Message newMsg = new Message("DUEL!",p.getIp(),p.getPort());
			newMsg.run();	
			
			
		}
		else if(currentPlayerCounter < 2) {
			System.out.println("first counter entered......");
			Player p = allPlayers.get(allPlayers.size()-1);
			for(Player allP : allPlayers) {
				if(!allP.getName().equalsIgnoreCase(p.getName())) {
					
				sendPlayers(p,"AddF",allP.getIp(),allP.getPort());
				sendPlayers(allP,"AddF",p.getIp(),p.getPort());
				
				sendPlayers(p,"AddALL",allP.getIp(),allP.getPort());
				sendPlayers(allP,"AddALL",p.getIp(),p.getPort());
				
				
				}
			}		
			broadcast("new player added but not enough to start");
		}
		
		else {
			//System.out.println("else entered........");
			Player p = allPlayers.get(allPlayers.size()-1);
			for(Player allP : allPlayers) {
				if(!allP.getName().equalsIgnoreCase(p.getName())) {
					
				sendPlayers(p,"AddF",allP.getIp(),allP.getPort());
				sendPlayers(allP,"AddF",p.getIp(),p.getPort());
				
				sendPlayers(p,"AddALL",allP.getIp(),allP.getPort());
				sendPlayers(allP,"AddALL",p.getIp(),p.getPort());
				
				}
			}	
			broadcast("DUEL!");
			//broadcast("duelstarted!");
			duelStarted = true;
			
		}
	}
	
	public void callSummary() {
		for(Summary s : mySummary) {
			System.out.println("Opponent: "+s.getPlayer2()+" | opponent number: "+s.getNumber2()+" | your number: "+s.getNumber1()+" | target number: "+s.getTarget()+" | Result: "+s.getResult());
		}
		
	};
	
	public void matchup() {
		
		Message msg = null ;
		Random rand = new Random();
		int index = rand.nextInt(futureCompetitor.size());
	
		if(pendingOps.size() != 0) {
			Duel myDuel = new Duel(p1,pendingOps.get(0));
			myDuel.matchup();			
			pendingOps.remove(0);
		}	
	
		else if(futureCompetitor.size() != 0) {	
				
				Duel myDuel = new Duel(p1,futureCompetitor.get(index));
				myDuel.matchup();	
		}
		else {
			System.out.println("no more Opponents");
			available = true;
		}
	
	}
	
	public boolean getAvaility() {
		return available;
	}
	
	
	public void sendMessage(String targetMessage,String targetip,int targetPort){
		
		targetMessage = targetMessage +"&"+p1.getName()+"&"+p1.getIp()+"&"+p1.getPort();
		
		Message msg = new Message(targetMessage,targetip,targetPort);
		
		msg.run();
	}
	
	
	
	public void sendPlayers(Player p, String targetMessage,String targetip,int targetPort){
		
		targetMessage = targetMessage +"&"+p.getName()+"&"+p.getIp()+"&"+p.getPort();
		Message msg = new Message(targetMessage,targetip,targetPort);
		
		msg.run();
	}



	public void broadcast(String msg){
				 Message newMsg = null;
					for(Player p : allPlayers) {
						newMsg = new Message(msg,p.getIp(),p.getPort());
						newMsg.run();
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {					
							//e.printStackTrace();
						}
						
					}	
	}
		
}
