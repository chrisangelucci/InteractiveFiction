package intfic.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import intfic.Utils;
import intfic.elements.Path;
import intfic.elements.Requirement;
import intfic.elements.Requirement.RequirementType;
import intfic.elements.Room;
import intfic.elements.View;

public class FictionElementParser {
	
	private String path;
	
	public FictionElementParser(String path) throws ParseError {
		//Including file type check for "custom" type later.
		if(new File(path).exists() && path.endsWith(".txt")){
			this.path = path;
		}else {
			throw new ParseError("File does not exist, or is incorrect type.");
		}
	}
	
	public List<Room> parseFile() throws ParseError {
		List<Room> rooms = new ArrayList<Room>();
		BufferedReader b = null;
		try {
			b = new BufferedReader(new FileReader(new File(this.path)));
		} catch (FileNotFoundException e) {
			throw new ParseError("File does not exist.");
		}
		
		String line = "";
		
		List<Object[]> elements = new ArrayList<Object[]>();
		
		int lineNumber = 1;
		try {
			while((line = b.readLine()) != null) {
				line = line.trim();
				if(line.startsWith("[") && line.endsWith("]")) {
					elements.add(parseElement(line));
				}else if(!line.startsWith("//")) {
					elements.add(new Object[] {line});
				}
				
				lineNumber++;
			}
			b.close();
		} catch (IOException e) {
			throw new ParseError("Error reading line.");
		}
		
		int roomId = 0;
		List<View> views = new ArrayList<View>();
		View partialView = null;
		List<Path> paths = new ArrayList<Path>();
		List<String> contents = new ArrayList<String>();
		boolean inRoom = false;
		for(Object[] o : elements) {
			if(o[0].equals("Room")) {
				inRoom = true;
				roomId = (int)o[1];
			}else if(o[0].equals("View")) {
				if(!inRoom) throw new ParseError("View element must be within a Room element.");
				
				views.add(new View((int)o[0], (Requirement)o[3]));
			}else if(o[0].equals("End-View")) {
				if(partialView == null) throw new ParseError("End-View element was found before View element.");
				
				views.add(partialView);
				partialView = null;
			}else if(o[0].equals("End-Room")) {
				if(!inRoom) throw new ParseError("End-Room element was found before Room element.");
				
				rooms.add(new Room(roomId, views, paths, contents));
				inRoom = false;
				views = new ArrayList<View>();
				partialView=null;
				paths = new ArrayList<Path>();
				contents = new ArrayList<String>();
			}else if(o[0].equals("Path")) {
				if(!inRoom) throw new ParseError("Path element was found outside of Room element.");
				paths.add(new Path((String)o[2], (int)o[1], (Requirement)o[3]));
			}
			if(partialView != null) {
				partialView.addLine((String)o[0]);
			}
		}
		
		return rooms;
	}
	
	public static Object[] parseElement(String element) throws ParseError{
		Object[] parsedElement = new Object[4];		
		//[Path 1 "go north" req(i)=key]
		element = element.substring(1,element.length()); 		//Path 1 "go north" req(i)=key
		
		String data = "";
		if(element.contains("\"")) {
			data = Utils.getBetween(element, '"', '"');
			parsedElement[2] = data;
		}
		element = element.replaceFirst("\"" + data + "\" ", ""); //Path 1  req(i)=key
		
		
		if(!element.contains(" ")) {
			parsedElement[0] = element;
			return parsedElement;
		}
		
		String[] elementSplit = element.split(" ");
		parsedElement[0] = elementSplit[0];
		
		if(Utils.isInt(elementSplit[1])) {
			parsedElement[1] = Integer.valueOf(elementSplit[1]);
		}
		
		if(elementSplit.length == 3) {
			if(elementSplit[2].startsWith("req(")) {
				String req = elementSplit[2];
				String typeChar = Utils.getBetween(req, '(', ')');
				Requirement.RequirementType type = RequirementType.ITEM;
				if(typeChar.equals("h")) {
					type = RequirementType.HISTORY;
				}
				
				if(req.contains("=")) {
					String rdata = req.split("=")[1];
					parsedElement[3] = new Requirement(type, rdata);
				}else {
					throw new ParseError("Incorrect requirement in room. Missing '='");
				}
			}
		}
		
		return parsedElement;
	}
}
