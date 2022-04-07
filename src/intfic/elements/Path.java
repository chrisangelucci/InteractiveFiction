package intfic.elements;

import intfic.InteractiveFiction;

public class Path {

	private String knownCommand;
	
	private Requirement requirement;
	
	private int destinationRoom;
	
	public Path(String knownCommand, int destinationRoom, Requirement requirement) {
		this.knownCommand = knownCommand;
		this.destinationRoom = destinationRoom;
		this.requirement = requirement;
	}
	
	public String getCommand() {
		return this.knownCommand;
	}
	
	public int getDestinationRoom() {
		return this.destinationRoom;
	}
	
	public boolean metRequirement() {
		if(this.requirement == null)return true;
		switch(this.requirement.getType()) {
			case HISTORY: return true;
			case ITEM: return InteractiveFiction.getPlayer().hasItem(this.requirement.getData());
			default: return true;
		
		}
	}
	
	public String toString() {
		return "Path[cmd:" + this.knownCommand + ",destID:" + this.destinationRoom + ",req:" + this.requirement + "]";
	}
	
}
