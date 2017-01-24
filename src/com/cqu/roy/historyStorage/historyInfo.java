package com.cqu.roy.historyStorage;

public class historyInfo {
	private String TextInfo;//文本信息
	private int caretPosition;//光标的位置信息
	private String changeText;//改变的6个字符的信息
	public historyInfo(String textInfo,int caretPosition,String changeText) {
		// TODO Auto-generated constructor stub
		this.TextInfo = textInfo;
		this.caretPosition = caretPosition;
		this.changeText = changeText;
	}
	public void setChangeText(String changeText) {
		this.changeText = changeText;
	}
	public String getChangeText() {
		return changeText;
	}
	public void setTextInfo(String TextInfo) {
		this.TextInfo = TextInfo;
	}
	public String getTextInfo() {
		return TextInfo;
	}
	public void setCaretPosition(int caretPosition) {
		this.caretPosition = caretPosition;
	}
	public int getCaretPosition() {
		return caretPosition;
	}
}
