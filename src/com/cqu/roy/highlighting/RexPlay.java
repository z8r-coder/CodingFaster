package com.cqu.roy.highlighting;

import java.util.HashMap;
import java.util.Vector;
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
	//匹配关键字的前缀
	private final static String prefix = ".*[^A-Za-z0-9]+";
	//匹配关键字的后缀
	private final static String suffix = "[^A-Za-z0-9]+.*";
	//
	private Vector<Token> vc_absOffset;
	
	//private final static String matchBreak = "break|.*[^A-Za-z0-9]+break|break[^A-Za-z0-9]+.*|.*[^A-Za-z0-9]+break[^A-Za-z0-9]+.*";
	
	//存放关键词保留字
	private HashMap<String, String> hm_wold_regex = new HashMap<>();
	//存放类型保留字
	private HashMap<String, String> hm_type_regex = new HashMap<>();
	public RexPlay(String textLine) {
		// TODO Auto-generated constructor stub
		this.textLine = textLine;
		//C语言
		vc_absOffset = new Vector<>();
		String[] temp = splitString(textLine);
		matchesprefixAndsuffixKeyWord(temp);
		matchesKeyWord("dsadsbreakdasif");
		//System.out.println(b);
	}
	
	//红色字体
	//匹配带前缀后缀的关键词
	public void matchesprefixAndsuffixKeyWord(String[] line) {
		//if匹配的时候与A-Z a-z 0-9中间至少夹着一个特殊字符
		generaterStringReg();
		String allRegex = null;
		
		//获取整个正则表达式
		for(int i = 0; i < KeyWord.KeyWord_C.length;i++){
			if (i == 0) {
				allRegex = hm_wold_regex.get(KeyWord.KeyWord_C[i]);
			}else {
				allRegex = allRegex + "|" + hm_wold_regex.get(KeyWord.KeyWord_C[i]);
			}
		}
		Pattern pattern = Pattern.compile(allRegex);
		for(int j = 0; j < line.length;j++){
			Matcher matcher = pattern.matcher(line[j]);
			if (matcher.matches()) {
				//System.out.println(line[j]);
				Vector<Token> vector = matchesKeyWord(line[j]);
					for(int k = 0;k < vector.size();k++){
						Token token = vector.get(k);
						System.out.println(token.getValue() + "  " + "start:" 
								+ token.getStartPosition() + "  end:" + token.getEndPosition()
								+ " length:" + token.getLength());
				}
			}	
		}
	}
	//从带前缀后缀的关键词中匹配出关键词
	public Vector<Token> matchesKeyWord(String prefixAndSuffixKeyWord){
		String matchRegex = null;
		//存放从一段带前缀后缀中匹配出来的关键字,并在这个Token中存放其位置长度值信息
		//断的内偏移量
		Vector<Token> vc_token = new Vector<>();
		//"if|else|"
		for(int i = 0; i < KeyWord.KeyWord_C.length;i++){
			//begin
			if (i == 0) {
				matchRegex = KeyWord.KeyWord_C[i];
			}
			else {
				matchRegex = matchRegex + "|" + KeyWord.KeyWord_C[i];
			}
		}
		Pattern pattern = Pattern.compile(matchRegex);
		//在带前缀后缀的子串中可能包含着多个关键词的信息如 dwq&if*daw(break)qwq
		//此处就包含了多个关键词，通过while循环代替递归的方式，将起寻找完，并将其信息
		//存在Token对象中
		int count = 0;//计数
		int startPosition = 0;
		int endPosition = 0;
		while(true){		
			Matcher matcher = pattern.matcher(prefixAndSuffixKeyWord);
			if (matcher.find()) {
				if (count == 0) {
					Token token = new Token(matcher.group(0), matcher.start()
							, matcher.end() - 1, matcher.end() - matcher.start());
					vc_token.add(token);
					startPosition = matcher.end();
				}
				else {
					startPosition = startPosition + matcher.start();
					endPosition = startPosition - matcher.start() + matcher.end() - 1;
					Token token = new Token(matcher.group(0), startPosition
							, endPosition, matcher.end() - matcher.start());
					vc_token.add(token);
					startPosition = startPosition + matcher.end() - matcher.start();
				}
				prefixAndSuffixKeyWord = prefixAndSuffixKeyWord.substring(matcher.end());
//				System.out.println();
//				System.out.println(prefixAndSuffixKeyWord);
				count++;
			}else {
				break;
			}
		}
		return vc_token;
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
