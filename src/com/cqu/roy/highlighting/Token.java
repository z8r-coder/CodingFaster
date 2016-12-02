package com.cqu.roy.highlighting;

public class Token {
	private String value;
	private int StartPosition;
	private int EndPosition;
	private int length;
	
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
}
