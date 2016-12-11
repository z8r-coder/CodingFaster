package com.cqu.roy.highlighting;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

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
	private Style keywordStyle;//
	private Style normalStyle;
	private Style typeStyle;
	private Set<String> keywords;
	private RexPlay rPlay;
	private JTextPane jtp;
	private Vector<Token> vc_KeyWord_token;
	private Vector<Token> vc_Type_token;
	
	public SyntaxHighlighter(JTextPane jtp) {
		// TODO Auto-generated constructor stub
		keywordStyle = ((StyledDocument) jtp.getDocument()).addStyle("Keyword_Style", null);
		typeStyle = ((StyledDocument) jtp.getDocument()).addStyle("Keyword_Style", null);
		normalStyle = ((StyledDocument) jtp.getDocument()).addStyle("Keyword_Style", null);
		//渲染颜色
		StyleConstants.setForeground(keywordStyle, Color.RED);	
		StyleConstants.setForeground(typeStyle, Color.BLUE);
		StyleConstants.setForeground(normalStyle, Color.BLACK);
		
		this.jtp = jtp;
		
		try {
			rPlay = new RexPlay(jtp.getDocument().getText(0, jtp.getDocument().getLength()));
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void colouring(StyledDocument doc,int pos,int len) throws BadLocationException{
		colouringWord(doc, 0);
	}
	
	public void colouringWord(StyledDocument doc, int pos) throws BadLocationException {
		SwingUtilities.invokeLater(new ColouringTask(doc, 0, doc.getLength(), normalStyle));
		for(int i = 0; i < vc_KeyWord_token.size();i++){
			Token token = vc_KeyWord_token.get(i);
//			System.out.println("value:" + token.getValue() + " start:" + token.getLocation());
			SwingUtilities.invokeLater(new ColouringTask(doc,token.getLocation()
					,token.getLength() ,keywordStyle));
		}
		for(int i = 0; i < vc_Type_token.size();i++){
			Token token = vc_Type_token.get(i);
			SwingUtilities.invokeLater(new ColouringTask(doc, token.getLocation()
					, token.getLength(), typeStyle));
		}
	}
	
	@Override
	public void insertUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		try {
			try {
				RexPlay rPlay = new RexPlay(jtp.getDocument().getText(0
						, jtp.getDocument().getLength()));
				vc_KeyWord_token = rPlay.getKeyWordToken();
				vc_Type_token = rPlay.getTypeToken();
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			colouring((StyledDocument) e.getDocument(), e.getOffset(), e.getLength());
//			System.out.println("insert:" + e.getOffset());
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		try {
			// 因为删除后光标紧接着影响的单词两边, 所以长度就不需要了
			try {
				RexPlay rPlay = new RexPlay(jtp.getDocument().getText(0
						, jtp.getDocument().getLength()));
				vc_KeyWord_token = rPlay.getKeyWordToken();
				vc_Type_token = rPlay.getTypeToken();
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			colouring((StyledDocument) e.getDocument(), e.getOffset(), 0);
//			System.out.println("remove:" + e.getOffset());
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		
	}
}
