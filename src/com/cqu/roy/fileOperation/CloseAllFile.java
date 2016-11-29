package com.cqu.roy.fileOperation;

import java.util.HashMap;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import com.cqu.roy.attribute.TextAtrr;
import com.cqu.roy.mywdiget.JpathButton;

public class CloseAllFile implements FileOperation{
	
	CloseFile cf = new CloseFile();
	
	@Override
	public void use(JPanel jp, JScrollPane jsp, JPanel northjp, Vector<Integer> close_id, Vector<Integer> untitled_vc,
			Vector<String> sequece_name, String currentAreaName, JpathButton currentButton,
			HashMap<String, JTextPane> hmTextArea, HashMap<String, TextAtrr> hm_name_atrr,
			HashMap<String, JpathButton> hm_name_btn) {
		// TODO Auto-generated method stub
		
		Vector<String> seq_clone = (Vector<String>) sequece_name.clone();
		for(String s:seq_clone){
			currentAreaName = s;
			currentButton = hm_name_btn.get(s);
			cf.use(jp, jsp, northjp, close_id, untitled_vc, sequece_name
					, currentAreaName, currentButton
					, hmTextArea, hm_name_atrr, hm_name_btn);
			
		}
	}

}
