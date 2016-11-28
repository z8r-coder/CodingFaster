package com.cqu.roy.mywdiget;

import javax.swing.JButton;

public class JpathButton extends JButton{
	private String MapFilePath;
	public JpathButton(String text,String path) {
		// TODO Auto-generated constructor stub
		super(text);
		this.MapFilePath = path;
	}
	
	public void setMapFilePath(String mapFilePath) {
		MapFilePath = mapFilePath;
	}
	
	public String getMapFilePath() {
		return MapFilePath;
	}
}
