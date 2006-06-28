package com.darwinsys.swingui;

import java.awt.*;
import javax.swing.*;

import com.darwinsys.swingui.layout.CircleLayout;

/** Testbed for CircleLayout layout manager.
 * @author	Ian Darwin, http://www.darwinsys.com/
 * @version $Id$
 */
public class CircleLayoutTest {

	/** "main program" method - construct and show */
	public static void main(String[] av) {
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
	}
}
