package com.cqu.roy.highlighting;

public class Token {
	private String value;
	private int StartPosition;
	private int EndPosition;
	private int length;
	private int absLocation;
	private int location;
	
	public Token(String value,int StartPosition,int EndPosition,int length) {
		// TODO Auto-generated constructor stub
		this.value = value;
		this.StartPosition = StartPosition;
		this.EndPosition = EndPosition;
		this.length = length;
	}
	
	public String getValue() {
		return value;
	}
	
	public int getStartPosition() {
		return StartPosition;
	}
	
	public int getEndPosition() {
		return EndPosition;
	}
	
	public int getLength() {
		return length;
	}
	public void setAbsLocation(int absLocation) {
		this.absLocation = absLocation;
	}
	public int getAbsLocation() {
		return absLocation;
	}
	public void setLocation(int location) {
		this.location = location;
	}
	public int getLocation() {
		return location;
	}
}
