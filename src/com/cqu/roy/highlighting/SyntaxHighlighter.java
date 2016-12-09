package com.cqu.roy.highlighting;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class SyntaxHighlighter implements DocumentListener{
	private Style keywordStyle;
	private Style normalStyle;
	
	public SyntaxHighlighter(JTextPane jtp) {
		// TODO Auto-generated constructor stub
		keywordStyle = ((StyledDocument) jtp.getDocument()).addStyle("Keyword_Style", null);
	}
	
	@Override
	public void insertUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		
	}
}
