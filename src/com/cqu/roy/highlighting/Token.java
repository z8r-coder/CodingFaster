package com.cqu.roy.highlighting;

public class Token {
	private String value;//值
	private int StartPosition;//绝对起始位置
	private int EndPosition;//结束位置
	private int length;//长度
	private int absLocation;//段内偏移位置
	private int location;//绝对位置
	
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
