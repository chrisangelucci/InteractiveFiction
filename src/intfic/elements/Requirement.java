package intfic.elements;

public class Requirement {

	public enum RequirementType{
		ITEM,HISTORY;
	}
	
	private RequirementType type;
	private String data;
	
	public Requirement(RequirementType type, String data) {
		this.type = type;
		this.data = data;
	}
	
	public RequirementType getType() {
		return this.type;
	}
	
	public String getData() {
		return this.data;
	}
	
}
