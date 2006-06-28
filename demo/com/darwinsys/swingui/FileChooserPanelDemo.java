package com.darwinsys.swingui;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class FileChooserPanelDemo {
	public static void main(String[] args) {
		JFrame f = new JFrame("JFileChooser Demo");
		FileChooserPanel chooserPanel = new FileChooserPanel(f);
		chooserPanel.setBorder(BorderFactory.createTitledBorder("Install Directory"));
		JFileChooser chooser = chooserPanel.getChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		f.getContentPane().add(chooserPanel);
		f.pack();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		UtilGUI.center(f);
		f.setVisible(true);
	}
}
