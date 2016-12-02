package com.cqu.roy.highlighting;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cqu.roy.constant.KeyWord;
import com.cqu.roy.fileOperation.newFile;
/*C语言保留字
 * type:auto char enum float int long   
 * short signed struct typedef union
 * unsigned void volatile double
 * 
 * key word:break case const continue
 * default do else extern for goto if register
 * return sizeof static switch while*/
public class RexPlay {
	private String textLine;
	private final static String prefix = ".*[^A-Za-z0-9]+";
	private final static String suffix = "[^A-Za-z0-9]+.*";
	
	//private final static String matchBreak = "break|.*[^A-Za-z0-9]+break|break[^A-Za-z0-9]+.*|.*[^A-Za-z0-9]+break[^A-Za-z0-9]+.*";
	
	//存放关键词保留字
	private HashMap<String, String> hm_wold_regex = new HashMap<>();
	//存放类型保留字
	private HashMap<String, String> hm_type_regex = new HashMap<>();
	public RexPlay(String textLine) {
		// TODO Auto-generated constructor stub
		this.textLine = textLine;
		//C语言
		
		String[] temp = splitString(textLine);
		matchesKeyWord(temp);
		//System.out.println(b);
	}
	
	//红色字体
	public void matchesKeyWord(String[] line) {
		//if匹配的时候与A-Z a-z 0-9中间至少夹着一个特殊字符
		generaterStringReg();
		Pattern pattern = Pattern.compile(hm_type_regex.get("auto"));
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
	
	public void generaterStringReg() {
		//生成每个关键字需要的正则表达式
		//先生成关键词保留字
		for(int i = 0; i < KeyWord.KeyWord_C.length;i++){
			String kw = KeyWord.KeyWord_C[i] + "|" + prefix + KeyWord.KeyWord_C[i]
					+ "|" + KeyWord.KeyWord_C[i] + suffix + "|"
					+ prefix + KeyWord.KeyWord_C[i] + suffix;
			hm_wold_regex.put(KeyWord.KeyWord_C[i], kw);
		}
		
		//再生成类型保留字
		for(int i = 0; i < KeyWord.Type_C.length;i++){
			String tp = KeyWord.Type_C[i] + "|" + prefix + KeyWord.Type_C[i]
					+ "|" + KeyWord.Type_C[i] + suffix + "|"
					+ prefix + KeyWord.Type_C[i] + suffix;
			hm_type_regex.put(KeyWord.Type_C[i], tp);
		}
	}
}
