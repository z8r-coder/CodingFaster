package com.cqu.roy.highlighting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*C语言保留字
 * type:auto char enum float int long   
 * short signed struct typedef union
 * unsigned void volatile
 * 
 * key word:break case const continue
 * default do double else extern for goto if register
 * return sizeof static switch while*/
public class RexPlay {
	private String textLine;
	private final static String matchBreak = "break|.*[^A-Za-z0-9]+break|break[^A-Za-z0-9]+.*";
	private final static String matchIF = "if|.*[^A-Za-z0-9]+if|if[^A-Za-z0-9]+.*";
	public RexPlay(String textLine) {
		// TODO Auto-generated constructor stub
		this.textLine = textLine;
		
		
		String[] temp = splitString(textLine);
		matcheKeyWord(temp);
		//System.out.println(b);
	}
	
	//红色字体
	public void matcheKeyWord(String[] line) {
		//if匹配的时候与A-Z a-z 0-9中间至少夹着一个特殊字符
		//Pattern pattern = Pattern.compile("if|.*[^A-Za-z0-9]+if|if[^A-Za-z0-9]+.*");
		Pattern pattern = Pattern.compile("[.*[^A-Za-z0-9]+]*if[[^A-Za-z0-9]+.*]*");
		//Pattern pattern = Pattern.compile("[^a-zA-Z]+");//非的话，或转与,第一个取非，整体便非
		for(int i = 0; i < line.length;i++){
			Matcher matcher = pattern.matcher(line[i]);
			boolean b = matcher.matches();
			System.out.println(b);
		}
	}
	
	public String[] splitString (String str) {
		str.trim();
		Pattern pattern = Pattern.compile("[ ]+");
		String[] temp = pattern.split(str);
		return temp;
	}
}
