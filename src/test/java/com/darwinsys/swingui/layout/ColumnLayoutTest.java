package com.darwinsys.swingui.layout;

import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Simple class to non-exhaustively test out RelLayout layout manager.
 */
public class ColumnLayoutTest {

	private static final long serialVersionUID = 5311489466425902707L;
	
	JButton adButton;	// adjust (dummy here)
	JButton qb;			// quit


	@ParameterizedTest()
	@CsvSource({
		// 120 is ColumnLayout.XAXIS, 121 is Y
		"120, 0, 0",
		"121, 0, 0",
		"120, 10, 10",
		"121, 10, 10",
	})
	public void testLayout(int alignment, int hpad, int vpad) {
		JFrame jf = null;
		try {
			jf = new JFrame("ColumnLayout");
		} catch (HeadlessException he) {
			System.out.println("ColumnLayoutTest.test(): cannot test Headless");
			return;
		}
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
}
