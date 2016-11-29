package com.cqu.roy.fileOperation;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import com.cqu.roy.attribute.TextAtrr;
import com.cqu.roy.attribute.writeAndread;
import com.cqu.roy.constant.ButtonMsg;
import com.cqu.roy.mainframe.MainFrame;
import com.cqu.roy.mywdiget.JpathButton;

public class SaveAs implements FileOperation{
	
	writeAndread war = new writeAndread();
	private SaveAssistant sass;
	MainFrame mainFrame = MainFrame.getInstance();
	private HashMap<String, JTextPane> hmTextArea;
	private HashMap<String, JpathButton> hm_name_btn;
	private HashMap<String, TextAtrr> hm_name_atrr;
	
	@Override
	public void use(JPanel jp, JScrollPane jsp, JPanel northjp, Vector<Integer> close_id, Vector<Integer> untitled_vc,
			Vector<String> sequece_name, String currentAreaName, JpathButton currentButton,
			HashMap<String, JTextPane> hmTextArea, HashMap<String, TextAtrr> hm_name_atrr,
			HashMap<String, JpathButton> hm_name_btn) {
		// TODO Auto-generated method stub
		
		if (currentAreaName == null || MainFrame.fileCount != 0) {
			return;
		}
		
		this.hmTextArea = hmTextArea;
		this.hm_name_btn = hm_name_btn;
		this.hm_name_atrr = hm_name_atrr;
		
		TextAtrr textAtrr = hm_name_atrr.get(currentAreaName);
	
			JFileChooser jf = new JFileChooser();
			MainFrame.fileCount++;
			int value = jf.showSaveDialog(null);//阻塞
			if (value == JFileChooser.APPROVE_OPTION) {
				sass = new SaveAssistant(hmTextArea, hm_name_btn, hm_name_atrr);
				currentAreaName = sass.saveAssistant(jf, textAtrr, currentAreaName, war, close_id
						, untitled_vc, sequece_name);
			}
			else {
				MainFrame.fileCount--;
				MainFrame.sureOrcancel = ButtonMsg.CANCLE;
				mainFrame.requestFocus();
		}
	}
}
