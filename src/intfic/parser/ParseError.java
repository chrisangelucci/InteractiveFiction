package intfic.parser;

public class ParseError extends Exception {
	private static final long serialVersionUID = 5663533418509662861L;
	
	private String message;
	
	public ParseError(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
	
}
