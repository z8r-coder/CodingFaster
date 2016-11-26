/*整体布局
 * 滚轮jpanel放center
 * 放置切换当前文本域的按钮放在north*/
package com.cqu.roy.mainframe;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.io.File;
import java.util.HashMap;
import java.util.Vector;

import javax.sound.midi.Sequence;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;

import com.cqu.roy.attribute.TextAtrr;
import com.cqu.roy.attribute.writeAndread;
import com.cqu.roy.constant.ItemName;
import com.cqu.roy.constant.KeyCode;
import com.cqu.roy.constant.LenthAll;
import com.cuq.roy.mywdiget.MyFontStyle;

public class MainFrame extends JFrame implements ActionListener{
	private JPanel jp;
	private JScrollPane jsp;
	private JMenuBar bar;
	private JMenu Filemenu;
	private JMenu Editmenu;
	
	private BorderLayout bLayout = new BorderLayout();
	private GridLayout gridLayout = new GridLayout(1, 6);
	
	private JPanel northjp;
	
	private MyFontStyle myFontStyle;//字体样式
	
	private String currentAreaName = null;//当前聚焦的文本
	private JButton currentButton = null;//当前聚焦的文本按钮
	
	private writeAndread war = new writeAndread();
	
	//每次页面中发生变化，需要维护的变量
	private HashMap<String,JTextPane> hmTextArea = new HashMap<>();//名字和文本域的映射
	private HashMap<String, JButton> hm_name_btn = new HashMap<>();//名字和按钮的映射
	private HashMap<String, TextAtrr> hm_name_atrr = new HashMap<>();//名字与具体对象的映射
	private Vector<Integer> untitled_vc = new Vector<>();//未保存的id集合
	private Vector<Integer> close_id = new Vector<>();//保留ID,如1234,把2关闭啦，close_id保存数字2,然后下一个new 的时候命名为2
	private Vector<String> sequece_name = new Vector<>();//文件打开的序列;
	
	//JFileChooser只能有一个
	private static int fileCount = 0;
	
	//组和键
	private boolean com_shift = false;
	private boolean com_ctrl = false;
	//file
	private boolean com_S = false;//save
	private boolean com_O = false;//open
	private boolean com_N = false;//new
	private boolean com_W = false;//close
	
	private boolean com_Z = false;//undo
	private boolean com_Y = false;//redo
	
