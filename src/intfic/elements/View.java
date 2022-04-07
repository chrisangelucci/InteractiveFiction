package intfic.elements;

import java.util.ArrayList;
import java.util.List;

public class View {

	private Requirement requirement;
	
	private int id;
	
	private List<String> lines;
	
	public View(int id, Requirement requirement) {
		this.id = id;
		this.requirement = requirement;
		this.lines = new ArrayList<String>();
	}

	public void addLine(String line) {
		this.lines.add(line);
	}
}
