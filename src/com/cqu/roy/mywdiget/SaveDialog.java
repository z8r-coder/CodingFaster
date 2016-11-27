package com.cqu.roy.mywdiget;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class SaveDialog{
	public static final int         CLOSED_OPTION = -1;
	
	public static int showOptionDialog(Component parentComponent, Object message, 
			String title, int optionType, int messageType, Icon icon, Object[] options,
			Object initialValue,int width,int height){
		
		JOptionPane pane = new JOptionPane(message, messageType, optionType,
				icon, options, initialValue);
		
		JDialog myDialog = pane.createDialog(title);
		myDialog.setSize(width, height);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		myDialog.setLocation((screenWidth - width) / 2, (screenHeight - height) / 2);
		
		pane.selectInitialValue();
		myDialog.show();
		myDialog.dispose();
		Object value = pane.getValue();
        if(value == null)
            return CLOSED_OPTION;
        if(options == null) {
            if(value instanceof Integer)
                return ((Integer)value).intValue();
            return CLOSED_OPTION;
        }
        for(int counter = 0, maxCounter = options.length;
            counter < maxCounter; counter++) {
            if(options[counter].equals(value))
                return counter;
        }
        return CLOSED_OPTION;
	}
}
