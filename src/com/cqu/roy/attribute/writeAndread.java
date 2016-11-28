package com.cqu.roy.attribute;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import com.cqu.roy.mywdiget.MyFontStyle;

public class writeAndread {
	
	private final static String encoding = "UTF-8";
	
	public void saveTo(File file,String Content) {
		try {
			OutputStreamWriter osw = new OutputStreamWriter(
					new FileOutputStream(file),encoding);
			BufferedWriter bw = new BufferedWriter(osw);
			try {
				bw.write(Content);
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
	
	public JTextPane openFrom(File file,JTextPane jtp) {
		String lineText = null;
		StyledDocument document = jtp.getStyledDocument();
		MyFontStyle myFontStyle = new MyFontStyle(document);
		document = myFontStyle.getStyleDoc();
		jtp.setStyledDocument(document);
		try {
			InputStreamReader isr = new InputStreamReader(
					new FileInputStream(file),encoding);
			BufferedReader br = new BufferedReader(isr);
			try {
				if (document.getLength() == 0) {
					document.setLogicalStyle(0, document.getStyle("Style06"));
				}
				while((lineText = br.readLine()) != null){
					try {
						document.insertString(document.getLength(), lineText + "\n"
								, document.getStyle("Style06"));
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
		return jtp;
	}
}
