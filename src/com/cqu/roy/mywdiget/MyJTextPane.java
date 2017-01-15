package com.cqu.roy.mywdiget;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;

public class MyJTextPane extends JTextPane implements MouseListener,CaretListener{
   // private static final long serialVersionUID = -2308615404205560110L;  
    private MyJPopupMenu pop = null; // 弹出菜单  
    private MyJMenuItem copy = null, paste = null, cut = null; // 三个功能菜单  
    private int line = 1;
    private int caretLineNum = 0;
   
    public MyJTextPane() {
		// TODO Auto-generated constructor stub
        super();  
        init();
	}
   
    private void init() {  
	     this.addMouseListener(this);  
	     this.addCaretListener(this);
	     pop = new MyJPopupMenu();  
	     pop.add(copy = new MyJMenuItem("Copy"));  
	     pop.add(paste = new MyJMenuItem("Paste"));  
	     pop.add(cut = new MyJMenuItem("Cut"));  
	     copy.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_MASK));  
	     paste.setAccelerator(KeyStroke.getKeyStroke('V', InputEvent.CTRL_MASK));  
	     cut.setAccelerator(KeyStroke.getKeyStroke('X', InputEvent.CTRL_MASK));  
	     copy.addActionListener(new ActionListener() {  
		     public void actionPerformed(ActionEvent e) {  
		       action(e);  
		      }  
	     });  
	     paste.addActionListener(new ActionListener() {  
		      public void actionPerformed(ActionEvent e) {  
		    	  action(e);  
		      }  
	     });  
	     cut.addActionListener(new ActionListener() {  
		      public void actionPerformed(ActionEvent e) {  
		    	  action(e);  
		      }  
	     });  
	     this.add(pop);  
    }  
   
    /** 
    * 菜单动作 
    * @param e 
    */  
    public void action(ActionEvent e) {  
	     String str = e.getActionCommand();  
	     if (str.equals(copy.getText())) { // 复制  
	    	 this.copy();  
	     } 
	     else if (str.equals(paste.getText())) { // 粘贴  
	    	 this.paste();  
	     } 
	     else if (str.equals(cut.getText())) { // 剪切  
	    	 this.cut();  
	     }  
    }  
    //行号维护
    public void line() {
		line++;
	}
    public void back() {
		line--;
	}
    public int getLine() {
		return line;
	}
    public void setLine(int lineNum) {
		this.line = lineNum;
	}
    public MyJPopupMenu getPop() {  
     return pop;  
    }  
   
    public void setPop(MyJPopupMenu pop) {  
     this.pop = pop;  
    }  
   
    /** 
    * 剪切板中是否有文本数据可供粘贴 
    *  
    * @return true为有文本数据 
    */  
    public boolean isClipboardString() {  
	     boolean isClipboardstring = false;  
	     Clipboard clipboard = this.getToolkit().getSystemClipboard();  
	     Transferable content = clipboard.getContents(this);  
	     try {  
	    	 if (content.getTransferData(DataFlavor.stringFlavor) instanceof String) {  
	    		 isClipboardstring = true;  
	    	 }  
	     } catch (Exception e) {
	     }  
	     return isClipboardstring;  
    }  
   
    /** 
    * 文本组件中是否具备复制的条件 
    *  
    * @return true为具备 
    */  
    public boolean isCanCopy() {  
     boolean iscancopy = false;  
     int startPosition = this.getSelectionStart();  
     int endPosition = this.getSelectionEnd();  
     if (startPosition != endPosition)  
	      iscancopy = true;  
	     return iscancopy;  
    }  
    public void setCaretLine(int CaretLine) {
		this.caretLineNum = CaretLine;
	}
    
    public int getCaretLine() {
		return caretLineNum;
	}
    
    public void mouseClicked(MouseEvent e) {
    	
    }  
   
    public void mouseEntered(MouseEvent e) {  
    }  
   
    public void mouseExited(MouseEvent e) {  
    }  
   
    public void mousePressed(MouseEvent e) {  
     if (e.getButton() == MouseEvent.BUTTON3) {  
      copy.setEnabled(isCanCopy());  
      paste.setEnabled(isClipboardString());  
      cut.setEnabled(isCanCopy());  
      pop.show(this, e.getX(), e.getY());  
     }  
    }  
   
    public void mouseReleased(MouseEvent e) {  
    }

	@Override
	public void caretUpdate(CaretEvent e) {
		// TODO Auto-generated method stub
		//System.out.println(e.getMark());
		caretLineNum = 0;
			try {
				String text = getText(0, getCaretPosition());
				for(int i = 0; i < text.length();i++){
					if (text.charAt(i) == '\n') {
						caretLineNum++;
					}
				}
				//System.out.println(getText(0,getCaretPosition()));
				//System.out.println(caretLineNum);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
}
