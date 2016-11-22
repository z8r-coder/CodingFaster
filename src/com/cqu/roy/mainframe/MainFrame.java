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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Vector;

import javax.sound.sampled.AudioFormat.Encoding;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

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
	private int fileNumber = 0;//打开了多少个文本
	
	private final static String encoding = "UTF-8";
	private HashMap<String,JTextArea> hmTextArea = new HashMap<>();
	private HashMap<String, String> hm_name_address = new HashMap<>();
	
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
		
		jp.add(northjp, BorderLayout.NORTH);//北部中套用另一个布局管理器
	
		jsp = new JScrollPane();//滚轮
		
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
			JTextArea jta = new JTextArea();
			JButton switchbtn = new JButton("untitled");
			JButton ss = new JButton("ds");
			switchbtn.setSize(100, 20);
			ss.setSize(100, 20);
			northjp.add(switchbtn);
			northjp.add(ss);

			hmTextArea.put("untitled", jta);
			
			currentAreaName = "untitled";
			
			jp.add(jsp,BorderLayout.CENTER);

			jsp.setViewportView(jta);
			jp.updateUI();
			northjp.updateUI();
		}
		else if (e.getActionCommand().equals(ItemName.selectionName[1])) {//open file
			JFileChooser jf = new JFileChooser();
			JTextArea jta = new JTextArea();
			jp.add(jsp,BorderLayout.CENTER);
			jsp.setViewportView(jta);
			String lineText = null;
			
			int value = jf.showOpenDialog(null);
			if (value == JFileChooser.APPROVE_OPTION) {
				jp.updateUI();
				File file = jf.getSelectedFile();
				
				hm_name_address.put(file.getName(), file.getPath());
				hmTextArea.put(file.getName(), jta);
				
				currentAreaName = file.getName();
				
				if (file.isFile() && file.exists()) {
					try {
						InputStreamReader isr = new InputStreamReader(
								new FileInputStream(file),encoding);
						BufferedReader br = new BufferedReader(isr);
						try {
							while((lineText = br.readLine()) != null){
								jta.append(lineText);
								jta.append("\n");
							}
							br.close();
							isr.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
		else if (e.getActionCommand().equals(ItemName.selectionName[2])) {//open folder
			System.out.println(hmTextArea.get("untitled").getText());
		}
		else if (e.getActionCommand().equals(ItemName.selectionName[3])) {//New view into file
			
		}
		else if (e.getActionCommand().equals(ItemName.selectionName[4])) {//save
			if (hmTextArea.size() != 0 && hm_name_address.get(currentAreaName) == null) {
				JFileChooser jf = new JFileChooser();
				String lineTxt = null;
				int value = jf.showSaveDialog(null);
				if (value == JFileChooser.APPROVE_OPTION) {
					File file = jf.getSelectedFile();
					/*当打开文件是第一次被保存时候，向hashmap中添加条目
					 * 并且会弹出窗口选择*/
					hm_name_address.put(file.getName(), file.getPath());
					
					try {
						OutputStreamWriter osw = new OutputStreamWriter(
								new FileOutputStream(file),encoding);
						BufferedWriter bw = new BufferedWriter(osw);
						try {
							bw.write(hmTextArea.get("untitled").getText());
							bw.close();
							osw.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			
			}else if (hm_name_address.get(currentAreaName) != null) {
				/*当文件并非第一次创建的时候，
				 * 会弹出选择窗口*/
				File file = new File(hm_name_address.get(currentAreaName));
				try {
					OutputStreamWriter osw = new OutputStreamWriter(
							new FileOutputStream(file),encoding);
					BufferedWriter bw = new BufferedWriter(osw);
					try {
						bw.write(hmTextArea.get(currentAreaName).getText());
						bw.close();
						osw.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
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
			fileNumber--;
			jp.remove(jsp);
			hm_name_address.remove(currentAreaName);
			hmTextArea.remove(currentAreaName);
			jp.updateUI();
		}
		else if (e.getActionCommand().equals(ItemName.selectionName[10])) {//Close all file
			
		}
		else if (e.getActionCommand().equals(ItemName.selectionName[11])) {//exit
			System.exit(0);
		}
	}
}
