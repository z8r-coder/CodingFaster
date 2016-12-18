package com.cqu.roy.fileOperation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.cqu.roy.attribute.TextAtrr;
import com.cqu.roy.attribute.writeAndread;
import com.cqu.roy.highlighting.SyntaxHighlighter;
import com.cqu.roy.mainframe.MainFrame;
import com.cqu.roy.mywdiget.JpathButton;
import com.cqu.roy.mywdiget.MainJpanel;
import com.cqu.roy.mywdiget.MainLayout;
import com.cqu.roy.mywdiget.MyJTextPane;
import com.cqu.roy.mywdiget.MyLabel;

public class OpenFile implements FileOperation{
	private MainFrame mainFrame = MainFrame.getInstance();
	private writeAndread war = new writeAndread();
	private HashMap<String, MainJpanel> hmTextArea;
	private HashMap<String, JpathButton> hm_name_btn;
	private HashMap<String, TextAtrr> hm_name_atrr;
	@Override
	public void use(JPanel jp, JScrollPane jsp, JPanel northjp, Vector<Integer> close_id, Vector<Integer> untitled_vc,
			Vector<String> sequece_name, String currentAreaName, JpathButton currentButton,
			HashMap<String, MainJpanel> hmTextArea, HashMap<String, TextAtrr> hm_name_atrr,
			HashMap<String, JpathButton> hm_name_btn) {
		// TODO Auto-generated method stub
		this.hmTextArea = hmTextArea;
		this.hm_name_atrr = hm_name_atrr;
		this.hm_name_btn = hm_name_btn;
		
		if (MainFrame.fileCount != 0) {
			return;
		}
		JFileChooser jf = new JFileChooser();
		//主panel
		MainJpanel mainJpanel = new MainJpanel();
		MainLayout mainLayout = new MainLayout();
		mainJpanel.setLayout(mainLayout);
		MyJTextPane jtp = new MyJTextPane();
		//背景色的设置
		jtp.setBackground(new Color(50, 50, 50));
		//设置文本监听，当文本改变时候，进行保留字的高亮渲染
		jtp.getDocument().addDocumentListener(new SyntaxHighlighter(jtp));
		jtp.setBorder(null);
		//前景色为白色
		jtp.setCaretColor(Color.WHITE);
		JpathButton btn;
		
		TextAtrr textAtrr;
		
		MainFrame.fileCount++;
		int value = jf.showOpenDialog(null);//此操作会阻塞
		if (value == JFileChooser.APPROVE_OPTION) {
			MainFrame.fileCount--;
			jp.updateUI();
			File file = jf.getSelectedFile();
			textAtrr = new TextAtrr(true, 0, file.getName(), file.getPath());
			MyJTextPane finishWritenArea;
			if (file.isFile() && file.exists()) {
				if (hm_name_btn.get(file.getPath()) == null) {
					JPanel linePanel = new JPanel();
					BoxLayout bLayout = new BoxLayout(linePanel, BoxLayout.Y_AXIS);
					linePanel.setBackground(new Color(50, 50, 50));
					linePanel.setLayout(bLayout);
					//linePanel.add(new MyLabel(" 1    "));
					finishWritenArea = war.openFrom(file, jtp,linePanel);//写入程序,返回值为已经写入文本的pane
					finishWritenArea.setLine(war.getLineNum());
					mainJpanel.add(finishWritenArea,BorderLayout.CENTER);
					mainJpanel.add(linePanel,BorderLayout.WEST);
					
					mainJpanel.setTextPane(finishWritenArea);
					mainJpanel.setLinePanel(linePanel);
					
					jsp.add(mainJpanel);
					jsp.setViewportView(mainJpanel);
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
								System.out.println(111);
								jsp.remove(hmTextArea.get(mainFrame.getCurrentAreaName()));
								mainFrame.setCurrentAreaName(btn.getMapFilePath());
								mainFrame.setCurrentButton(btn);
								jsp.add(hmTextArea.get(mainFrame.getCurrentAreaName()));
								jsp.setViewportView(hmTextArea.get(mainFrame.getCurrentAreaName()));
								jsp.updateUI();
							}
						}
					});
					addMap(currentAreaName, mainJpanel, btn, textAtrr);//添加属性
					
					
					btn.setText(file.getName());
					
					northjp.add(mainFrame.getCurrentButton());
					sequece_name.add(mainFrame.getCurrentAreaName());
					jp.add(jsp,BorderLayout.CENTER);
					mainFrame.requestFocus();
				}
				else {
					if (mainFrame.getCurrentAreaName() != file.getPath()) {
						jsp.remove(hmTextArea.get(mainFrame.getCurrentAreaName()));
						mainFrame.setCurrentAreaName(file.getPath());
						mainFrame.setCurrentButton(hm_name_btn.get(file.getPath()));
						jsp.setViewportView(hmTextArea.get(mainFrame.getCurrentAreaName()));
						jsp.updateUI();
					}
				}
			}
		}else{
			MainFrame.fileCount--;
			mainFrame.requestFocus();
		}
	}
	private void addMap(String name,MainJpanel mainJpanel,JpathButton btn,TextAtrr atrr){
		hmTextArea.put(name, mainJpanel);
		hm_name_btn.put(name, btn);
		hm_name_atrr.put(name, atrr);
	}
	
	private String getSuffix(String fileName){
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		return suffix;
	}
}
