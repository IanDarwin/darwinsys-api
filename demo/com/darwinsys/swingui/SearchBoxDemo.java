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
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		sb.getTextField().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final String text = sb.getText();
				if (text.length() > 0)
					System.out.println("Would search for: " + text);
			}			
		});
		// This is not normally required; it's just to make the demo complete :-)
		sb.getClearButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Cleared!");
			}	
		});
	}
}
