package com.cqu.roy.mainframe;

import java.awt.Dimension;
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
import javax.swing.JTextArea;

import com.cqu.roy.constant.ItemName;
import com.cqu.roy.constant.LenthAll;

public class MainFrame extends JFrame implements ActionListener{
	private JPanel jp;
	private JMenuBar bar;
	private JMenu Filemenu;
	private JMenu Editmenu;
	private final static String encoding = "UTF-8";
	private HashMap<String,JTextArea> hmTextArea = new HashMap<String, JTextArea>();
	
	public MainFrame() {
		// TODO Auto-generated constructor stub
		Toolkit tool = getToolkit();
		Dimension dim = tool.getScreenSize();
		setLocation((int)(dim.getWidth() - LenthAll.WINDOW_WIDTH) / 2,
				(int)(dim.getHeight() - LenthAll.WINDOW_HEIGHT) / 2);
		setSize(LenthAll.WINDOW_WIDTH, LenthAll.WINDOW_HEIGHT);
		jp = (JPanel) getContentPane();
		//jp.setLayout(null);
		
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
			jp.add(jta);
			hmTextArea.put("untitled", jta);
			jp.updateUI();
		}
		else if (e.getActionCommand().equals(ItemName.selectionName[1])) {//open file
			JFileChooser jf = new JFileChooser();
			JTextArea jta;
			String lineText = null;
			int value = jf.showOpenDialog(null);
			if (value == JFileChooser.APPROVE_OPTION) {
				jta = new JTextArea();
				jp.add(jta);
				jp.updateUI();
				File file = jf.getSelectedFile();
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
			if (hmTextArea.size() == 0) {
				
			}else{
				JFileChooser jf = new JFileChooser();
				String lineTxt = null;
				int value = jf.showSaveDialog(null);
				if (value == JFileChooser.APPROVE_OPTION) {
					File file = jf.getSelectedFile();
					try {
						OutputStreamWriter osw = new OutputStreamWriter(
								new FileOutputStream(file),encoding);
						BufferedWriter bw = new BufferedWriter(osw);
						try {
	//						bw.write(hmTextArea.get("untitled").getText(), 0,
	//								hmTextArea.get("untitled").getText().length());
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
			
		}
		else if (e.getActionCommand().equals(ItemName.selectionName[10])) {//Close all file
			
		}
		else if (e.getActionCommand().equals(ItemName.selectionName[11])) {//exit
			System.exit(0);
		}
	}
}
