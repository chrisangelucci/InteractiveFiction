package intfic;

import java.util.List;

import intfic.elements.Room;
import intfic.parser.FictionElementParser;
import intfic.parser.ParseError;

public class InteractiveFiction {
	
	private static FictionPlayer player;
	
	public static void main(String[] args) throws ParseError {
		List<Room> rooms=null;
		try {
			rooms = new FictionElementParser("C:\\Users\\Chris\\Desktop\\Game\\workspace\\InteractiveFiction\\story.txt").parseFile();
		} catch (ParseError e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		for(Room r : rooms) {
			System.out.println(r);
		}
		//player = new FictionPlayer(rooms);
		//player.start();
	}
	
	public static FictionPlayer getPlayer() {
		return player;
	}
	
}
