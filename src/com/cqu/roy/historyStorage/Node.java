package com.cqu.roy.historyStorage;

public class Node {
	private int lineNumber;//存储的行号位置
	private TextInfo textinfo;//存储的具体内容
	private boolean isCurrentNode;//是否是当前node版本
	private boolean isLeaf;//是否是叶子节点
	private boolean isRoot;//是否是根节点
	private int nextLineCount;//下一个操作的行号，前提是该节点非叶子节点
	private int previousLineCount;//上一个操作的行号，前提是该节点的上一个节点非根节点
	private Node ParentNode;//父节点
	private Node SubNode;//子节点
	private int caretPosition;//光标的位置
	public Node(TextInfo textInfo,int lineNumber,int nextLineCount,int previousLineCount
			,Node parentNode,Node SubNode,int caretPosition) {
		// TODO Auto-generated constructor stub
		this.textinfo = textInfo;
		this.lineNumber = lineNumber;
		this.nextLineCount = nextLineCount;
		this.previousLineCount = previousLineCount;
		this.SubNode = SubNode;
		this.ParentNode = parentNode;
		this.caretPosition = caretPosition;
	}
	
	public void setParentNode(Node ParentNode) {
		this.ParentNode = ParentNode;
	}
	
	public Node getParentNode() {
		return ParentNode;
	}
	public void setSubNode(Node SubNode) {
		this.SubNode = SubNode;
	}
	
	public Node getSubNode() {
		return SubNode;
	}
	
	public void setText(TextInfo textinfo) {
		this.textinfo = textinfo;
	}
	
	public TextInfo getText() {
		return textinfo;
	}
	
	public void setlineNum(int lineNum) {
		this.lineNumber = lineNum;
	}
	public int getLineNum() {
		return lineNumber;
	}
	
	public void setIsCurrentNode(boolean isCurrentNode) {
		this.isCurrentNode = isCurrentNode;
	}
	
	public boolean getIsCurrentNode() {
		return isCurrentNode;
	}
	
	public void setIsLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	
	public boolean getIsLeaf() {
		return isLeaf;
	}
	
	public void setIsRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}
	
	public boolean getIsRoot() {
		return isRoot;
	}
	
	public void setNextLineCount(int nextLineCount) {
		this.nextLineCount = nextLineCount;
	}
	public int getNextLineCount() {
		return nextLineCount;
	}
	public void setPreviousLineCount(int previousLineCount) {
		this.previousLineCount = previousLineCount;
	}
	public int getPreviousLineCount() {
		return previousLineCount;
	}
	public void setCaretPosition(int caretPosition) {
		this.caretPosition = caretPosition;
	}
	public int getCaretPosition() {
		return caretPosition;
	}
}
