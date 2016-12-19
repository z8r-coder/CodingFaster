package com.cqu.roy.mywdiget;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

public class MyLabel extends JLabel{
	public MyLabel(String text) {
		// TODO Auto-generated constructor stub
		super(text);
		setForeground(Color.GRAY);
		setFont(new Font("微软雅黑", Font.BOLD, 16));
	}
}
