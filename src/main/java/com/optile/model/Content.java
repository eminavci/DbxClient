package com.optile.model;

public class Content {
	private String type;
	private String name;
	private String modifDate;
	
	public Content(String type, String name, String modifDate) {
		super();
		this.type = type;
		this.name = formatName(name);
		this.modifDate = modifDate;
	}
	public String getType() {
		return type;
	}
	public String getName() {
		return name;
	}
	public String getModifDate() {
		return modifDate;
	}
	
	
	
	@Override
	public String toString() {
		return name + " : " + type + ", " + modifDate;
	}
	private static String formatName(String nn){
		if(nn.length() > 50 )
			return nn.substring(0, 47) + "...";
		return String.format("%-" + 50 + "." + 50 + "s", nn);
	}
}
