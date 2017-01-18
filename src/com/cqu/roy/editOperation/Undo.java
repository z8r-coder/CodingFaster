package com.cqu.roy.editOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;

import com.cqu.roy.attribute.TextAtrr;
import com.cqu.roy.fileOperation.FileOperation;
import com.cqu.roy.highlighting.SyntaxHighlighter;
import com.cqu.roy.historyStorage.Node;
import com.cqu.roy.historyStorage.VersionTree;
import com.cqu.roy.mainframe.MainFrame;
import com.cqu.roy.mywdiget.JpathButton;
import com.cqu.roy.mywdiget.MainJpanel;
import com.cqu.roy.mywdiget.MyJTextPane;

//此只是一个undo操作，具体数据结构在jtp里
public class Undo implements FileOperation{
	private Stack<HashSet<Integer>> UndoStack;//Undo栈
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
		jtp = hmTextArea.get(mainFrame.getCurrentAreaName()).getTextPane();
		vst = jtp.getVersionTree();//获取版本树
		currentNodeSet = vst.getCurrentNodeSet();//获取当前节点集合
		UndoStack = jtp.getUndoStack();
		HashSet<Integer> modified = UndoStack.pop();
		Iterator<Integer> iterator = modified.iterator();
		while(iterator.hasNext()){
			int lineNum = iterator.next();
			Node node = currentNodeSet.get(lineNum);
			int startPosition = node.getText().getStartPostion();
			int length = node.getText().getLength();
			try {
				jtp.getDocument().remove(startPosition, length);
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("here is undo");
	}
}
