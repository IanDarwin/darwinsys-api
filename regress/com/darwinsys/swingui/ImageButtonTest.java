package com.darwinsys.swingui;

import java.awt.FlowLayout;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * Demonstrate that ImageButton is superfluous, just use 
 * new JButton(String, IconImage) in its place.
 */
public class ImageButtonTest {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		ImageIcon img = new ImageIcon(new URL("http://www.darwinsys.com/images/java_lobby_logo.gif"));
		ImageButton target = new ImageButton(img, "An ImageButton");
		JButton replace = new JButton("A JButton", img);
		JFrame jf = new JFrame();
		jf.setLayout(new FlowLayout());

		jf.add(target);
		jf.add(replace);
		jf.pack();
		jf.setVisible(true);
	}

}
