/*整体布局
 * 滚轮jpanel放center
 * 放置切换当前文本域的按钮放在north*/
package com.cqu.roy.mainframe;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.cqu.roy.attribute.TextAtrr;
import com.cqu.roy.attribute.writeAndread;
import com.cqu.roy.constant.ItemName;
import com.cqu.roy.constant.KeyCode;
import com.cqu.roy.constant.LenthAll;
import com.cqu.roy.editOperation.Redo;
import com.cqu.roy.editOperation.Undo;
import com.cqu.roy.fileOperation.CloseAllFile;
import com.cqu.roy.fileOperation.CloseFile;
import com.cqu.roy.fileOperation.CloseWindow;
import com.cqu.roy.fileOperation.FileOperation;
import com.cqu.roy.fileOperation.OpenFile;
import com.cqu.roy.fileOperation.SaveAll;
import com.cqu.roy.fileOperation.SaveAs;
import com.cqu.roy.fileOperation.SaveSingleOp;
import com.cqu.roy.fileOperation.newFile;
import com.cqu.roy.historyStorage.Node;
import com.cqu.roy.historyStorage.TextInfo;
import com.cqu.roy.historyStorage.VersionTree;
import com.cqu.roy.mywdiget.JpathButton;
import com.cqu.roy.mywdiget.MainLayout;
import com.cqu.roy.mywdiget.MyFontStyle;
import com.cqu.roy.mywdiget.MainJpanel;

public class MainFrame extends JFrame implements ActionListener{
//	private JPanel mainJP;//承载主要内容，包括文本段，行号，代码缩小等，采用复杂的GridBadLayout布局
	private JPanel jp;
	private JScrollPane jsp;
	private JMenuBar bar;
	private JMenu Filemenu;
	private JMenu Editmenu;
	
	private BorderLayout bLayout = new BorderLayout();
	private GridLayout gridLayout = new GridLayout(1, 6);
//	private MainLayout mainlayout = new MainLayout();
	String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment()
			.getAvailableFontFamilyNames();
	
	private JPanel northjp;
	private JPanel westjp;
	
	private MyFontStyle myFontStyle;//字体样式
	
	private String currentAreaName = null;//当前聚焦的文本
	private JpathButton currentButton = null;//当前聚焦的文本按钮
	
	private writeAndread war = new writeAndread();
	
	//每次页面中发生变化，需要维护的变量
	//名字为文件地址（由于在同一层级目录下，是没有相同名字的文件的。），若未保存，名字为untitled + id
	private HashMap<String,MainJpanel> hmTextArea = new HashMap<>();//名字和文本域的映射
	private HashMap<String, JpathButton> hm_name_btn = new HashMap<>();//名字和按钮的映射
	private HashMap<String, TextAtrr> hm_name_atrr = new HashMap<>();//名字与具体对象的映射
	private Vector<Integer> untitled_vc = new Vector<>();//未保存的id集合
	private Vector<Integer> close_id = new Vector<>();//保留ID,如1234,把2关闭啦，close_id保存数字2,然后下一个new 的时候命名为2
	private Vector<String> sequece_name = new Vector<>();//文件打开的序列;

	//JFileChooser只能有一个
	public static int fileCount = 0;
	//在closeFile中获取JFileChooser是按的确定还是取消
	public static int sureOrcancel;
	
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
	
	//dialog 选项与图片
	Icon icon = new ImageIcon("src/imageResources/warning.png");
	Object[] selection = {"Save","Cancle","Close Without Saving"};
	
