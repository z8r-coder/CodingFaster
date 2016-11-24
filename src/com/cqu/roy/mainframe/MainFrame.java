/*整体布局
 * 滚轮jpanel放center
 * 放置切换当前文本域的按钮放在north*/
package com.cqu.roy.mainframe;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.cqu.roy.attribute.TextAtrr;
import com.cqu.roy.attribute.writeAndread;
import com.cqu.roy.constant.ItemName;
import com.cqu.roy.constant.LenthAll;

public class MainFrame extends JFrame implements ActionListener{
	private JPanel jp;
	private JScrollPane jsp;
	private JMenuBar bar;
	private JMenu Filemenu;
	private JMenu Editmenu;
	
	private BorderLayout bLayout = new BorderLayout();
	private GridLayout gridLayout = new GridLayout(1, 6);
	
	private JPanel northjp;
	
	private String currentAreaName = null;//当前聚焦的文本
	private JButton currentButton = null;//当前聚焦的文本按钮
	
	private writeAndread war = new writeAndread();
	
	//每次页面中发生变化，需要维护的变量
	private HashMap<String,JTextArea> hmTextArea = new HashMap<>();//名字和文本域的映射
	private HashMap<String, JButton> hm_name_btn = new HashMap<>();//名字和按钮的映射
	private HashMap<String, TextAtrr> hm_name_atrr = new HashMap<>();//名字与具体对象的映射
	private Vector<Integer> untitled_vc = new Vector<>();//未保存的id集合
	private Vector<Integer> close_id = new Vector<>();//保留ID,如1234,把2关闭啦，close_id保存数字2,然后下一个new 的时候命名为2
	private Vector<String> sequece_name = new Vector<>();//文件打开的序列;
	
	public MainFrame() {
		// TODO Auto-generated constructor stub
		Toolkit tool = getToolkit();
		Dimension dim = tool.getScreenSize();
		setLocation((int)(dim.getWidth() - LenthAll.WINDOW_WIDTH) / 2,
				(int)(dim.getHeight() - LenthAll.WINDOW_HEIGHT) / 2);
		setSize(LenthAll.WINDOW_WIDTH, LenthAll.WINDOW_HEIGHT);
		setTitle("CodingFaster");
		
		jp = (JPanel) getContentPane();
		jp.setLayout(bLayout);
		
		northjp = new JPanel();
		northjp.setLayout(gridLayout);
		northjp.setSize(jp.getSize().width, 40);
	
		jsp = new JScrollPane();//滚轮
		jp.add(northjp, BorderLayout.NORTH);//北部中套用另一个布局管理器
		/*菜单*/
		bar = new JMenuBar();
		initFileMenu();
		initEditMenu();
		setJMenuBar(bar);
		
		setVisible(true);
 	}
	
	private void initFileMenu(){
		
		Filemenu = new JMenu("File");
		for(int i = 0; i < ItemName.selectionName.length;i++){
			JMenuItem item = new JMenuItem(ItemName.selectionName[i]);
			item.addActionListener(this);
			Filemenu.add(item);
		}
		bar.add(Filemenu);
	}
	
