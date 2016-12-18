package com.cqu.roy.fileOperation;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JFileChooser;

import com.cqu.roy.attribute.TextAtrr;
import com.cqu.roy.attribute.writeAndread;
import com.cqu.roy.constant.ButtonMsg;
import com.cqu.roy.mainframe.MainFrame;
import com.cqu.roy.mywdiget.JpathButton;
import com.cqu.roy.mywdiget.MainJpanel;

public class SaveAssistant {
	private MainFrame mainFrame = MainFrame.getInstance();
	private HashMap<String, MainJpanel> hmTextArea;
	private HashMap<String, JpathButton> hm_name_btn;
	private HashMap<String, TextAtrr> hm_name_atrr;
	
	public SaveAssistant(HashMap<String, MainJpanel> hmTextArea,HashMap<String, JpathButton> hm_name_btn
			,HashMap<String, TextAtrr> hm_name_atrr) {
		// TODO Auto-generated constructor stub
		this.hmTextArea = hmTextArea;
		this.hm_name_btn = hm_name_btn;
		this.hm_name_atrr = hm_name_atrr;
	}
	String saveAssistant(JFileChooser jf,TextAtrr textAtrr,String fileName,writeAndread war
			,Vector<Integer> close_id,Vector<Integer> untitled_vc,Vector<String> sequece_name){
		MainFrame.fileCount--;
		MainFrame.sureOrcancel = ButtonMsg.SURE;//表示文件选择框是选择的确定按钮。
		File file = jf.getSelectedFile();
		/*当打开文件是第一次被保存时候，向hashmap中添加条目
		 * 并且会弹出窗口选择*/
		textAtrr.setFilename(file.getName());
		textAtrr.setFileAddress(file.getPath());
		textAtrr.setisSave(true);
		
		war.saveTo(file, hmTextArea.get(fileName).getTextPane().getText());//写入文件
		
		MainJpanel temp_area = hmTextArea.get(fileName);
		JpathButton temp_btn = hm_name_btn.get(fileName);
		TextAtrr temp_atrr = hm_name_atrr.get(fileName);
		
		removeMap(fileName);
		
		close_id.add(textAtrr.getID());//将该文本域的ID加入缺省ID集合
		untitled_vc.remove(fileName);
		int index = sequece_name.indexOf(fileName);
		sequece_name.remove(index);
		
		String newName = file.getPath();
		sequece_name.insertElementAt(newName, index);
		
		temp_btn.setMapFilePath(newName);
		temp_atrr.setSuffix(getSuffix(newName));//
		addMap(newName, temp_area, temp_btn, temp_atrr);
		temp_btn.setText(file.getName());
		
		mainFrame.setCurrentAreaName(newName);
		mainFrame.setCurrentButton(temp_btn);
		return newName;
	}
	private void removeMap(String name){
		hmTextArea.remove(name);
		hm_name_atrr.remove(name);
		hm_name_btn.remove(name);
	}
	private void addMap(String name,MainJpanel jtp,JpathButton btn,TextAtrr atrr){
		hmTextArea.put(name, jtp);
		hm_name_btn.put(name, btn);
		hm_name_atrr.put(name, atrr);
	}
	
	private String getSuffix(String fileName){
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		return suffix;
	}
}
