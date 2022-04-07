package intfic;

import java.util.Arrays;

public class Alias {

	public static String getKnown(String cmd) {
		if(Arrays.asList(new String[]{"travel, head, move"}).contains(cmd))
			return "go";
		return cmd;
	}
	
}