	private void initEditMenu(){
		String []selectionName = {
			"Undo","Redo","Undo selection"
		};
		
		Editmenu = new JMenu("Edit");
		bar.add(Editmenu);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals(ItemName.selectionName[0])) {// new file
			newFile();
		}
		else if (e.getActionCommand().equals(ItemName.selectionName[1])) {//open file
			openOp();
		}
		else if (e.getActionCommand().equals(ItemName.selectionName[2])) {//open folder

		}
		else if (e.getActionCommand().equals(ItemName.selectionName[3])) {//New view into file
			
		}
		else if (e.getActionCommand().equals(ItemName.selectionName[4])) {//save
			saveOp();
		}
		else if (e.getActionCommand().equals(ItemName.selectionName[5])) {//save as
			
		}
		else if (e.getActionCommand().equals(ItemName.selectionName[6])) {//save all
			
		}
		else if (e.getActionCommand().equals(ItemName.selectionName[7])) {//new window
			
		}
		else if (e.getActionCommand().equals(ItemName.selectionName[8])) {//Close window
			
		}
		else if (e.getActionCommand().equals(ItemName.selectionName[9])) {//Close file
			closeFileOp();
		}
		else if (e.getActionCommand().equals(ItemName.selectionName[10])) {//Close all file
			clossAllFileOp();
		}
		else if (e.getActionCommand().equals(ItemName.selectionName[11])) {//exit
			System.exit(0);
		}
	}
	//new
	private void newFile(){
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
		currentAreaName = "untitled" + id;
		
		hm_name_atrr.put(currentAreaName, textAtrr);
		sequece_name.add(currentAreaName);

		JTextArea jta = new JTextArea();
		JButton switchbtn = new JButton(currentAreaName);
		switchbtn.setSize(100, LenthAll.BUTTON_HEIGHT);
		northjp.add(switchbtn);
		switchbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentAreaName != switchbtn.getText()) {
					jsp.remove(hmTextArea.get(currentAreaName));
					currentAreaName = switchbtn.getText();
					currentButton = switchbtn;
					jsp.setViewportView(hmTextArea.get(currentAreaName));
					jsp.updateUI();
				}
			}
		});
		currentButton = switchbtn;
		hm_name_btn.put(switchbtn.getText(), switchbtn);
		hmTextArea.put(currentAreaName, jta);
		
		jp.add(jsp,BorderLayout.CENTER);

		jsp.setViewportView(jta);
		jp.updateUI();
		northjp.updateUI();
	}
	//save
	private void saveOp(){

		TextAtrr textAtrr = hm_name_atrr.get(currentAreaName);
		
		if (hmTextArea.size() != 0 && (!textAtrr.getisSave())) {
			JFileChooser jf = new JFileChooser();
			int value = jf.showSaveDialog(null);
			if (value == JFileChooser.APPROVE_OPTION) {
				File file = jf.getSelectedFile();
				/*当打开文件是第一次被保存时候，向hashmap中添加条目
				 * 并且会弹出窗口选择*/
				textAtrr.setFilename(file.getName());
				textAtrr.setFileAddress(file.getPath());
				textAtrr.setisSave(true);
				war.saveTo(file, hmTextArea.get(currentAreaName).getText());//写入文件
				JTextArea temp_area = hmTextArea.get(currentAreaName);
				JButton temp_btn = hm_name_btn.get(currentAreaName);
				TextAtrr temp_atrr = hm_name_atrr.get(currentAreaName);
				
				removeMap(currentAreaName);
				
				close_id.add(textAtrr.getID());//将该文本域的ID加入缺省ID集合
				untitled_vc.remove(currentAreaName);
				int index = sequece_name.indexOf(currentAreaName);
				sequece_name.remove(index);
				
				currentAreaName = file.getName();
				sequece_name.insertElementAt(currentAreaName, index);
				
				addMap(currentAreaName, temp_area, temp_btn, temp_atrr);
				currentButton.setText(file.getName());
			}
		}else if (textAtrr.getisSave()) {
			/*当文件并非第一次创建的时候，已经保存过了
			 * 会弹出选择窗口*/
			File file = new File(textAtrr.getFileAddress());
			war.saveTo(file, hmTextArea.get(currentAreaName).getText());
		}
	}
	//open
	private void openOp(){
		JFileChooser jf = new JFileChooser();
		JTextArea jta = new JTextArea();
		JButton btn = new JButton();
		
		TextAtrr textAtrr;
		
		int value = jf.showOpenDialog(null);
		if (value == JFileChooser.APPROVE_OPTION) {
			jp.updateUI();
			File file = jf.getSelectedFile();
			textAtrr = new TextAtrr(true, 0, file.getName(), file.getPath());
			JTextArea finishWritenArea;
			if (file.isFile() && file.exists()) {
				finishWritenArea = war.openFrom(file, jta);//写入程序,返回值为已经写入文本的area
				
				jsp.setViewportView(finishWritenArea);
				
				currentAreaName = file.getName();
				currentButton = btn;
				
				currentButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						if (currentAreaName != btn.getText()) {
							jsp.remove(hmTextArea.get(currentAreaName));
							currentAreaName = btn.getText();
							currentButton = btn;
							jsp.setViewportView(hmTextArea.get(currentAreaName));
							jsp.updateUI();
						}
					}
				});
				
				addMap(currentAreaName, finishWritenArea, btn, textAtrr);//添加属性
				
				btn.setText(currentAreaName);
				
				northjp.add(currentButton);
				sequece_name.add(currentAreaName);
				jp.add(jsp,BorderLayout.CENTER);
				jsp.setViewportView(jta);
			}
		}
	}
	//close file
	private void closeFileOp(){
		TextAtrr textAtrr = hm_name_atrr.get(currentAreaName);
		if (!textAtrr.getisSave()) {//未保存
			close_id.add(textAtrr.getID());
			untitled_vc.remove((Integer)(hm_name_atrr.get(currentAreaName).getID()));
		}

		jsp.remove(hmTextArea.get(currentAreaName));
		northjp.remove(currentButton);

		hm_name_atrr.remove(currentAreaName);
		hmTextArea.remove(currentAreaName);
		
		int index = sequece_name.indexOf(currentAreaName);
		if (sequece_name.size() == 1) {//如果只有一页，直接关掉即可
			//do nothing
		}
		else if (index == sequece_name.size() - 1) {
			sequece_name.remove(currentAreaName);
			currentAreaName = sequece_name.get(sequece_name.size() - 1);
			currentButton = hm_name_btn.get(currentAreaName);
		}else {
			sequece_name.remove(currentAreaName);
			currentAreaName = sequece_name.get(index);
			currentButton = hm_name_btn.get(currentAreaName);
		}
		jsp.setViewportView(hmTextArea.get(currentAreaName));
		jp.updateUI();
	}
	//close all file 
	private void clossAllFileOp(){
		jp.remove(jsp);
		northjp.removeAll();
		clearAllEl();
		jp.updateUI();
	}
	//当关闭页面上所有的page，需要clear所有需要维护的变量
	private void clearAllEl(){
		hm_name_atrr.clear();
		hm_name_btn.clear();
		hmTextArea.clear();
		
		sequece_name.removeAllElements();
		close_id.clear();
		untitled_vc.clear();
	}
	
	private void addMap(String name,JTextArea jTextArea,JButton btn,TextAtrr atrr){
		hmTextArea.put(name, jTextArea);
		hm_name_btn.put(name, btn);
		hm_name_atrr.put(name, atrr);
	}
	
	private void removeMap(String name){
		hmTextArea.remove(name);
		hm_name_atrr.remove(name);
		hm_name_btn.remove(name);
	}
}
