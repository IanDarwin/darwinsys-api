package com.darwinsys.swingui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

/** A JTextField with a Clear Button */
public class SearchBoxDemo extends JFrame {

	public static void main(String[] unuxed) {
		final JFrame jf = new JFrame();
		final SearchBox sb = new SearchBox();
		jf.setContentPane(sb);
		jf.pack();
		jf.setVisible(true);
		
		sb.getTextField().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Would search for: " + sb.getText());
			}			
		});
	}
}
