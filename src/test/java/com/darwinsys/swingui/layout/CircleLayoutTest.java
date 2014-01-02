package com.darwinsys.swingui.layout;

import java.awt.Container;
import java.awt.HeadlessException;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.junit.Test;

/** Testbed for CircleLayout layout manager.
 * @author	Ian Darwin, http://www.darwinsys.com/
 */
public class CircleLayoutTest {

	@Test
	public void test() {
		try {
		final JFrame f = new JFrame("CircleLayout Demonstration");
		Container cp = f.getContentPane();
		cp.setLayout(new CircleLayout(true));
		cp.add(new JButton("One"));
		cp.add(new JButton("Two"));
		cp.add(new JButton("Three"));
		cp.add(new JButton("Four"));
		cp.add(new JButton("Five"));
		cp.add(new JButton("Six"));
		f.pack();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocation(200, 200);
		f.setVisible(true);
		} catch (HeadlessException he) {
			System.err.println("CircleLayoutTest.test(): ignored, running headless.");
		}
	}
}
