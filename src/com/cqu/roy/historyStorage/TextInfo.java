package com.cqu.roy.historyStorage;

public class TextInfo {
	private String text;
	private int startPosition;
	private int endPostion;
	private int length;
	
	public TextInfo(String text,int startPosition,int endPosition,int length) {
		// TODO Auto-generated constructor stub
		this.text = text;
		this.startPosition = startPosition;
		this.endPostion = endPosition;
		this.length = length;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}
	
	public int getStartPostion() {
		return startPosition;
	}
	
	public void setEndPositon(int endPosition) {
		this.endPostion = endPosition;
	}
	
	public int getEndPosition() {
		return endPostion;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	public int getLength() {
		return length;
	}
}
