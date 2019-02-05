
public class Summary {
	
	private String player1;
	private String player2;
	int target;
	int number1;
	int number2;
	String result;
	

	public Summary(String player1, String player2, int target, int number1, int number2, String result) {
		this.player1 = player1;
		this.player2 = player2;
		this.target = target;
		this.number1 = number1;
		this.number2 = number2;
		this.result = result;
	}


	public String getPlayer1() {
		return player1;
	}


	public void setPlayer1(String player1) {
		this.player1 = player1;
	}


	public String getPlayer2() {
		return player2;
	}


	public void setPlayer2(String player2) {
		this.player2 = player2;
	}


	public int getTarget() {
		return target;
	}


	public void setTarget(int target) {
		this.target = target;
	}


	public int getNumber1() {
		return number1;
	}


	public void setNumber1(int number1) {
		this.number1 = number1;
	}


	public int getNumber2() {
		return number2;
	}


	public void setNumber2(int number2) {
		this.number2 = number2;
	}


	public String getResult() {
		return result;
	}


	public void setResult(String result) {
		this.result = result;
	}

	
	
}
