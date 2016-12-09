package com.cqu.roy.highlighting;

import javax.swing.text.StyledDocument;

public class ColouringTask implements Runnable{
	private StyledDocument doc;
	private javax.swing.text.Style style;
	private int pos;
	private int len;
	
	public ColouringTask(StyledDocument doc, int pos, int len, javax.swing.text.Style normalStyle) {
		this.doc = doc;
		this.pos = pos;
		this.len = len;
		this.style = normalStyle;

	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		doc.setCharacterAttributes(pos, len, style, true);
	}

}
