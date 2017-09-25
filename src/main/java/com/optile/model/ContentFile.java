package com.optile.model;

public class ContentFile extends Content{

	private long size;
	private String metaType;
	
	public ContentFile(String type, String name, String modifDate, long size, String metaType) {
		super(type, name, modifDate);
		this.size = size;
		this.metaType = metaType;
	}

	public long getSize() {
		return size;
	}

	public String getMetaType() {
		return metaType;
	}

	@Override
	public String toString() {
		return super.toString() + ", " + size + " KB, " + metaType;
	}
	
	
}
