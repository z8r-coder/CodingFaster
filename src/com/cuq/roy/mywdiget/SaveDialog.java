package com.cuq.roy.mywdiget;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class SaveDialog extends JDialog{
	private JLabel text;
	private JButton sureBtn;
	private JButton cancelBtn;
	private JButton closeWithOutSaveBtn;
	public SaveDialog(JFrame parent,boolean model,int width,int height) {
		// TODO Auto-generated constructor stub
		super(parent,model);
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = screensize.width;
		int screenheight = screensize.height;
		
		setSize(width, height);
		setLayout(null);
		setLocation((screenWidth - width) / 2, (screenheight - height) / 2);
		//setResizable(false);
		
		text = new JLabel("Save changes to AnotherPthread.c before closing?");
		sureBtn = new JButton("save");
		cancelBtn = new JButton("cancel");
		closeWithOutSaveBtn = new JButton("close without saving");
		add(text);
		add(sureBtn);
		add(cancelBtn);
		add(closeWithOutSaveBtn);
		sureBtn.setFont(new Font("华文彩云", Font.BOLD, 15));
		text.setLocation(50, 30);
		text.setSize(400,50);
		sureBtn.setLocation(300, 150);
		sureBtn.setSize(100, 40);
		setVisible(true);
	}
}
