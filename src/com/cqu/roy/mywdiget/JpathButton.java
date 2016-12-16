package com.cqu.roy.mywdiget;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import com.cqu.roy.fileOperation.newFile;


public class JpathButton extends JButton{
	private String MapFilePath;
	
	public JpathButton(String text,String path) {
		// TODO Auto-generated constructor stub
		super(text);
		this.MapFilePath = path;
		setBackground(new Color(50, 50, 50));
		setFocusPainted(false);
	}
	
	public void setMapFilePath(String mapFilePath) {
		MapFilePath = mapFilePath;
	}
	
	public String getMapFilePath() {
		return MapFilePath;
	}
	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub
		super.setText(text);
	}
}
