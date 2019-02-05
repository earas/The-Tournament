
import java.io.IOException;
import java.net.Socket;

public class Message extends Thread {

    String message, hostname;
    int port;

    public Message(){}

    public Message(String message, String hostname, int port){
        this.message = message;
        this.hostname = hostname;
        this.port = port;

    }
    
    public Message(Player p1, String message, String hostname, int port){
        

    }

    @Override
    public void run(){
        try {
        	Socket s = new Socket(hostname, port);
            s.getOutputStream().write(message.getBytes());
            s.close();

        } catch (IOException e) {
            //e.printStackTrace();
        }
    }


}