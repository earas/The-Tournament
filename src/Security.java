
public class Security {
	
	
	public Security() {
		
	}

	public static String encrypt(String key) {
		String result = "";
		int l = key.length();
		char ch;
		for(int i = 0; i < l; i++) {
			ch = key.charAt(i);
			ch += 10;
			result += ch;
		}
		
		return result;
	}
	
	public static String decrypt(String key) {
		String result = "";
		int l = key.length();
		char ch;
		for(int i = 0; i < l; i++) {
			ch = key.charAt(i);
			ch -= 10;
			result += ch;
		}
		
		return result;
	}
	
}