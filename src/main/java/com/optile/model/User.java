package com.optile.model;

public class User {

	private String id;
	private String displayName;
	private String name;
	private String email;
	private String country;
	private String referralLink;
	public User(String id, String displayName, String name, String email, String country, String referralLink) {
		super();
		this.id = id;
		this.displayName = displayName;
		this.name = name;
		this.email = email;
		this.country = country;
		this.referralLink = referralLink;
	}
	@Override
	public String toString() {
		return "\n ID=" + id + "\n displayName=" + displayName + "\n name=" + name + "\n email=" + email + "\n country="
				+ country + "\n referralLink=" + referralLink;
	}
	
	
	
}
