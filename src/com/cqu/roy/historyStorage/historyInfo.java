package com.cqu.roy.historyStorage;

import java.util.Vector;

import com.cqu.roy.mywdiget.MyLabel;

public class historyInfo {
	private String TextInfo;//文本信息
	private int caretPosition;//光标的位置信息
	private Vector<MyLabel> lineInfo;//行号信息

	public historyInfo(String textInfo,int caretPosition,Vector<MyLabel> lineInfo) {
		// TODO Auto-generated constructor stub
		this.TextInfo = textInfo;
		this.caretPosition = caretPosition;
		this.lineInfo = lineInfo;
	}
	public void setLineInfo(Vector<MyLabel> lineInfo) {
		this.lineInfo = lineInfo;
	}
	public Vector<MyLabel> getLineInfo() {
		return lineInfo;
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
