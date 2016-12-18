package com.cqu.roy.mywdiget;

import javax.swing.JPanel;

public class MainJpanel extends JPanel{
	private MyJTextPane myJTextPane;
	private JPanel linePanel;
	public MainJpanel() {
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
