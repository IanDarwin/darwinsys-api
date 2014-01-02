package com.darwinsys.swingui.layout;

import java.awt.Container;
import java.awt.HeadlessException;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.junit.Test;

/** Testbed for EntryLayout layout manager.
 * @author	Ian Darwin, http://www.darwinsys.com/
 */
// BEGIN main
public class EntryLayoutTest {

	/** "main program" method - construct and show */
	public static void main(String[] av) {
		testTwoCols();
		testFiveCols();
	}

	static void testTwoCols() {
		final JFrame f = new JFrame("EntryLayout Demonstration");
		Container cp = f.getContentPane();
		double widths[] = { .33, .66 };
		cp.setLayout(new EntryLayout(widths));
		cp.add(new JLabel("Login:", SwingConstants.RIGHT));
		cp.add(new JTextField(10));
		cp.add(new JLabel("Password:", SwingConstants.RIGHT));
		cp.add(new JPasswordField(20));
		cp.add(new JLabel("Security Domain:", SwingConstants.RIGHT));
		cp.add(new JTextField(20));
		// cp.add(new JLabel("Monkey wrench in works"));
		f.pack();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocation(200, 200);
		f.setVisible(true);
	}

	static void testFiveCols() {
		final JFrame f = new JFrame("EntryLayout Five Columns");
		Container cp = f.getContentPane();
		double widths[] = { .25, .33, .10, .10, .20 };
		cp.setLayout(new EntryLayout(widths));
		cp.add(new JLabel("Login:", SwingConstants.RIGHT));
		cp.add(new JTextField(10));
		cp.add(new JCheckBox());
		cp.add(new JCheckBox());
		cp.add(new JCheckBox());
		cp.add(new JLabel("Password:", SwingConstants.RIGHT));
		cp.add(new JPasswordField(20));
		cp.add(new JCheckBox());
		cp.add(new JCheckBox());
		cp.add(new JCheckBox());
		cp.add(new JLabel("Security Domain:", SwingConstants.RIGHT));
		cp.add(new JTextField(20));
		cp.add(new JCheckBox());
		cp.add(new JCheckBox());
		cp.add(new JCheckBox());
		f.pack();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocation(200, 200);
		f.setVisible(true);
	}
	
	@Test
	public void trivialTest() {
		try {
			main(null);
		} catch (HeadlessException he) {
			System.out.println("EntryLayoutTest.test(): cannot test Headless");
		}
	}
}
// END main
