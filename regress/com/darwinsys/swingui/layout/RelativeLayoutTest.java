package com.darwinsys.swingui.layout;

import java.awt.Button;
import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import org.junit.Test;

/**
 * Simple class to non-exhaustively test out RelativeLayout layout manager.
 */
public class RelativeLayoutTest {

	/**
	 * Simple main program to test out RelativeLayout.
	 * Invoke directly from Java interpreter.
	 */
	public static void main(String[] av) {
		JFrame f = new JFrame("Test");
		RelativeLayoutTest relativeLayoutTest = new RelativeLayoutTest();
		relativeLayoutTest.setParentFrame(f);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

	/** Construct a RelativeLayoutTest test program. */
	public void setParentFrame(JFrame f) {
		Button qb;			// quit
		Container cp = f.getContentPane();
		cp.setLayout(new RelativeLayout(300, 150));
		cp.add("80,20", new Button("MidWidth"));
		cp.add("150,75", qb = new Button("Quit"));
		qb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		f.pack();
	}
	
	public RelativeLayoutTest() {
		// empty
	}
	
	@Test
	public void trivialTest() {
		try {
			main(null);
		} catch (HeadlessException he) {
			System.out.println("RelativeLayoutTest.test(): cannot test Headless");
		}
	}
}
