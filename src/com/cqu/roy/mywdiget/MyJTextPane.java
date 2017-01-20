package com.cqu.roy.mywdiget;

import java.awt.Font;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;

import com.cqu.roy.editOperation.Undo;
import com.cqu.roy.fileOperation.newFile;
import com.cqu.roy.highlighting.SyntaxHighlighter;
import com.cqu.roy.historyStorage.Node;
import com.cqu.roy.historyStorage.TextInfo;
import com.cqu.roy.historyStorage.VersionTree;
import com.cqu.roy.main.main;
import com.cqu.roy.mainframe.MainFrame;

public class MyJTextPane extends JTextPane implements MouseListener,CaretListener,MouseWheelListener{
   // private static final long serialVersionUID = -2308615404205560110L;  
	
    private MyJPopupMenu pop = null; // 弹出菜单  
    private MyJMenuItem copy = null, paste = null, cut = null; // 三个功能菜单  
    private int line = 1;
    private int caretLineNum = 0;
    private String caretCharacter;//光标当前字符
    private String preCharacter;//光标前一个字符
    private boolean isChange = false;
    private VersionTree vst;//每个文本域映射一个版本树，将其封装在此类中
    private ArrayList<Node> currentNodeSet;
    private Stack<HashSet<Integer>> RedoStack;//每个文本域映射一个Redo栈
    private Stack<HashSet<Integer>> UndoStack;//每个文本域映射一个Undo栈
    private MainFrame mainFrame;
    public MyJTextPane() {
		// TODO Auto-generated constructor stub
        super();  
        init();
        mainFrame = MainFrame.getInstance();
		vst = new VersionTree();
		/*Node的参数：
		 * 1:存储的具体内容
		 * 2：存储的行号位置
		 * 3：下一个操作的行号，若下一个节点不存在，则为-1
		 * 4：上一个操作的行号，若上一个节点为根节点，则为-1
		 * 5：指向父节点的指针
		 * 6：指向子节点的指针*/
		Node firstNode = new Node(new TextInfo(null, 0, 0, 0), 0, -1, -1, null, null);
		vst.InsertNode(0, firstNode);//第一代子节点
		currentNodeSet = vst.getCurrentNodeSet();
		currentNodeSet.add(firstNode);//当前字节点
		RedoStack = new Stack<>();
		UndoStack = new Stack<>();
	}
    public VersionTree getVersionTree() {
		return vst;
	}
    public Stack<HashSet<Integer>> getRedoStack() {
		return RedoStack;
	}
    public Stack<HashSet<Integer>> getUndoStack() {
		return UndoStack;
	}
    private void init() {  
	     this.addMouseListener(this);  
	     this.addCaretListener(this);
	     this.addMouseWheelListener(this);
	     
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

    //获取光标当前字符
    public String getcaretChar() {
		return caretCharacter;
	}
    //设置光标当前字符
    public void setCaretChar(String caretChar) {
		this.caretCharacter = caretChar;
	}
    //获取光标前一个字符
    public String getPreCaretChar() {
		return preCharacter;
	}
	@Override
	public void caretUpdate(CaretEvent e) {
		// TODO Auto-generated method stub
		caretLineNum = 0;
			try {
				String text = getText(0, getCaretPosition());
				for(int i = 0; i < text.length();i++){
					if (text.charAt(i) == '\n') {
						caretLineNum++;
					}
				}
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		boolean ctrl = mainFrame.getCtrl();
		//向后滚字体放大
		if (e.getWheelRotation() == 1 && ctrl) {
			try {
				
				getDocument().getText(0, getDocument().getLength());
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println(1);
		}
		//向前滚字体缩小
		if (e.getWheelRotation() == -1 && ctrl) {
			System.out.println(2);
		}
	}
}
