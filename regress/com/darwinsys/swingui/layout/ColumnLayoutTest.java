package regress;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

import com.darwinsys.swingui.layout.ColumnLayout;

/**
 * Simple class to non-exhaustively test out RelLayout layout manager.
 */
public class ColumnLayoutTest extends JFrame {
	JButton adButton;	// adjust (dummy here)
	JButton qb;			// quit

	/**
	 * Simple main program to test out RelLayout.
	 * Invoke directly from Java interpreter.
	 */
	public static void main(String[] av) {
		new ColumnLayoutTest(ColumnLayout.X_AXIS, 0, 0).setVisible(true);
		new ColumnLayoutTest(ColumnLayout.Y_AXIS, 0, 0).setVisible(true);
		new ColumnLayoutTest(ColumnLayout.X_AXIS, 10, 10).setVisible(true);
		new ColumnLayoutTest(ColumnLayout.Y_AXIS, 10, 10).setVisible(true);
	}

	/** Construct a Test test program. */
	ColumnLayoutTest(int alignment, int hpad, int vpad) {
		super("Column Layout Tester");
		Container cp = getContentPane();
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
		pack();
	}
}
