package intfic.elements;

import java.util.List;

public class Room {

	private int id;
	
	private List<View> views;
	
	private List<Path> paths;
	
	private List<String> contents;
	
	public Room(int id, List<View> views, List<Path> paths, List<String> contents) {
		this.id = id;
		this.views = views;
		this.paths = paths;
		this.contents = contents;
	}
	
	public int getID() {
		return this.id;
	}
	
	public List<View> getViews() {
		return this.views;
	}
	
	public void addItem(String item) {
		this.contents.add(item);
	}
	
	public boolean hasItem(String item) {
		return this.contents.contains(item);
	}
	
	public void removeItem(String item) {
		this.contents.remove(item);
	}
	
	public Path getPath(String command) {
		for(Path p : this.paths)
			if(p.getCommand().equals(command))
				return p;
		return null;
	}
	
	public String toString() {
		String str = "Room[id:" + this.id + ",views:" + this.views + ",paths:";
		for(Path p : this.paths)
			str = str + p.toString() + ",";
		str = str + "]";
		return str;
	}
	
}
