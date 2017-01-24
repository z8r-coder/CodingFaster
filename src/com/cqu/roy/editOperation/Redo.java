package com.cqu.roy.editOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import com.cqu.roy.attribute.TextAtrr;
import com.cqu.roy.fileOperation.FileOperation;
import com.cqu.roy.historyStorage.Node;
import com.cqu.roy.historyStorage.VersionTree;
import com.cqu.roy.historyStorage.historyInfo;
import com.cqu.roy.mainframe.MainFrame;
import com.cqu.roy.mywdiget.JpathButton;
import com.cqu.roy.mywdiget.MainJpanel;
import com.cqu.roy.mywdiget.MyFontStyle;
import com.cqu.roy.mywdiget.MyJTextPane;

public class Redo implements FileOperation{
	//版本树策略的栈
	private Stack<HashSet<Integer>> RedoStack_vst;
	private Stack<HashSet<Integer>> UndoStack_vst;
	//文本策略的栈
	private Stack<historyInfo> RedoStack_text;
	private Stack<historyInfo> UndoStack_text;
	private MainFrame mainFrame;
	private MyJTextPane jtp;
	private VersionTree vst;
	private ArrayList<Node> currentNodeSet;
	@Override
	public void use(JPanel jp, JScrollPane jsp, JPanel northjp, Vector<Integer> close_id, Vector<Integer> untitled_vc,
			Vector<String> sequece_name, String currentAreaName, JpathButton currentButton,
			HashMap<String, MainJpanel> hmTextArea, HashMap<String, TextAtrr> hm_name_atrr,
			HashMap<String, JpathButton> hm_name_btn) {
		// TODO Auto-generated method stub
		mainFrame = MainFrame.getInstance();
		MainJpanel mainJp = hmTextArea.get(mainFrame.getCurrentAreaName());
		if (mainJp != null) {
			jtp = mainJp.getTextPane();
		}else {
			return;
		}
		//版本树策略
		//versionTreeStrategy();
		//文本策略
		textStrategy();
	}
	public void versionTreeStrategy() {
		vst = jtp.getVersionTree();//版本树
		currentNodeSet = vst.getCurrentNodeSet();//当前版本节点集合
		RedoStack_vst = jtp.getRedoStack_vst();
		UndoStack_vst = jtp.getUndoStack_vst();
		HashSet<Integer> modified = null;
		if (!RedoStack_vst.isEmpty()) {
			modified = RedoStack_vst.pop();
		}else {
			return;
		}
		Iterator<Integer> iterator = modified.iterator();
		//文本样式
		StyledDocument document = jtp.getStyledDocument();
		MyFontStyle myFontStyle = new MyFontStyle(document);
		document = myFontStyle.getStyleDoc();
		jtp.setStyledDocument(document);
		
		while(iterator.hasNext()){
			int lineNum = iterator.next();
			//当前节点
			Node current_node = currentNodeSet.get(lineNum);
			//当前节点的子节点
			Node sub_node = current_node.getSubNode();
			//若子节点为空，则进行下一个节点
			if (sub_node == null) {
				continue;
			}
			//当前节点属性
			int current_startPositon = current_node.getText().getStartPostion();
			int current_length = current_node.getText().getLength();
			//其子节点属性
			int sub_startPosition = sub_node.getText().getStartPostion();
			String sub_text = sub_node.getText().getText();
			int sub_caretPosition = sub_node.getCaretPosition();
			try {
				jtp.getDocument().remove(current_startPositon, current_length);
				jtp.getDocument().insertString(sub_startPosition, sub_text
						, document.getStyle("Style06"));
				jtp.setCaretPosition(sub_caretPosition);
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//重新设置节点集合
			if (lineNum < jtp.getLine()) {
				currentNodeSet.set(lineNum, sub_node);
			}
		}	
		//将Redo栈中pop出的元素，压栈入Undo栈
		UndoStack_vst.push(modified);
		//System.out.println("here is Redo");
	}
	public void textStrategy() {
		RedoStack_text = jtp.getRedoStack_text();
		UndoStack_text = jtp.getUndoStack_text();
		
		//文本样式
		StyledDocument document = jtp.getStyledDocument();
		MyFontStyle myFontStyle = new MyFontStyle(document);
		document = myFontStyle.getStyleDoc();
		jtp.setStyledDocument(document);
		//获取下一个历史
		historyInfo hif = null;
		if (!RedoStack_text.isEmpty()) {
			//若栈内非空
			hif = RedoStack_text.pop();
		}else {
			//栈空则直接结束
			return;
		}
		
		String textStr = hif.getTextInfo();
		int caretPosition = hif.getCaretPosition();
		
		try {
			jtp.getDocument().remove(0, jtp.getDocument().getLength());
			jtp.getDocument().insertString(0, textStr, document.getStyle("Style06"));
			jtp.setCaretPosition(caretPosition);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//将当前History信息压如Undo栈
		UndoStack_text.push(jtp.getHistoryInfo());
		//并将弹出来的history设置成jtp的当前history
		jtp.setHistoryInfo(hif);
	}
}
