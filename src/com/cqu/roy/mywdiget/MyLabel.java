package com.cqu.roy.mywdiget;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

import com.cqu.roy.main.main;
import com.cqu.roy.mainframe.MainFrame;

public class MyLabel extends JLabel{
	private MainFrame mainFrame;
	private MyJTextPane jtp;
	private int labelFontSize;
	public MyLabel(String text) {
		// TODO Auto-generated constructor stub
		super(text);
		mainFrame = MainFrame.getInstance();
		jtp = mainFrame.getHashTextPane().get(mainFrame.getCurrentAreaName()).getTextPane();
		labelFontSize = jtp.getLabelFontSize();
		setForeground(Color.GRAY);
		setFont(new Font("微软雅黑", Font.BOLD, labelFontSize));
	}
	public void transformTheSize(int fontSize) {
		setForeground(Color.GRAY);
		setFont(new Font("微软雅黑", Font.BOLD, fontSize));
		updateUI();
	}
}