	//Table driven
	HashMap<String, FileOperation> map = new HashMap<>();
	//单例化
	private static MainFrame mFrame = new MainFrame();
	private MainFrame() {
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  

		//初始化hash表
		TableDriven();
		
		jp = (JPanel) getContentPane();
		jp.setBackground(new Color(38, 38, 38));
		jp.setLayout(bLayout);
//		mainJP = new JPanel();
//		mainJP.setLayout(mainlayout);
		SwingUtilities.updateComponentTreeUI(jp);
		northjp = new JPanel();
		northjp.setBackground(new Color(38, 38, 38));
		northjp.setLayout(gridLayout);
		northjp.setSize(jp.getSize().width, 40);
		
		jsp = new JScrollPane();//滚轮
		jsp.setBackground(new Color(50, 50, 50));
		//jsp.add(mainJP);
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
				TableDriven();
				MainJpanel currentArea = hmTextArea.get(currentAreaName);
				if (((KeyEvent)event).getID() == KeyEvent.KEY_PRESSED) {
					switch (((KeyEvent)event).getKeyCode()) {
					case KeyCode.CTRL:	
						if (currentArea != null) {
							currentArea.getTextPane().setEditable(false);//锁住area
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
					if (com_S && com_ctrl && !com_shift) {
						//saveSingleOp();
						map.get(ItemName.selectionName[2]).use(jp, jsp, northjp, close_id, untitled_vc
								, sequece_name, currentAreaName, currentButton
								, hmTextArea, hm_name_atrr, hm_name_btn);
					}
					//open
					else if (com_O && com_ctrl) {
						//openOp();
						map.get(ItemName.selectionName[1]).use(jp, jsp, northjp, close_id, untitled_vc
								, sequece_name, currentAreaName, currentButton
								, hmTextArea, hm_name_atrr, hm_name_btn);
					}
					//new
					else if (com_N && com_ctrl && !com_shift) {
						//newFile();
						map.get(ItemName.selectionName[0]).use(jp, jsp, northjp, close_id, untitled_vc
								, sequece_name, currentAreaName, currentButton
								, hmTextArea, hm_name_atrr, hm_name_btn);
					}
					else if (com_ctrl && com_W && !com_shift) {
						//closeFileOp();
						map.get(ItemName.selectionName[6]).use(jp, jsp, northjp, close_id, untitled_vc
								, sequece_name, currentAreaName, currentButton
								, hmTextArea, hm_name_atrr, hm_name_btn);
					}
					//close window
					else if (com_W && com_ctrl && com_shift) {
						map.get(ItemName.selectionName[5]).use(jp, jsp, northjp, close_id, untitled_vc
								, sequece_name, currentAreaName, currentButton
								, hmTextArea, hm_name_atrr, hm_name_btn);
					}
					//save as
					else if (com_ctrl && com_shift && com_S) {
						map.get(ItemName.selectionName[3]).use(jp, jsp, northjp, close_id, untitled_vc
								, sequece_name, currentAreaName, currentButton
								, hmTextArea, hm_name_atrr, hm_name_btn);
					}
				}
				else if(((KeyEvent)event).getID() == KeyEvent.KEY_RELEASED){
					switch (((KeyEvent)event).getKeyCode()) {
					case KeyCode.CTRL:
						if (currentArea != null) {
							currentArea.getTextPane().setEditable(true);//解锁area
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
	//该构造函数专门为其他类提供当前文件路径和当前按钮
	//获取单例的对象
	public static MainFrame getInstance() {
		return mFrame;
	}
	//获取行号显示面板
	public JPanel getLinePanel() {
		return westjp;
	}
	//获取main的布局
//	public MainLayout getMainLayout() {
//		return mainlayout;
//	}
	//获取ctrl键的信息
	public boolean getCtrl() {
		return com_ctrl;
	}
	public String getCurrentAreaName() {
		return currentAreaName;
	}
//	public JPanel getMainPane() {
//		return mainJP;
//	}
	public JpathButton getCurrentButton() {
		return currentButton;
	}
	public HashMap<String, MainJpanel> getHashTextPane() {
		return hmTextArea;
	}
	public void setCurrentAreaName(String currentAreaName){
		this.currentAreaName = currentAreaName;
	}
	
	public void setCurrentButton(JpathButton currentButton) {
		this.currentButton = currentButton;
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
		Editmenu = new JMenu("Edit");
		for(int i = 0;i < ItemName.editOpName.length;i++){
			JMenuItem item = new JMenuItem(ItemName.editOpName[i]);
			item.addActionListener(this);
			Editmenu.add(item);
		}
		bar.add(Editmenu);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//初始化hash表
		TableDriven();
		try {
			
		} catch (NullPointerException npe) {
			// TODO: handle exception
			int n = JOptionPane.showConfirmDialog(null, "There are some wrong.So sorry!\n"
					+ "You can save all the files by pressing the button?", "Error", JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION) {
				map.get(ItemName.selectionName[4]).use(jp, jsp, northjp, close_id, untitled_vc
						, sequece_name, currentAreaName, currentButton, hmTextArea
						, hm_name_atrr, hm_name_btn);
				System.exit(0);
			}else if (n == JOptionPane.NO_OPTION) {
				System.exit(0);
			}
		}
		map.get(e.getActionCommand()).use(jp, jsp, northjp, close_id, untitled_vc
				, sequece_name, currentAreaName
				, currentButton, hmTextArea, hm_name_atrr, hm_name_btn);
		//System.out.println(hm_name_atrr.get(currentAreaName).getSuffix());
	}

	private void TableDriven(){
		map.put(ItemName.selectionName[0], new newFile());
		map.put(ItemName.selectionName[1], new OpenFile());
		map.put(ItemName.selectionName[2], new SaveSingleOp());
		map.put(ItemName.selectionName[3], new SaveAs());
		map.put(ItemName.selectionName[4], new SaveAll());
		map.put(ItemName.selectionName[5], new CloseWindow()); 
		map.put(ItemName.selectionName[6], new CloseFile());
		map.put(ItemName.selectionName[7], new CloseAllFile());
		map.put(ItemName.editOpName[0], new Redo());
		map.put(ItemName.editOpName[1], new Undo());
	}
}
