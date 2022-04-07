package intfic;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import intfic.elements.Path;
import intfic.elements.Room;

public class FictionPlayer {

	private List<Room> rooms;
	
	private List<String> inventory;
	
	private Room currentRoom;
	
	
	public FictionPlayer(List<Room> rooms) {
		this.rooms = rooms;
		this.inventory = new ArrayList<String>();
		setRoom(0);
	}
	
	private void setRoom(int id) {
		for(Room r : this.rooms)
			if(r.getID() == id) {
				this.currentRoom = r;
				for(String s : r.getView())
					System.out.println(s);
			}
	}
	
	public void addItem(String item) {
		this.inventory.add(item);
	}
	
	public void removeItem(String item) {
		this.inventory.remove(item);
	}
	
	public boolean hasItem(String item) {
		return this.inventory.contains(item);
	}
	
	public void start() {
		Scanner s = new Scanner(System.in);
		while(s.hasNext())
			parseCommand(s.nextLine().trim().toLowerCase());
	}

	private void parseCommand(String line) {
		String command = "";
		String arg = "";
		if(line.contains(" ")) {
			String[] sa = line.split(" ");
			command = sa[0];
			arg = sa[1];
		}else {
			command = line;
		}
		String knownCommand = Alias.getKnown(command);
		//TODO Allow for paths to use commands without arguments.
		Path p = this.currentRoom.getPath(knownCommand + " " + arg);
		if(p != null) {
			if(p.metRequirement()) {
				setRoom(p.getDestinationRoom());
			}else {
				//TODO make these message configurable in the story file
				System.out.println("You don't meet the requirements to go this way.");
			}
			return;
		}else {
			if(knownCommand.equals("go")) {
				System.out.println("You can't go that way.");
				return;
			}else if(knownCommand.equals("grab")) {
				if(arg.equals("")) {
					System.out.println("What do you want to grab?");
					return;
				}
				if(this.currentRoom.hasItem(arg)) {
					System.out.println("You picked up " + arg + ".");
					addItem(arg);
					this.currentRoom.removeItem(arg);
					return;
				}
			}else if(knownCommand.equals("inventory")) {
				if(this.inventory.size() == 0) {
					System.out.println("You are not carrying anything.");
					return;
				}
				System.out.println("You are carrying the following items:");
				for(String s : this.inventory) {
					System.out.println("- " + s);
				}
				return;
			}
			
		}
	}
	
}
