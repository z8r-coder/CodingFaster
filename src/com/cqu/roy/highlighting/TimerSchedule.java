package com.cqu.roy.highlighting;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.text.BadLocationException;

import com.cqu.roy.historyStorage.Node;
import com.cqu.roy.historyStorage.TextInfo;
import com.cqu.roy.historyStorage.VersionTree;
import com.cqu.roy.mywdiget.MyJTextPane;

//计时器，用于redo undo
//每1秒记录一次
public class TimerSchedule implements Runnable{
	private boolean isModified;
	private MyJTextPane jtp;
	private HashSet<Integer> modified;
	private VersionTree vst;
	private ArrayList<Node> currentNode;//显示结点
	private int count;//计数器，
	public TimerSchedule(boolean isModified,MyJTextPane jtp,HashSet<Integer> modified,VersionTree vst) {
		// TODO Auto-generated constructor stub
		this.isModified = isModified;
		this.jtp = jtp;
		this.modified = modified;
		this.vst = vst;
		currentNode = vst.getCurrentNodeSet();
		count = 0;
	}
	public void setIsModified(boolean isModified) {
		this.isModified = isModified;
	}
	//currentLine需要将非第一行的数据添加上换行符
	public String getModifiedString(int startPosition,int currentLine) {
		String str = null;
		int temp = startPosition;
		try {
			while(!jtp.getDocument().getText(temp, 1).equals("\n") && temp < jtp.getDocument().getLength()){
				temp++;
			}
			str = jtp.getDocument().getText(startPosition, temp - startPosition);
			if (currentLine != 0) {
				str = "\n" + str;
			}
		} catch (BadLocationException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	//同步存在很大的问题
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Iterator<Integer> iterator = modified.iterator();
			if (isModified) {
				while(iterator.hasNext()){
					int currentLine = (int)iterator.next();
					Node node = currentNode.get(currentLine);
					
					TextInfo textInfo = node.getText();
					int startPostion = textInfo.getStartPostion();
					if (currentLine == 0) {
						//产生新一代版本
						Node parentNode = currentNode.get(currentLine);
						String content = getModifiedString(startPostion, currentLine);
						TextInfo textInfo2 = new TextInfo(content, startPostion, 
								startPostion + content.length(), content.length());
						Node nextNode = new Node(textInfo2, currentLine, -1, -1, parentNode, null);
						parentNode.setSubNode(nextNode);
						currentNode.set(currentLine, nextNode);
					}else {
						Node parentNode = currentNode.get(currentLine);
						String content = getModifiedString(startPostion + 1, currentLine);
						TextInfo textInfo2 = new TextInfo(content, startPostion, 
								startPostion + content.length(), content.length());
						Node nextNode = new Node(textInfo2, currentLine, -1, -1, parentNode, null);
						parentNode.setSubNode(nextNode);
						currentNode.set(currentLine, nextNode);
					}
				}
			}
			for(int i = 0; i < currentNode.size();i++){
				System.out.println(currentNode.get(i).getText().getText());
			}
			modified.clear();
		}
	}
}
