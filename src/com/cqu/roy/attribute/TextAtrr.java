package com.cqu.roy.attribute;
//封装每个文本域所对应的属性,即每个存在于页面的文本域和相应的按钮都存在一个对应的TextAtrr
public class TextAtrr {
	private boolean isSave;//是否被保存啦
	private int ID;//若被保存，则无效,为0，若未被保存，则判断是第几个未被保存的文件，用来命名untitled
	private String filename = null;//被保存了的时候为有效位。
	private String fileAddress = null;//被保存了的时候为有效位
	private String Suffix = null;//文件的后缀
	
	public TextAtrr(boolean isSave,int ID,String filename,String fileAddress) {
		// TODO Auto-generated constructor stub
		this.isSave = isSave;
		this.ID = ID;
		this.filename = filename;
		this.fileAddress = fileAddress;
	}
	
	public void setisSave(boolean isSave) {
		this.isSave = isSave;
	}
	public boolean getisSave(){
		return isSave;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getID() {
		return ID;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFilename() {
		return filename;
	}
	public void setFileAddress(String fileAddress) {
		this.fileAddress = fileAddress;
	}
	public String getFileAddress() {
		return fileAddress;
	}
	
	public void setSuffix(String Suffix) {
		this.Suffix = Suffix;
	}
	
	public String getSuffix() {
		return Suffix;
	}
}
