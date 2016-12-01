package com.cqu.roy.fileOperation;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;

import com.cqu.roy.attribute.TextAtrr;
import com.cqu.roy.constant.LenthAll;
import com.cqu.roy.mainframe.MainFrame;
import com.cqu.roy.mywdiget.JpathButton;
import com.cqu.roy.mywdiget.MyFontStyle;

//newFile
public class newFile implements FileOperation{
	MainFrame mainFrame = MainFrame.getInstance();
	@Override
	public void use(JPanel jp,JScrollPane jsp,JPanel northjp,Vector<Integer> close_id,Vector<Integer> untitled_vc
			,Vector<String> sequece_name,String currentAreaName,JpathButton currentButton,HashMap<String, JTextPane> hmTextArea 
			,HashMap<String, TextAtrr> hm_name_atrr,HashMap<String, JpathButton> hm_name_btn) {
		// TODO Auto-generated method stub
		int id;
		if (close_id.size() == 0) {
			id = untitled_vc.size() + 1;
			untitled_vc.add(id);
		}else {
			id = close_id.get(0);//最先被关闭的id
			close_id.remove((Integer)id);//该id已经被使用，移除！
			untitled_vc.add(id);
		}
		TextAtrr textAtrr = new TextAtrr(false, id, "untitled" + id, null);
		//存在前一个页面，要对前一个页面解锁
		
		if (currentAreaName != null) {
			hmTextArea.get(currentAreaName).setEditable(true);
			jsp.remove(hmTextArea.get(currentAreaName));//同时移除掉，前面一个的页面
		}
		currentAreaName = "untitled" + id;
		mainFrame.setCurrentAreaName(currentAreaName);//修改当前文件路径
		
		hm_name_atrr.put(currentAreaName, textAtrr);
		sequece_name.add(currentAreaName);

		JTextPane jtp = new JTextPane();
		textPaneStyle(jtp,"Style06");
		JpathButton switchbtn = new JpathButton(currentAreaName,currentAreaName);
		
		switchbtn.setSize(100, LenthAll.BUTTON_HEIGHT);
		northjp.add(switchbtn);
		
		mainFrame.setCurrentButton(switchbtn);//修改当前按钮
		
		hm_name_btn.put(switchbtn.getText(), switchbtn);
		hmTextArea.put(currentAreaName, jtp);
		mainFrame.getCurrentButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (mainFrame.getCurrentAreaName() != switchbtn.getMapFilePath()) {
					jsp.remove(hmTextArea.get(mainFrame.getCurrentAreaName()));
					mainFrame.setCurrentAreaName(switchbtn.getMapFilePath());
					mainFrame.setCurrentButton(switchbtn);
					jsp.add(hmTextArea.get(mainFrame.getCurrentAreaName()));
					jsp.setViewportView(hmTextArea.get(mainFrame.getCurrentAreaName()));
					jsp.updateUI();
				}
			}
		});
		
		jsp.add(jtp);
		jsp.setViewportView(jtp);
		jp.add(jsp,BorderLayout.CENTER);
		
		jp.updateUI();
		northjp.updateUI();
	}
	private void textPaneStyle(JTextPane jtp,String StyleName){
		StyledDocument styledDocument = jtp.getStyledDocument();
		MyFontStyle myFontStyle = new MyFontStyle(styledDocument);
		styledDocument = myFontStyle.getStyleDoc();
		styledDocument.setLogicalStyle(3, styledDocument.getStyle(StyleName));
		jtp.setStyledDocument(styledDocument);
	}
}
