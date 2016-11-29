package com.cqu.roy.fileOperation;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import com.cqu.roy.attribute.TextAtrr;
import com.cqu.roy.constant.ButtonMsg;
import com.cqu.roy.constant.LenthAll;
import com.cqu.roy.main.main;
import com.cqu.roy.mainframe.MainFrame;
import com.cqu.roy.mywdiget.JpathButton;
import com.cqu.roy.mywdiget.SaveDialog;

public class CloseFile implements FileOperation{
	private MainFrame mainFrame = MainFrame.getInstance();
	private SaveSingleOp saveSingleOp = new SaveSingleOp();
	private String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	private Icon icon = new ImageIcon("src/imageResources/warning.png");
	private Object[] selection = {"Save","Cancle","Close Without Saving"};
	
	private HashMap<String, JTextPane> hmTextArea;
	private HashMap<String, JpathButton> hm_name_btn;
	private HashMap<String, TextAtrr> hm_name_atrr;
	
	@Override
	public void use(JPanel jp, JScrollPane jsp, JPanel northjp, Vector<Integer> close_id, Vector<Integer> untitled_vc,
			Vector<String> sequece_name, String currentAreaName, JpathButton currentButton,
			HashMap<String, JTextPane> hmTextArea, HashMap<String, TextAtrr> hm_name_atrr,
			HashMap<String, JpathButton> hm_name_btn) {
		// TODO Auto-generated method stub
		System.out.println(sequece_name.size());
		if (currentAreaName == null) {
			return;
		}
		
		this.hmTextArea = hmTextArea;
		this.hm_name_btn = hm_name_btn;
		this.hm_name_atrr = hm_name_atrr;
		
		TextAtrr textAtrr = hm_name_atrr.get(currentAreaName);
		if (!textAtrr.getisSave()) {//未保存
			JLabel fontSet = new JLabel("Save changes to " + currentAreaName 
					+" before closing?");
			fontSet.setFont(new Font(fontNames[16], Font.BOLD, 15));
			int selectionValue = SaveDialog.showOptionDialog(mainFrame, fontSet, "warning"
					, JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, icon, selection
					, selection[0], LenthAll.DIALOG_WIDTH, LenthAll.DIALOG_HEIGHT);
			
			switch (selectionValue) {
			case 0://save
				saveSingleOp.use(jp, jsp, northjp, close_id, untitled_vc, sequece_name, currentAreaName
						, currentButton, hmTextArea, hm_name_atrr, hm_name_btn);
				if (MainFrame.sureOrcancel == ButtonMsg.CANCLE) {
					return;
				}
				break;
			case 1://cancel
				return;
			case 2://close without saving
				close_id.add(textAtrr.getID());
				untitled_vc.remove((Integer)(hm_name_atrr.get(mainFrame.getCurrentAreaName()).getID()));
				break;
			default:
				break;
			}
		}else{//已经保存
			JLabel fontSet = new JLabel("Save changes to " + currentAreaName 
					+" before closing?");
			fontSet.setFont(new Font(fontNames[16], Font.BOLD, 15));
			int selectionValue = SaveDialog.showOptionDialog(mainFrame, fontSet, "warning"
					, JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, icon, selection
					, selection[0], 500, 200);
			switch (selectionValue) {
			case 0://save
				saveSingleOp.use(jp, jsp, northjp, close_id, untitled_vc, sequece_name, currentAreaName
						, currentButton, hmTextArea, hm_name_atrr, hm_name_btn);
				break;
			case 1://cancel
				return;
			case 2://close without saving
				break;
			default:
				break;
			}
		}

		jsp.remove(hmTextArea.get(currentAreaName));
		northjp.remove(currentButton);

		removeMap(mainFrame.getCurrentAreaName());//维护表变量
		
		int index = sequece_name.indexOf(mainFrame.getCurrentAreaName());
		if (sequece_name.size() == 1) {//如果只有一页，直接关掉即可
			sequece_name.remove(currentAreaName);
			mainFrame.setCurrentAreaName(null);//并将当前页面和按钮置为空
			mainFrame.setCurrentButton(null);
			jp.remove(jsp);
		}
		//如过是最后一页，则显示前面一页
		else if (index == sequece_name.size() - 1) {
			sequece_name.remove(currentAreaName);
			mainFrame.setCurrentAreaName(sequece_name.get(sequece_name.size() - 1));
			mainFrame.setCurrentButton(hm_name_btn.get(mainFrame.getCurrentAreaName()));
		//如果在中间，则显示下一页
		}else {
			sequece_name.remove(currentAreaName);
			mainFrame.setCurrentAreaName(sequece_name.get(index));
			mainFrame.setCurrentButton(hm_name_btn.get(mainFrame.getCurrentAreaName()));
		}
		if (mainFrame.getCurrentAreaName() != null) {
			jsp.setViewportView(hmTextArea.get(mainFrame.getCurrentAreaName()));
		}
		jp.updateUI();
	}
	private void removeMap(String name){
		hmTextArea.remove(name);
		hm_name_atrr.remove(name);
		hm_name_btn.remove(name);
	}
}
