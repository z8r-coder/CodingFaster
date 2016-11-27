package com.cqu.roy.mywdiget;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class MyFontStyle {
	private StyledDocument styledDoc;
	public MyFontStyle(StyledDocument doc) {
		// TODO Auto-generated constructor stub
		this.styledDoc = doc;
		 // Get Available  Font Family Name
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
//        for(int i = 0; i < fontNames.length;i++){
//        	System.out.println(fontNames[i]);
//        }
        // Content
        createStyle("Style01", styledDoc, 12, 0, 0, 0, Color.BLACK, Font.SANS_SERIF);
        createStyle("Style02", styledDoc, 35, 1, 1, 1, Color.GREEN, "华文琥珀");
        createStyle("Style03", styledDoc, 25, 1, 0, 0, Color.BLUE, "隶书");
        createStyle("Style04", styledDoc, 18, 1, 0, 0, new Color(0, 128, 128), fontNames[0]);
        createStyle("Style05", styledDoc, 20, 0, 1, 0, new Color(128, 128, 0), fontNames[7]);
        createStyle("Style06", styledDoc, 15, 1, 0, 0, Color.black, fontNames[16]);
        createStyle("Style07", styledDoc, 18, 1, 1, 0, Color.RED, "华文彩云");
	}
    public void createStyle(String style, StyledDocument doc, int size, 
    		int bold, int italic, int underline, Color color, String fontName) {
        Style sys = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        try {
            doc.removeStyle(style);
        } catch (Exception e) {
        } //先删除这种Style,假使他存在

        Style s = doc.addStyle(style, sys); // 加入
        StyleConstants.setFontSize(s, size); // 大小
        StyleConstants.setBold(s, (bold == 1) ? true : false); // 粗体
        StyleConstants.setItalic(s, (italic == 1) ? true : false); // 斜体
        StyleConstants.setUnderline(s, (underline == 1) ? true : false); // 下划线
        StyleConstants.setForeground(s, color); // 颜色
        StyleConstants.setFontFamily(s, fontName);  // 字体
    }
    
    public StyledDocument getStyleDoc() {
		return styledDoc;
	}
}
