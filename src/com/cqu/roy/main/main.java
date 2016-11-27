package com.cqu.roy.main;

import com.cqu.roy.mainframe.MainFrame;
import com.cuq.roy.mywdiget.SaveDialog;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainFrame mf = new MainFrame();
		System.out.println(1);
		SaveDialog sDialog = new SaveDialog(mf, true, 500, 300);
	}
}
