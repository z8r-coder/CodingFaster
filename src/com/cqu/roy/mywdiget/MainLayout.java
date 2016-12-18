package com.cqu.roy.mywdiget;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class MainLayout extends BorderLayout{
	private MyJTextPane myJTextPane;
	private JPanel linePanel;
	public MainLayout() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public void setTextPane(MyJTextPane myJTextPane) {
		this.myJTextPane = myJTextPane;
	}
	public MyJTextPane getTextPane() {
		return myJTextPane;
	}
	public void setLinePanel(JPanel linePanel) {
		this.linePanel = linePanel;
	}
	public JPanel getlinePanel() {
		return linePanel;
	}
}
