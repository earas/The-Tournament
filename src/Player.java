
public class Player {
	
	private String name;
	private String ip;
	private int port;
	
	
	
	public Player(String name, String ip, int port) {		
		this.name = name;
		this.ip = ip;
		this.port = port;
		
		
	}
	
	
	public String toString() {
		
		return name;
	}

	
	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getIp() {
		return ip;
	}



	public void setIp(String ip) {
		this.ip = ip;
	}



	public int getPort() {
		return port;
	}



	public void setPort(int port) {
		this.port = port;
	}



}