	public MainFrame() {
		// TODO Auto-generated constructor stub
		Toolkit tool = getToolkit();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Dimension dim = tool.getScreenSize();
		setLocation((int)(dim.getWidth() - LenthAll.WINDOW_WIDTH) / 2,
				(int)(dim.getHeight() - LenthAll.WINDOW_HEIGHT) / 2);
		setSize(LenthAll.WINDOW_WIDTH, LenthAll.WINDOW_HEIGHT);
		setTitle("CodingFaster");
		
		jp = (JPanel) getContentPane();
		jp.setLayout(bLayout);
		SwingUtilities.updateComponentTreeUI(jp);
		northjp = new JPanel();
		northjp.setLayout(gridLayout);
		northjp.setSize(jp.getSize().width, 40);
	
		jsp = new JScrollPane();//滚轮
		jp.add(northjp, BorderLayout.NORTH);//北部中套用另一个布局管理器
		/*菜单*/
		bar = new JMenuBar();
		bar.setFont(new Font("粗体", Font.PLAIN, 5));
		initFileMenu();
		initEditMenu();
		setJMenuBar(bar);
		
		/*全局键盘监听*/
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {

			@Override
			public void eventDispatched(AWTEvent event) {
				// TODO Auto-generated method stub

				JTextPane currentArea = hmTextArea.get(currentAreaName);
				if (((KeyEvent)event).getID() == KeyEvent.KEY_PRESSED) {
					switch (((KeyEvent)event).getKeyCode()) {
					case KeyCode.CTRL:	
						if (currentArea != null) {
							currentArea.setEditable(false);//锁住area
						}
						com_ctrl = true;
						break;
					case KeyCode.SHIFT:
						com_shift = true;
						break;
					case KeyCode.S:
						com_S = true;
						break;
					case KeyCode.O:
						com_O = true;
						break;
					case KeyCode.N:
						com_N = true;
						break;
					case KeyCode.W:
						com_W = true;
						break;
					default:
						break;
					}
					//file
					//save
					if (com_S && com_ctrl) {
						saveOp();
					}
					//open
					else if (com_O && com_ctrl) {
						openOp();
					}
					//new
					else if (com_N && com_ctrl && !com_shift) {
						newFile();
					}
					else if (com_ctrl && com_W && !com_shift) {
						closeFileOp();
					}
					//new window
					else if (com_N && com_ctrl && com_shift) {
						newWindow();
					}
					//close window
					else if (com_W && com_ctrl && com_shift) {
						closeWindow();
					}
				}
				else if(((KeyEvent)event).getID() == KeyEvent.KEY_RELEASED){
					switch (((KeyEvent)event).getKeyCode()) {
					case KeyCode.CTRL:
						if (currentArea != null) {
							currentArea.setEditable(true);//解锁area
						}
						com_ctrl = false;
						break;
					case KeyCode.SHIFT:
						com_shift = false;
						break;
					case KeyCode.S:
						com_S = false;
						break;
					case KeyCode.O:
						com_O = false;
						break;
					case KeyCode.N:
						com_N = false;
						break;
					case KeyCode.W:
						com_W = false;
						break;
					default:
						break;
					}
				}
				else if (((KeyEvent)event).getID() == KeyEvent.KEY_TYPED) {

				}
			}
		}, AWTEvent.KEY_EVENT_MASK);
		setVisible(true);
 	}
	
	private void initFileMenu(){
		
		Filemenu = new JMenu("File");
		Filemenu.setFont(new Font("黑体", Font.PLAIN,15));
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
		else if (e.getActionCommand().equals(ItemName.selectionName[3])) {//save
			saveOp();
		}
		else if (e.getActionCommand().equals(ItemName.selectionName[4])) {//save as
			
		}
		else if (e.getActionCommand().equals(ItemName.selectionName[5])) {//save all
			saveAll();
		}
		else if (e.getActionCommand().equals(ItemName.selectionName[6])) {//new window
			newWindow();
		}
		else if (e.getActionCommand().equals(ItemName.selectionName[7])) {//Close window
			closeWindow();
		}
		else if (e.getActionCommand().equals(ItemName.selectionName[8])) {//Close file
			closeFileOp();
		}
		else if (e.getActionCommand().equals(ItemName.selectionName[9])) {//Close all file
			closeAllFileOp();
		}
		else if (e.getActionCommand().equals(ItemName.selectionName[10])) {//exit
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
		//存在前一个页面，要对前一个页面解锁
		
		if (currentAreaName != null) {
			hmTextArea.get(currentAreaName).setEditable(true);
			jsp.remove(hmTextArea.get(currentAreaName));//同时移除掉，前面一个的页面
		}
		currentAreaName = "untitled" + id;
		
		hm_name_atrr.put(currentAreaName, textAtrr);
		sequece_name.add(currentAreaName);

		JTextPane jtp = new JTextPane();
		textPaneStyle(jtp,"Style06");
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
					jsp.add(hmTextArea.get(currentAreaName));
					jsp.setViewportView(hmTextArea.get(currentAreaName));
					jsp.updateUI();
				}
			}
		});
		currentButton = switchbtn;
		hm_name_btn.put(switchbtn.getText(), switchbtn);
		hmTextArea.put(currentAreaName, jtp);
		
		jsp.add(jtp);
		jsp.setViewportView(jtp);
		jp.add(jsp,BorderLayout.CENTER);
		
		jp.updateUI();
		northjp.updateUI();
	}
	//save
	private void saveOp(){
		if (currentAreaName == null || fileCount != 0) {
			return;
		}
		TextAtrr textAtrr = hm_name_atrr.get(currentAreaName);
		
		if (hmTextArea.size() != 0 && (!textAtrr.getisSave())) {
			JFileChooser jf = new JFileChooser();
			fileCount++;
			int value = jf.showSaveDialog(null);//阻塞
			if (value == JFileChooser.APPROVE_OPTION) {
				fileCount--;
				File file = jf.getSelectedFile();
				/*当打开文件是第一次被保存时候，向hashmap中添加条目
				 * 并且会弹出窗口选择*/
				textAtrr.setFilename(file.getName());
				textAtrr.setFileAddress(file.getPath());
				textAtrr.setisSave(true);
				war.saveTo(file, hmTextArea.get(currentAreaName).getText());//写入文件
				JTextPane temp_area = hmTextArea.get(currentAreaName);
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
				this.requestFocus();
			}else {
				fileCount--;
				this.requestFocus();
			}
		}else if (textAtrr.getisSave()) {
			/*当文件并非第一次创建的时候，已经保存过了
			 * 会弹出选择窗口*/
			File file = new File(textAtrr.getFileAddress());
			war.saveTo(file, hmTextArea.get(currentAreaName).getText());
		}
	}
	//save all
	private void saveAll(){
//		JOptionPane.showConfirmDialog(jsp, "你确认要保存吗？", "确认窗口"
//				, JOptionPane.YES_NO_OPTION);
		
		Vector<String> image_sequece = new Vector<>();
		image_sequece = (Vector<String>) sequece_name.clone();

		for(String fileName:image_sequece){
			TextAtrr textAtrr = hm_name_atrr.get(fileName);
			if (!textAtrr.getisSave()) {
				JFileChooser jf = new JFileChooser();
				fileCount++;
				int value = jf.showSaveDialog(null);//阻塞
				if (value == JFileChooser.APPROVE_OPTION) {
					fileCount--;
					File file = jf.getSelectedFile();
					/*当打开文件是第一次被保存时候，向hashmap中添加条目
					 * 并且会弹出窗口选择*/
					textAtrr.setFilename(file.getName());
					textAtrr.setFileAddress(file.getPath());
					textAtrr.setisSave(true);
					
					war.saveTo(file, hmTextArea.get(fileName).getText());//写入文件
					
					JTextPane temp_area = hmTextArea.get(fileName);
					JButton temp_btn = hm_name_btn.get(fileName);
					TextAtrr temp_atrr = hm_name_atrr.get(fileName);
					
					removeMap(fileName);
					
					close_id.add(textAtrr.getID());//将该文本域的ID加入缺省ID集合
					untitled_vc.remove(fileName);
					int index = sequece_name.indexOf(fileName);
					sequece_name.remove(index);
					
					String newName = file.getName();
					sequece_name.insertElementAt(newName, index);
					
					addMap(newName, temp_area, temp_btn, temp_atrr);
					temp_btn.setText(file.getName());
					this.requestFocus();
					if (currentAreaName == fileName) {
						currentAreaName = newName;
					}
				}else {
					fileCount--;
					this.requestFocus();
				}
			}else {
				/*当文件并非第一次创建的时候，已经保存过了
				 * 会弹出选择窗口*/
				File file = new File(textAtrr.getFileAddress());
				war.saveTo(file, hmTextArea.get(fileName).getText());
			}
		}
	}
	//open
	private void openOp(){
		if (fileCount != 0) {
			return;
		}
		JFileChooser jf = new JFileChooser();
		JTextPane jtp = new JTextPane();
		JButton btn = new JButton();
		
		TextAtrr textAtrr;
		
		fileCount++;
		int value = jf.showOpenDialog(null);//此操作会阻塞
		if (value == JFileChooser.APPROVE_OPTION) {
			fileCount--;
			jp.updateUI();
			File file = jf.getSelectedFile();
			textAtrr = new TextAtrr(true, 0, file.getName(), file.getPath());
			JTextPane finishWritenArea;
			if (file.isFile() && file.exists()) {
				
				finishWritenArea = war.openFrom(file, jtp);//写入程序,返回值为已经写入文本的pane
				jsp.add(finishWritenArea);
				
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
				jsp.setViewportView(jtp);
				this.requestFocus();
			}
		}else{
			fileCount--;
			this.requestFocus();
		}
	}
	//close file
	private void closeFileOp(){
		if (currentAreaName == null) {
			return;
		}
		TextAtrr textAtrr = hm_name_atrr.get(currentAreaName);
		if (!textAtrr.getisSave()) {//未保存
			close_id.add(textAtrr.getID());
			untitled_vc.remove((Integer)(hm_name_atrr.get(currentAreaName).getID()));
		}

		jsp.remove(hmTextArea.get(currentAreaName));
		northjp.remove(currentButton);

		removeMap(currentAreaName);//维护表变量
		
		int index = sequece_name.indexOf(currentAreaName);
		if (sequece_name.size() == 1) {//如果只有一页，直接关掉即可
			sequece_name.remove(currentAreaName);
			currentAreaName = null;
			currentButton = null;
			jp.remove(jsp);
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
		if (currentAreaName != null) {
			jsp.setViewportView(hmTextArea.get(currentAreaName));
		}
		jp.updateUI();
	}
	//close all file 
	private void closeAllFileOp(){
		jp.remove(jsp);
		northjp.removeAll();
		clearAllEl();
		jp.updateUI();
	}
	//new a window
	private void newWindow() {
		new MainFrame();
	}
	//close current window
	private void closeWindow(){
		this.setVisible(false);
	}
	//当关闭页面上所有的page，需要clear所有需要维护的变量
	private void clearAllEl(){
		hm_name_atrr.clear();
		hm_name_btn.clear();
		hmTextArea.clear();
		
		sequece_name.removeAllElements();
		close_id.clear();
		untitled_vc.clear();
		
		currentAreaName = null;
		currentButton = null;
	}
	
	private void addMap(String name,JTextPane jtp,JButton btn,TextAtrr atrr){
		hmTextArea.put(name, jtp);
		hm_name_btn.put(name, btn);
		hm_name_atrr.put(name, atrr);
	}
	
	private void removeMap(String name){
		hmTextArea.remove(name);
		hm_name_atrr.remove(name);
		hm_name_btn.remove(name);
	}
	private void textPaneStyle(JTextPane jtp,String StyleName){
		StyledDocument styledDocument = jtp.getStyledDocument();
		MyFontStyle myFontStyle = new MyFontStyle(styledDocument);
		styledDocument = myFontStyle.getStyleDoc();
		styledDocument.setLogicalStyle(3, styledDocument.getStyle(StyleName));
		jtp.setStyledDocument(styledDocument);
	}
}
