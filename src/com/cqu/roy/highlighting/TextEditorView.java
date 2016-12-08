package com.cqu.roy.highlighting;

import java.awt.Graphics;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.PlainView;
import javax.swing.text.Segment;

public class TextEditorView extends PlainView{

	public TextEditorView(Element elem) {
		super(elem);
		// TODO Auto-generated constructor stub
	}
	protected int drawSelectedText(Graphics g, int x, int y, int p0, int p1) 
			throws BadLocationException{
		Segment segment = new Segment();
		return p1;
		
	}

}
