package intfic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import intfic.elements.Path;
import intfic.elements.Requirement;
import intfic.elements.Room;
import intfic.parser.ParseError;

public class FictionParser {
	
	private String path;
	
	public FictionParser(String path) throws ParseError {
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
		
		List<String> partialRoom = new ArrayList<String>();
		
		boolean inRoom = false;
		int lineNumber = 1;
		int roomLine = 0;
		try {
			while((line = b.readLine()) != null) {
				line = line.trim();
				if(!inRoom && line.startsWith("[Room ")) {
					roomLine = lineNumber;
					inRoom = true;
				}
				if(inRoom && !line.equals("") && !line.startsWith("//"))
					partialRoom.add(line);
				if(inRoom && line.equals("[End-Room]")) {
					inRoom = false;
					rooms.add(parseRoom(partialRoom, roomLine));
					partialRoom.clear();
				}
				lineNumber++;
			}
			b.close();
			return rooms;
		} catch (IOException e) {
			throw new ParseError("Error reading line.");
		}
	}

	private Room parseRoom(List<String> partialRoom, int roomLine) throws ParseError{
		int id = 0;
		List<String> view = new ArrayList<String>();
		List<Path> paths = new ArrayList<Path>();
		List<String> contents = new ArrayList<String>();
		for(String line : partialRoom) {
			
			if(line.startsWith("[Room ")) {
				if(Utils.isInt(line.substring(6,7))) {
					id = Integer.valueOf(line.substring(6,7));
					continue;
				}
				throw new ParseError("Expecting integer id in room on line " + roomLine);
			}
			
			if(line.startsWith("[End-Room]"))
				continue;
			
			if(line.startsWith("[Path-to ")) {
				String pathInfo = Utils.getBetween(line, '[', ']');
				pathInfo = pathInfo.substring(8);
				
				int destinationRoom = 0;
				if(Utils.isInt(pathInfo.substring(0,1))) {
					destinationRoom = Integer.valueOf(pathInfo.substring(0,1));
				}else {
					throw new ParseError("Expecting integer destination in room on line " + roomLine);
				}
				
				Requirement req = null;
				if(pathInfo.contains("req(")) {
					String typeChar = Utils.getBetween(pathInfo, '(', ')');
					Requirement.RequirementType type = Requirement.RequirementType.ITEM;
					if(typeChar.equals("h")) {
						type = Requirement.RequirementType.HISTORY;
					}
					
					if(pathInfo.contains("=")) {
						String data = pathInfo.split("=")[1];
						req = new Requirement(type, data);
					}else {
						throw new ParseError("Incorrect requirement in room on line " + roomLine + ". Missing '='");
					}
				}
				
				String command = Utils.getBetween(pathInfo, '"', '"');
				
				//TODO check to make sure command is a known command and not a dedicated command.
				paths.add(new Path(command, destinationRoom, req));
				continue;
			}
			
			if(line.startsWith("[Item ")) {
				line = line.substring(6);
				line = line.substring(0, line.length()-1);
				contents.add(line);
				continue;
			}
			
			view.add(line);
		}
		return new Room(id, view, paths, contents);
	}
}
