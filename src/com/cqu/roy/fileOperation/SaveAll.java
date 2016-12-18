package com.cqu.roy.fileOperation;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.cqu.roy.attribute.TextAtrr;
import com.cqu.roy.attribute.writeAndread;
import com.cqu.roy.main.main;
import com.cqu.roy.mainframe.MainFrame;
import com.cqu.roy.mywdiget.JpathButton;
import com.cqu.roy.mywdiget.MyJTextPane;

public class SaveAll implements FileOperation{
	
	MainFrame mainFrame = MainFrame.getInstance();
	writeAndread war = new writeAndread();
	private SaveAssistant sass;
	
	@Override
	public void use(JPanel jp, JScrollPane jsp, JPanel northjp, Vector<Integer> close_id, Vector<Integer> untitled_vc,
			Vector<String> sequece_name, String currentAreaName, JpathButton currentButton,
			HashMap<String, MyJTextPane> hmTextArea, HashMap<String, TextAtrr> hm_name_atrr,
			HashMap<String, JpathButton> hm_name_btn) {
		// TODO Auto-generated method stub
		
		Vector<String> image_sequece = new Vector<>();
		image_sequece = (Vector<String>) sequece_name.clone();
		
		//当前路径，用来获取当前路径修改后的路径
		String temp_Name = mainFrame.getCurrentAreaName();
		String Update_Name = null;//修改后的路径;
		
		for(String fileName:image_sequece){
			TextAtrr textAtrr = hm_name_atrr.get(fileName);
			if (!textAtrr.getisSave()) {
				JFileChooser jf = new JFileChooser();
				MainFrame.fileCount++;
				int value = jf.showSaveDialog(null);//阻塞
				if (value == JFileChooser.APPROVE_OPTION) {
					sass = new SaveAssistant(hmTextArea, hm_name_btn, hm_name_atrr);
					String newName = sass.saveAssistant(jf, textAtrr, fileName, war, close_id
							, untitled_vc, sequece_name);
					//保存当前页路径和btn，由当前页执行save all files
					if (temp_Name == fileName) {
						Update_Name = newName;
					}
				}else {
					MainFrame.fileCount--;
					mainFrame.requestFocus();
				}
			}else {
				/*当文件并非第一次创建的时候，已经保存过了
				 * 会弹出选择窗口*/
				File file = new File(textAtrr.getFileAddress());
				war.saveTo(file, hmTextArea.get(fileName).getText());
			}
		}
		if (Update_Name != null) {
			mainFrame.setCurrentAreaName(Update_Name);
			mainFrame.setCurrentButton(hm_name_btn.get(Update_Name));
		}
	}
}
