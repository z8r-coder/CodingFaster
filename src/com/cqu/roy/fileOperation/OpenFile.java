package com.cqu.roy.fileOperation;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import com.cqu.roy.attribute.TextAtrr;
import com.cqu.roy.attribute.writeAndread;
import com.cqu.roy.mainframe.MainFrame;
import com.cqu.roy.mywdiget.JpathButton;

public class OpenFile implements FileOperation{
	private MainFrame mainFrame = MainFrame.getInstance();
	private writeAndread war = new writeAndread();
	private HashMap<String, JTextPane> hmTextArea;
	private HashMap<String, JpathButton> hm_name_btn;
	private HashMap<String, TextAtrr> hm_name_atrr;
	@Override
	public void use(JPanel jp, JScrollPane jsp, JPanel northjp, Vector<Integer> close_id, Vector<Integer> untitled_vc,
			Vector<String> sequece_name, String currentAreaName, JpathButton currentButton,
			HashMap<String, JTextPane> hmTextArea, HashMap<String, TextAtrr> hm_name_atrr,
			HashMap<String, JpathButton> hm_name_btn) {
		// TODO Auto-generated method stub
		this.hmTextArea = hmTextArea;
		this.hm_name_atrr = hm_name_atrr;
		this.hm_name_btn = hm_name_btn;
		
		if (MainFrame.fileCount != 0) {
			return;
		}
		JFileChooser jf = new JFileChooser();
		JTextPane jtp = new JTextPane();
		JpathButton btn;
		
		TextAtrr textAtrr;
		
		MainFrame.fileCount++;
		int value = jf.showOpenDialog(null);//此操作会阻塞
		if (value == JFileChooser.APPROVE_OPTION) {
			MainFrame.fileCount--;
			jp.updateUI();
			File file = jf.getSelectedFile();
			textAtrr = new TextAtrr(true, 0, file.getName(), file.getPath());
			JTextPane finishWritenArea;
			if (file.isFile() && file.exists()) {
				
				finishWritenArea = war.openFrom(file, jtp);//写入程序,返回值为已经写入文本的pane
				jsp.add(finishWritenArea);
				
				currentAreaName = file.getPath();
				
				btn = new JpathButton(file.getName(),file.getPath());
				
				textAtrr.setSuffix(getSuffix(currentAreaName));
				
				mainFrame.setCurrentButton(btn);//设置当前按钮
				mainFrame.setCurrentAreaName(currentAreaName);//设置当前路径
				
				btn.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						if (mainFrame.getCurrentAreaName() != btn.getMapFilePath()) {
							jsp.remove(hmTextArea.get(mainFrame.getCurrentAreaName()));
							//currentAreaName = btn.getMapFilePath();
							mainFrame.setCurrentAreaName(btn.getMapFilePath());
							//currentButton = btn;
							mainFrame.setCurrentButton(btn);
							jsp.setViewportView(hmTextArea.get(mainFrame.getCurrentAreaName()));
							jsp.updateUI();
						}
					}
				});
				addMap(currentAreaName, finishWritenArea, btn, textAtrr);//添加属性
				
				btn.setText(file.getName());
				
				northjp.add(mainFrame.getCurrentButton());
				sequece_name.add(mainFrame.getCurrentAreaName());
				jp.add(jsp,BorderLayout.CENTER);
				jsp.setViewportView(jtp);
				mainFrame.requestFocus();
			}
		}else{
			MainFrame.fileCount--;
			mainFrame.requestFocus();
		}
	}
	private void addMap(String name,JTextPane jtp,JpathButton btn,TextAtrr atrr){
		hmTextArea.put(name, jtp);
		hm_name_btn.put(name, btn);
		hm_name_atrr.put(name, atrr);
	}
	
	private String getSuffix(String fileName){
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		return suffix;
	}
}
