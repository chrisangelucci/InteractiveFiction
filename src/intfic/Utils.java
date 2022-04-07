package intfic;

public class Utils {
	public static boolean isInt(String s) {
		try {
			Integer.valueOf(s);
			return true;
		}catch(NumberFormatException e) {
			return false;
		}
	}

	public static String getBetween(String str, char start, char end) {
		return str.substring(str.indexOf(start)+1, str.indexOf(end, str.indexOf(start)+1));
	}
}
