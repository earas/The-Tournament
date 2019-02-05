
import java.util.Random;
import java.util.Scanner;


public class Duel{
	Player p1,p2;
	static boolean myAnswer = false;
	Scanner scan = new Scanner(System.in);
	static int number = 0;
	Random rand = new Random();
	
	public Duel(Player p1, Player p2) {
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public Duel(boolean answer) {
		myAnswer = answer;
	}
	
	public Duel(String number) {
		Security sec = new Security();
		int newNum = Integer.parseInt(sec.decrypt(number));
		this.number = newNum;
	}
	
		
	
	public void sendMessage(Player p1, String targetMessage,String targetip,int targetPort){
		
		targetMessage = targetMessage +"&"+p1.getName()+"&"+p1.getIp()+"&"+p1.getPort();
		
		Message msg = new Message(targetMessage,targetip,targetPort);
		
		msg.run();
	}



	public void matchup() {
	
	Thread matchingup = new Thread(new Runnable() {
		 @Override
         public void run() {
			 Message msg;
			 
			 sendMessage(p1,"shallWeDuel?",p2.getIp(),p2.getPort());
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if(myAnswer) {
					myAnswer = false;
					msg = new Message("makeDisable!",p1.getIp(),p1.getPort());
					msg.run();
					msg = new Message("started!",p2.getIp(),p2.getPort());
					msg.run();
					System.out.println("Duel started between you and "+p2);
									
					System.out.println("enter your number: ");
					String myNumber= scan.next();
					
					Security sec = new Security();
										
					String key ="key&"+sec.encrypt(myNumber);
					
					msg = new Message(key,p2.getIp(),p2.getPort());
					msg.run();	
					
					
					System.out.println("waiting for number...");
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
					
					 
					int mineNumber = Integer.parseInt(myNumber);
					int target = rand.nextInt(99)+1;
					int difference1 = 0;
					int difference2 = 0;
					int total = mineNumber + number;
					
					if(target > number) {
						difference2 = target-number;
					}
					else if (target < number)
					{
						difference2 = number-target;
						
					}
					else if(target == number) {
						difference2 = 0;
					}
					
					if(target > mineNumber) {
						difference1 = target-mineNumber;
					}
					else if (target < mineNumber)
					{
						difference1 = mineNumber-target;
						
					}
					else if(target == number) {
						difference1 = 0;
					}
					
										
					if(difference1 > difference2) {
						
						
						if(checkOdd(total)) {
							
							sendMessage(p1,"winner!&"+target+"&"+number+"&"+mineNumber,p2.getIp(),p2.getPort());					
							sendMessage(p2,"looser!&"+target+"&"+mineNumber+"&"+number,p1.getIp(),p1.getPort());
							
						}
						else {
							
							sendMessage(p2,"winner!&"+target+"&"+mineNumber+"&"+number,p1.getIp(),p1.getPort());
							sendMessage(p1,"looser!&"+target+"&"+number+"&"+mineNumber,p2.getIp(),p2.getPort());
							
						}										
						
					}
					else if (difference1 < difference2)
					{										
						if(checkOdd(total)) {							
							sendMessage(p2,"winner!&"+target+"&"+mineNumber+"&"+number,p1.getIp(),p1.getPort());
							sendMessage(p1,"looser!&"+target+"&"+number+"&"+mineNumber,p2.getIp(),p2.getPort());							
						}
						else {							
							sendMessage(p1,"winner!&"+target+"&"+number+"&"+mineNumber,p2.getIp(),p2.getPort());					
							sendMessage(p2,"looser!&"+target+"&"+mineNumber+"&"+number,p1.getIp(),p1.getPort());							
						}												
					}
					else if(difference1 == difference2) {
						
						int newRand = rand.nextInt(2);
						if(newRand == 0) {
							sendMessage(p2,"winner!&"+target+"&"+mineNumber+"&"+number,p1.getIp(),p1.getPort());
							sendMessage(p1,"looser!&"+target+"&"+number+"&"+mineNumber,p2.getIp(),p2.getPort());
							
						}
						else {
							sendMessage(p1,"winner!&"+target+"&"+number+"&"+mineNumber,p2.getIp(),p2.getPort());					
							sendMessage(p2,"looser!&"+target+"&"+mineNumber+"&"+number,p1.getIp(),p1.getPort());						
						}
						
					}
											
					number = 0;										
					sendMessage(p1,"deletePending!",p2.getIp(),p2.getPort());
					sendMessage(p2,"deletePending!",p1.getIp(),p1.getPort());
					sendMessage(p1,"DellOp!",p2.getIp(),p2.getPort());
					sendMessage(p2,"DellOp!",p1.getIp(),p1.getPort());
				
					msg = new Message("makeAvailable!",p1.getIp(),p1.getPort());
					msg.run();	
					msg = new Message("makeAvailable!",p2.getIp(),p2.getPort());
					msg.run();
			
					msg = new Message("Duel!",p1.getIp(),p1.getPort());
					msg.run();	
					msg = new Message("Duel!",p2.getIp(),p2.getPort());
					msg.run();
			
				}
				else {
										
					msg = new Message("waitForOn!",p1.getIp(),p1.getPort());
					msg.run();
										
					sendMessage(p2,"WaitReciever!",p1.getIp(),p1.getPort());
					sendMessage(p1,"WaitReciever!",p2.getIp(),p2.getPort());
						
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
		
					msg = new Message("Duel!",p2.getIp(),p2.getPort());
					msg.run();
					
				}
				
		 }}); 
	
	matchingup.start();	
	
}
	
	public boolean checkOdd(int number){
		if(number % 2 == 0)
			return false;		
		return true;
	};

}