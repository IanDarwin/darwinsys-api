package regress;

import java.awt.*;
import java.awt.event.*;

import com.darwinsys.swingui.layout.RelativeLayout;

/**
 * Simple class to non-exhaustively test out RelativeLayout layout manager.
 */
public class RelativeLayoutTest extends Frame {
	Button adButton;	// adjust (dummy here)
	Button qb;			// quit

	/**
	 * Simple main program to test out RelativeLayout.
	 * Invoke directly from Java interpreter.
	 */
	public static void main(String[] av) {
		RelativeLayoutTest f = new RelativeLayoutTest();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

	/** Construct a RelativeLayoutTest test program. */
	public RelativeLayoutTest() {
		super("RelativeLayout Tester");
		setLayout(new RelativeLayout(300, 150));
		add("80,20", adButton = new Button("MidWidth"));
		add("150,75", qb = new Button("Quit"));
		qb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		pack();
	}
}
