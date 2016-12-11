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
	private Set<String> keywords;
	private RexPlay rPlay;
	private JTextPane jtp;
	private Vector<Token> vc_token;
	
	public SyntaxHighlighter(JTextPane jtp) {
		// TODO Auto-generated constructor stub
		keywordStyle = ((StyledDocument) jtp.getDocument()).addStyle("Keyword_Style", null);
		normalStyle = ((StyledDocument) jtp.getDocument()).addStyle("Keyword_Style", null);
		//渲染颜色
		StyleConstants.setForeground(keywordStyle, Color.BLUE);
		StyleConstants.setForeground(normalStyle, Color.BLACK);
		
		this.jtp = jtp;
		
		try {
			rPlay = new RexPlay(jtp.getDocument().getText(0, jtp.getDocument().getLength()));
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		keywords = new HashSet<String>();
		keywords.add("public");
		keywords.add("protected");
		keywords.add("private");
		keywords.add("_int9");
		keywords.add("float");
		keywords.add("double");
	}
	
	public void colouring(StyledDocument doc,int pos,int len) throws BadLocationException{
//		int start = indexOfWordStart(doc,pos);
//		int end = indexOfWordEnd(doc, pos + len);
//		
//		char ch;
//		while(start < end){
//			ch = getCharAt(doc, pos);
//			if (Character.isLetter(ch) || ch == '_') {
//				start = colouringWord(doc, start);
//			}else {
//				SwingUtilities.invokeLater(new ColouringTask(doc, start, 1, normalStyle));
//				++start;
//			}
//		}
		colouringWord(doc, 0);
	}
	
	public int colouringWord(StyledDocument doc, int pos) throws BadLocationException {
		int wordEnd = indexOfWordEnd(doc, pos);
		String word = doc.getText(pos, wordEnd - pos);
//		if (keywords.contains(word)) {
//			// 如果是关键字, 就进行关键字的着色, 否则使用普通的着色.
//			// 这里有一点要注意, 在insertUpdate和removeUpdate的方法调用的过程中, 不能修改doc的属性.
//			// 但我们又要达到能够修改doc的属性, 所以把此任务放到这个方法的外面去执行.
//			// 实现这一目的, 可以使用新线程, 但放到swing的事件队列里去处理更轻便一点.
//			SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd - pos, keywordStyle));
//		} else {
//			SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd - pos, normalStyle));
//		}
		//doc.setCharacterAttributes(0, jtp.getDocument().getLength(), normalStyle, true);
		for(int i = 0; i < vc_token.size();i++){
			Token token = vc_token.get(i);
			SwingUtilities.invokeLater(new ColouringTask(doc,token.getAbsLocation(),token.getLength() ,keywordStyle));
		}
		return wordEnd;
	}
	
	public int indexOfWordStart(Document doc,int pos)throws BadLocationException {
		for(;pos > 0 && isWordCharacter(doc, pos - 1); --pos);
		return pos;
	}
	
	public int indexOfWordEnd(Document doc,int pos) throws BadLocationException {
		for(;isWordCharacter(doc, pos);++pos);
		return pos;
	}
	
	public char getCharAt(Document doc, int pos) throws BadLocationException {
		return doc.getText(pos, 1).charAt(0);
	}
	
	public boolean isWordCharacter(Document doc, int pos) throws BadLocationException {
		char ch = getCharAt(doc, pos);
		if (Character.isLetter(ch) || Character.isDigit(ch) || ch == '_') { return true; }
		return false;
	}
	@Override
	public void insertUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		try {
			try {
				RexPlay rPlay = new RexPlay(jtp.getDocument().getText(0
						, jtp.getDocument().getLength()));
				vc_token = rPlay.getToken();
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			colouring((StyledDocument) e.getDocument(), e.getOffset(), e.getLength());
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
				
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			vc_token = rPlay.getToken();
			colouring((StyledDocument) e.getDocument(), e.getOffset(), 0);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		
	}
}
