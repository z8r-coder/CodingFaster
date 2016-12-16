package com.cqu.roy.highlighting;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jws.soap.SOAPBinding;

import com.cqu.roy.constant.KeyWord;
import com.cqu.roy.constant.LenthAll;
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
	//匹配关键字的前缀
	private final static String prefix = ".*[^A-Za-z0-9_]+";
	//匹配关键字的后缀
	private final static String suffix = "[^A-Za-z0-9_]+.*";
	//识别注释，带前缀
	private final static String prenotes = ".*//.*";
	//识别注释
	private final static String notes = "//.*";
	//识别带前缀字符串
	private final static String precharacterString = "\".*\"";
	//识别带前缀后缀的数字
	private final static String presufInteger = "[^A-Za-z_][0-9]+[^A-Za-z_]";
	//识别数字
	private final static String integer = "[0-9]+";
	//每个token的信息
	private Vector<Token> vc_token;
	//每个分离出来的词素中能够提取多少个关键词
	private Vector<Integer> keyWordNum;
	//每个分离出来带前缀后缀token的集合
	private ArrayList<String> pre_su = new ArrayList<>();
	//存放每个带前缀后缀token的绝对位置
	private ArrayList<Integer> absLocation = new ArrayList<>();
	//保留关键字的正则表达式
	private String allRegex_KeyWord = null;
	//类型的正则表达式
	private String allRegex_Type = null;
	//所有word模式的正则表达式
	private String allRegex = null;
	//不带前缀后缀关键字的正则表达式
	private String matches_keyword_regex = null;
	//不带前缀后缀类型的正则表达式
	private String matches_type_regex = null;
	//所有保留字
	private String matches_regex = null;
	//计数每个词素绝对位置
	private int abs_count = 0;
	//private final static String matchBreak = "break|.*[^A-Za-z0-9]+break|break[^A-Za-z0-9]+.*|.*[^A-Za-z0-9]+break[^A-Za-z0-9]+.*";
	String[] splitString;
	//存放关键词保留字
	private HashMap<String, String> hm_wold_regex = new HashMap<>();
	//存放类型保留字
	private HashMap<String, String> hm_type_regex = new HashMap<>();
	//type表
	private Set<String> typeWord;
	//keyword表
	private Set<String> keyWord;
	public RexPlay(String textLine) {
		// TODO Auto-generated constructor stub
		this.textLine = textLine;
		//C语言

		keyWordNum = new Vector<>();
		vc_token = new Vector<>();
		typeWord = new HashSet<>();
		keyWord = new HashSet<>();
		TableGet();
		generaterStringReg();
		generaterMatchesReg();
		//获取整个正则表达式
		getAllRegex_C();
		
		String sp = textLine;
		//去掉字符串开头的space
		for(int i = 0; i < textLine.length();i++){
			if (textLine.charAt(i) != ' ') {
				sp = sp.substring(i);
				break;
			}
		}
		//newLine是每行间的分割
		Vector<String> newLine = byNewLine(sp);
		//按照每行的顺序，依次将文本，注释放如splitString中，不能乱啦位置，否则渲染位置会出错
		Vector<String> vc_splitString = new Vector<>();
		for(int i = 0; i < newLine.size();i++){
			//notes是注释和代码的分割
			Vector<String> notes = splitStringBynotes(newLine.get(i));
			if (notes.size() != 0) {
				splitString = splitString(notes.get(0));
				for(int j = 0; j < splitString.length;j++){
					vc_splitString.add(splitString[j]);
				}
			}
			if (notes.size() > 1) {
				vc_splitString.add(notes.get(1));
			}
		}
		matchesprefixAndsuffixKeyWord(vc_splitString,allRegex,matches_regex,vc_token);

		for(int k = 0;k < vc_token.size();k++){
			Token token = vc_token.get(k);
			token.setLocation(token.getAbsLocation() + token.getStartPosition());
//			System.out.println(token.getValue() + "  " + "Location:" 
//					+ token.getLocation() +  " AbsLocation:" + token.getAbsLocation() + 
//					"  end:" + (token.getAbsLocation() + token.getStartPosition() + token.getLength())
//					+ " length:" + token.getLength());
		}
	}
	public HashSet<String> getKeyWord() {
		return (HashSet<String>) keyWord;
	}
	public HashSet<String> getTypeWord() {
		return (HashSet<String>) typeWord;
	}
	public Vector<Token> getToken() {
		return vc_token;
	}
	//数出绝对位置
	public void countingAbsLocation(String textLine,Vector<String> vc_splitString) {
		int count_pre_su = 0;//计数能匹配第几个前缀后缀的词素
		int count_token = 0;//计数第几个分离的串
		for(int i = 0; i < textLine.length();){
			if (textLine.charAt(i) == ' ' || textLine.charAt(i) == '\n' 
					 || textLine.charAt(i) == '\r' ) {
				i++;
			}else {
				if (count_pre_su == pre_su.size()) {
					return;
				}
				//
				if (vc_splitString.get(count_token) == pre_su.get(count_pre_su)) {
					absLocation.add(i);
					count_pre_su++;
				}
				i = vc_splitString.get(count_token).length() + i;
				count_token++;
			}
		}
	}
	//红色字体
	//匹配带前缀后缀的关键词
	public void matchesprefixAndsuffixKeyWord(Vector<String> vc_splitString,String allRegex
			,String matchRegex,Vector<Token> vc) {
		//if匹配的时候与A-Z a-z 0-9中间至少夹着一个特殊字符
		
		Pattern pattern = Pattern.compile(allRegex);
		for(int j = 0; j < vc_splitString.size();j++){
			Matcher matcher = pattern.matcher(vc_splitString.get(j));
			if (matcher.matches()) {
				pre_su.add(vc_splitString.get(j));
			}	
		}
		countingAbsLocation(textLine,vc_splitString);
		for(int i = 0; i < pre_su.size();i++){
			matchesKeyWord(pre_su.get(i),matchRegex,allRegex,vc);
		}
	}
	//从带前缀后缀的关键词中匹配出关键词
	public void matchesKeyWord(String prefixAndSuffixKeyWord,String matchRegex
			,String allRegex,Vector<Token> vc){

		Pattern pattern = Pattern.compile(matchRegex);//匹配关键字
		Pattern pattern_pre_su = Pattern.compile(allRegex);//在使用subString的时候还要检查新的String是否满足前后缀的条件
		//在带前缀后缀的子串中可能包含着多个关键词的信息如 dwq&if*daw(break)qwq
		//此处就包含了多个关键词，通过while循环代替递归的方式，将起寻找完，并将其信息
		//存在Token对象中
		int count = 0;//计数
		int startPosition = 0;
		int endPosition = 0;
		while(true){
			Matcher matcher_pre_su = pattern_pre_su.matcher(prefixAndSuffixKeyWord);
			Matcher matcher = pattern.matcher(prefixAndSuffixKeyWord);
			//ifd^if会出现错误表示
			if (matcher.find() && matcher_pre_su.matches()) {
				if (count == 0) {
					Token token = new Token(matcher.group(0), matcher.start()
							, matcher.end() - 1, matcher.end() - matcher.start());
					token.setAbsLocation(absLocation.get(abs_count));
					vc.add(token);
					startPosition = matcher.end();
				}
				else {
					startPosition = startPosition + matcher.start();
					endPosition = startPosition - matcher.start() + matcher.end() - 1;
					Token token = new Token(matcher.group(0), startPosition
							, endPosition, matcher.end() - matcher.start());
					token.setAbsLocation(absLocation.get(abs_count));
					vc.add(token);
					startPosition = startPosition + matcher.end() - matcher.start();
				}
				prefixAndSuffixKeyWord = prefixAndSuffixKeyWord.substring(matcher.end());
				count++;
			}else {
				keyWordNum.add(count);
				abs_count++;
				break;
			}
		}
	}

	//以空格和换行符将单词分开
	public String[] splitString (String str) {
		str.trim();
		Pattern pattern = Pattern.compile("[ ]+|[\\n]");
		String[] temp = pattern.split(str);
		return temp;
	}
	//先用换行符分割
	public Vector<String> byNewLine(String str) {
		Pattern pattern = Pattern.compile("\n");
		String []temp = pattern.split(str);
		Vector<String> byNewLinew = new Vector<>();
		for (int i = 0; i < temp.length; i++) {
			byNewLinew.add(temp[i]);
		}
		return byNewLinew;
	}
	//以//先将注释分开
	public Vector<String> splitStringBynotes(String str) {
		Pattern pattern = Pattern.compile("//");
		Matcher matcher = pattern.matcher(str);
		Vector<String> byNotes = new Vector<>();
		if (matcher.find()) {
			int positon = matcher.start();
			String temp_1 = str.substring(0, positon);
			//System.out.println(temp_1);
			byNotes.add(temp_1);
			String temp_2 = str.substring(positon,str.length());
			byNotes.add(temp_2);
			//System.out.println(temp_2);
		}else {
			byNotes.add(str);
		}
		return byNotes;
	}
	//生成每个关键字需要的正则表达式,带前缀后缀
	public void generaterStringReg() {
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
	//不带前缀后缀的关键字正则表达式
	public void generaterMatchesReg(){
		//存放从一段带前缀后缀中匹配出来的关键字,并在这个Token中存放其位置长度值信息
		//段内偏移量
		//"if|else|"
		for(int i = 0; i < KeyWord.KeyWord_C.length;i++){
			//begin
			if (i == 0) {
				matches_keyword_regex = KeyWord.KeyWord_C[i];
			}
			else {
				matches_keyword_regex = matches_keyword_regex + "|" + KeyWord.KeyWord_C[i];
			}
		}
		for(int i = 0; i < KeyWord.Type_C.length;i++){
			//begin
			if (i == 0) {
				matches_type_regex = KeyWord.Type_C[i];
			}
			else {
				matches_type_regex = matches_type_regex + "|" + KeyWord.Type_C[i];
			}
		}
		matches_regex = matches_keyword_regex + "|" + matches_type_regex;
		matches_regex = matches_regex + "|" + notes;// + "|" + integer;
	}
	public void getAllRegex_C() {
		for(int i = 0; i < KeyWord.KeyWord_C.length;i++){
			if (i == 0) {
				allRegex_KeyWord = hm_wold_regex.get(KeyWord.KeyWord_C[i]);
			}else {
				allRegex_KeyWord = allRegex_KeyWord + "|" + hm_wold_regex.get(KeyWord.KeyWord_C[i]);
			}
		}
		for(int i = 0; i < KeyWord.Type_C.length;i++){
			if (i == 0) {
				allRegex_Type = hm_type_regex.get(KeyWord.Type_C[i]);
			}else {
				allRegex_Type = allRegex_Type + "|" + hm_type_regex.get(KeyWord.Type_C[i]);
			}
		}
		allRegex = allRegex_KeyWord + "|" + allRegex_Type;
		allRegex = allRegex + "|" + prenotes;// + "|" + presufInteger;
	}
	public void TableGet() {
		for(int i = 0; i < KeyWord.KeyWord_C.length;i++){
			keyWord.add(KeyWord.KeyWord_C[i]);
		}
		for (int i = 0; i < KeyWord.Type_C.length; i++) {
			typeWord.add(KeyWord.Type_C[i]);
		}
	}
}
