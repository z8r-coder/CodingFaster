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
	
	public void openFrom(File file,JTextArea jta) {
		String lineText = null;
		try {
			InputStreamReader isr = new InputStreamReader(
					new FileInputStream(file),encoding);
			BufferedReader br = new BufferedReader(isr);
			try {
				while((lineText = br.readLine()) != null){
					jta.append(lineText);
					jta.append("\n");
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
	}
}
