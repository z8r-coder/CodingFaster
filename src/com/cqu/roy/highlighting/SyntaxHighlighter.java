package com.cqu.roy.highlighting;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.cqu.roy.historyStorage.Node;
import com.cqu.roy.historyStorage.TextInfo;
import com.cqu.roy.historyStorage.VersionTree;
import com.cqu.roy.main.main;
import com.cqu.roy.mainframe.MainFrame;
import com.cqu.roy.mywdiget.MainJpanel;
import com.cqu.roy.mywdiget.MainLayout;
import com.cqu.roy.mywdiget.MyJTextPane;
import com.cqu.roy.mywdiget.MyLabel;
/*文本监听
 * */
public class SyntaxHighlighter implements DocumentListener{
	private MainFrame mainFrame;
	private Style keywordStyle;//
	private Style normalStyle;
	private Style notesStyle;
	private Style StringStyle;
	private Style IntegerStyle;
	private Style typeStyle;
	private HashSet<String> keyWord;
	private HashSet<String> typeWord;
	private RexPlay rPlay;
	private MyJTextPane jtp;
	private Vector<Token> vc_lan_token;
	private Vector<Token> vc_normal_token;
	private boolean isNewLine = false;
	//识别注释
	private final static String notes = "//.*";
	private final static String Integer = "[0-9]+";
	//退格后当前字符和上一个字符
	private String curChar;
	private String preChar;
	private VersionTree vst;//版本树
	private ArrayList<Node> currentNodeSet;//当前显示集合
	private Thread timer;
	private HashSet<Integer> modifiedLine;//修改过的行
	private Vector<MyLabel> LineNumVc;//行号集合
	public SyntaxHighlighter(MyJTextPane jtp) {
		// TODO Auto-generated constructor stub
		mainFrame = MainFrame.getInstance();
		
		keywordStyle = ((StyledDocument) jtp.getDocument()).addStyle("Keyword_Style", null);
		typeStyle = ((StyledDocument) jtp.getDocument()).addStyle("Type_Style", null);
		notesStyle = ((StyledDocument) jtp.getDocument()).addStyle("NotesStyle", null);
		IntegerStyle = ((StyledDocument) jtp.getDocument()).addStyle("IntegerStyle", null);
		normalStyle = ((StyledDocument) jtp.getDocument()).addStyle("NormalStyle", null);
		//渲染颜色
		StyleConstants.setForeground(keywordStyle, new Color(205, 50, 120));	
		StyleConstants.setForeground(typeStyle, new Color(175, 238, 238));
		StyleConstants.setForeground(notesStyle, Color.GRAY);
		StyleConstants.setForeground(IntegerStyle, new Color(238,221,130));
		StyleConstants.setForeground(normalStyle, Color.WHITE);
		this.jtp = jtp;
		//获取当前节点集合
		vst = jtp.getVersionTree();
		//获取行号集合
		LineNumVc = jtp.getLineLabel();
		currentNodeSet = vst.getCurrentNodeSet();//当前显示内容集合
		modifiedLine = new HashSet<>();
		TimerSchedule ts = new TimerSchedule(true, jtp, modifiedLine);
		timer = new Thread(ts);
		timer.start();
		try {
			rPlay = new RexPlay(jtp.getDocument().getText(0, jtp.getDocument().getLength()));
			keyWord = rPlay.getKeyWord();			//获取当前节点集合
			typeWord = rPlay.getTypeWord();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void colouring(StyledDocument doc,int pos,int len) throws BadLocationException{
		colouringWord(doc, 0);
	}
	
	public void colouringWord(StyledDocument doc, int pos) throws BadLocationException {
		//注释识别
		Pattern pattern = Pattern.compile(notes);
		SwingUtilities.invokeLater(new ColouringTask(doc, 0, doc.getLength(), normalStyle));
		for(int i = 0; i < vc_lan_token.size();i++){
			Token token = vc_lan_token.get(i);
			Matcher matcher = pattern.matcher(token.getValue());
			if (typeWord.contains(token.getValue())) {
				SwingUtilities.invokeLater(new ColouringTask(doc,token.getLocation()
						,token.getLength() ,typeStyle));
			}
			else if (keyWord.contains(token.getValue())) {
				SwingUtilities.invokeLater(new ColouringTask(doc,token.getLocation()
						,token.getLength() ,keywordStyle));
			}
			else if (matcher.matches()) {
				SwingUtilities.invokeLater(new ColouringTask(doc, token.getLocation(),
						token.getLength(),notesStyle));
			}
			else {
				SwingUtilities.invokeLater(new ColouringTask(doc, 0
						, doc.getLength(), normalStyle));
			}
		}
		for(int i = 0; i < vc_normal_token.size();i++){
			Token token = vc_normal_token.get(i);
			Pattern pattern_int = Pattern.compile(Integer);
			Matcher matcher_int = pattern_int.matcher(token.getValue());
			if (matcher_int.matches()) {
				SwingUtilities.invokeLater(new ColouringTask(doc, token.getLocation(), 
						token.getLength(), IntegerStyle));
			}
		}
	}
	
	private TextInfo getCurrentLineText(int caretPosition,String newChar){
		int caretLineNum = jtp.getCaretPosition();
		int preahead = caretLineNum;//向前探测换行符
		int lookahead = caretLineNum;//向后探测换行符
		String text = jtp.getText();
		if (newChar.equals("\n")) {
			++lookahead;
			if (lookahead < text.length()) {
				if (text.charAt(lookahead) == '\n') {
					--lookahead;
				}
			}
			else if (lookahead >= text.length()) {
				--lookahead;
			}
		}
		while(text.charAt(preahead) != '\n'){
			preahead--;
			if (preahead == -1) {
				break;
			}
		};
		while(text.charAt(lookahead) != '\n'){
			lookahead++;
			if (lookahead >= text.length()) {
				break;
			}
		};
		if (preahead == -1) {
			preahead++;
		}
		String content = null;
		try {
			content = jtp.getText(preahead,lookahead - preahead);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*参数含义
		 * 1.具体内容
		 * 2.开始位置
		 * 3.结束位置
		 * 4.长度*/
		return new TextInfo(content, preahead, lookahead, lookahead - preahead);
	}
	//插入更新
	@Override
	public void insertUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		try {
			try {
				RexPlay rPlay = new RexPlay(jtp.getDocument().getText(0
						, jtp.getDocument().getLength()));
				vc_lan_token = rPlay.getLanToken();
				vc_normal_token = rPlay.getNormalToken();
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			colouring((StyledDocument) e.getDocument(), e.getOffset(), e.getLength());
//			System.out.println("insert:" + e.getOffset());
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		try {
			//当前输入一个字符
			String newLine = e.getDocument().getText(e.getOffset(), 1);
			curChar = newLine;
			int caretLine;
			//若输入是换行符，则行号加一则正确
			if (newLine.equals("\n")) {
				caretLine = jtp.getCaretLine() + 1;
			}else {
				caretLine = jtp.getCaretLine();
			}

			modifiedLine.add(caretLine);
			//System.out.println(caretLine);
			//每次修改的时候会产生新的版本节点，使用光标的位置更新其startPosition
			TextInfo lineText = getCurrentLineText(jtp.getCaretPosition(), 
					e.getDocument().getText(e.getOffset(), 1));
			int startPosition = lineText.getStartPostion();
			
			int lineNum = jtp.getCaretLine();
			if (lineNum < currentNodeSet.size()) {
				Node node = currentNodeSet.get(lineNum);
				node.getText().setStartPosition(startPosition);
			}
			//若修改的行数在前面，后面的作出修改的行数已经不能获得更新，因此在此处进行更新
			Iterator<Integer> iterator = modifiedLine.iterator();
			while(iterator.hasNext()){
				int temp_line = (int) iterator.next();
				//若是行号在当前操作行的下面
				if (temp_line > lineNum && temp_line < currentNodeSet.size()) {
					TextInfo textInfo = currentNodeSet.get(temp_line).getText();
					int curStartPos = textInfo.getStartPostion();
					curStartPos++;
					textInfo.setStartPosition(curStartPos);
				}
			}
					
			//当新输入的字符是换行符的时候，执行行号显示，和版本树节点创建
			if (newLine.equals("\n")) {
				TextInfo currentLineText = getCurrentLineText(jtp.getCaretPosition(),
						e.getDocument().getText(e.getOffset(), 1));
				int temp_line = jtp.getCaretLine() + 1;//光标所在行号
				Node node = new Node(currentLineText, temp_line, -1,
						-1, null, null);
				vst.InsertNode(temp_line, node);
				currentNodeSet.add(temp_line, node);
				//在中间插入，重新设置后面的所有行号
				for(int i = temp_line; i < currentNodeSet.size();i++){
					currentNodeSet.get(i).setlineNum(i);
				}
//				for(int i = 0; i < currentNodeSet.size();i++){
//					System.out.println(currentNodeSet.get(i).getText().getText());
//				}
				HashMap<String, MainJpanel> hm_textPane = mainFrame.getHashTextPane();
				//当为空时直接return
				if (hm_textPane.get(mainFrame.getCurrentAreaName()) == null) {
					return;
				}
				
				hm_textPane.get(mainFrame.getCurrentAreaName()).getTextPane().line();
				
				//行号显示
				JPanel linePanel = hm_textPane.get(mainFrame.getCurrentAreaName()).getlinePanel();
				MyLabel jLabel = new MyLabel(" " + hm_textPane.get(mainFrame.getCurrentAreaName())
				.getTextPane().getLine());
				LineNumVc.add(jLabel);//行号集合
				linePanel.add(jLabel);
			}
		} catch (BadLocationException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		if (e.getOffset() > 0) {
			try {
				String lookahead = e.getDocument().getText(e.getOffset() - 1, 1);
				if (lookahead.equals("\n")) {
					isNewLine = true;
				}else {
					isNewLine = false;
				}
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		try {
			Robot robot = new Robot();
			try {
				int offset = e.getOffset();
				String Bracket = e.getDocument().getText(offset, 1);
				if (Bracket.equals("{")) {
					robot.keyPress(93);//"{}"
					robot.keyRelease(93);
				}
				else if (Bracket.equals("(")) {
					robot.keyPress(48);//"()"
					robot.keyRelease(48);
				}
				else if (Bracket.equals("\"")) {
					robot.keyPress(KeyEvent.VK_QUOTE);
					robot.keyRelease(KeyEvent.VK_QUOTE); //"\""??
				}
//				else if (Bracket.equals("\'")) {
//					robot.keyPress(KeyEvent.VK_QUOTE);
//					robot.keyRelease(KeyEvent.VK_QUOTE);
//				}
				if (Bracket.equals("\t")) {
				}
				//System.out.println(Bracket);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (AWTException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}
	
	//删除更新
	@Override
	public void removeUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub

		jtp.addCaretListener(new CaretListener() {
			//光标监听，退格键删除的那个字符为preChar;
			//curChar预读的字符
			@Override
			public void caretUpdate(CaretEvent e) {
				// TODO Auto-generated method stub
				if (jtp.getCaretPosition() > 0) {
					try {
						curChar = jtp.getDocument().getText(jtp.getCaretPosition() - 1, 1);
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		preChar = curChar;//将当前字符赋值为前一个字符，在remove更新的时候用
//			try {
//				
//				if (jtp.getCaretPosition() > 0) {
//					curChar = jtp.getDocument().getText(jtp.getCaretPosition() - 1, 1);
//				}
//			} catch (BadLocationException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
		//当退格掉的是换行符的时候
		Thread.yield();
		if (preChar.equals("\n")) {
			int temp_lineNum = jtp.getCaretLine();//删除掉的行
			vst.removeNode(temp_lineNum);//将该行的第一代节点移除
			currentNodeSet.remove(temp_lineNum);//将该行显示节点移除
			
			HashMap<String, MainJpanel> hm_textPane = mainFrame.getHashTextPane();
			hm_textPane.get(mainFrame.getCurrentAreaName()).getTextPane().back();
			JPanel linepane = hm_textPane.get(mainFrame.getCurrentAreaName()).getlinePanel();
			int lineCount = hm_textPane.get(mainFrame.getCurrentAreaName()).getTextPane().getLine();
			System.out.println(lineCount);
			LineNumVc.remove(lineCount);
			linepane.remove(lineCount);
			linepane.updateUI();
		}
		if (e.getOffset() > 0) {
			try {
				String lookahead = e.getDocument().getText(e.getOffset() - 1, 1);
				if (lookahead.equals("\n")) {
					isNewLine = true;
				}else {
					isNewLine = false;
				}
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		try {
			try {
				RexPlay rPlay = new RexPlay(jtp.getDocument().getText(0
						, jtp.getDocument().getLength()));
				vc_lan_token = rPlay.getLanToken();
				vc_normal_token = rPlay.getNormalToken();
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
