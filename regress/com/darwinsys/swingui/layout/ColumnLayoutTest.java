package com.darwinsys.swingui.layout;

import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.junit.Test;

/**
 * Simple class to non-exhaustively test out RelLayout layout manager.
 */
public class ColumnLayoutTest {

	private static final long serialVersionUID = 5311489466425902707L;
	
	JButton adButton;	// adjust (dummy here)
	JButton qb;			// quit

	/**
	 * Simple main program to test out RelLayout.
	 * Invoke directly from Java interpreter.
	 */
	public static void main(String[] av) {
		JFrame jf = new JFrame("ColumnLayout1");
		new ColumnLayoutTest(jf, ColumnLayout.X_AXIS, 0, 0);
		jf = new JFrame("ColumnLayout1");
		new ColumnLayoutTest(jf, ColumnLayout.Y_AXIS, 0, 0);
		jf = new JFrame("ColumnLayout1");
		new ColumnLayoutTest(jf, ColumnLayout.X_AXIS, 10, 10);
		jf = new JFrame("ColumnLayout1");
		new ColumnLayoutTest(jf, ColumnLayout.Y_AXIS, 10, 10);
	}

	/** Construct a Test test program. */
	ColumnLayoutTest(JFrame jf, int alignment, int hpad, int vpad) {
		Container cp = jf.getContentPane();
		ColumnLayout cl = new ColumnLayout(alignment, hpad, vpad);
		cp.setLayout(cl);
		cp.add(new JButton("X"));
		cp.add(new JButton("MidWidth"));
		cp.add(new JButton("A Long Button"));
		cl.addSpacer(cp);
		cp.add(qb = new JButton("Quit"));
		qb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		jf.pack();
		jf.setVisible(true);
	}
	
	public ColumnLayoutTest() {
		// empty
	}
	
	@Test
	public void trivialTest() {
		try {
			main(null);
		} catch (HeadlessException he) {
			System.out.println("ColumnLayoutTest.test(): cannot test Headless");
		}
	}
}
