import java.awt.*;
import java.awt.event.*;

/**
 * Simple class to non-exhaustively test out RelLayout layout manager.
 */
public class RelLayTest extends Frame {
	Button adButton;	// adjust (dummy here)
	Button qb;			// quit

	/**
	 * Simple main program to test out RelLayout.
	 * Invoke directly from Java interpreter.
	 */
	public static void main(String av[]) {
		RelLayTest f = new RelLayTest();
		f.setVisible(true);
	}

	/** Construct a RelLayTest test program. */
	RelLayTest() {
		super("RelLayout Tester");
		setLayout(new RelLayout(300, 150));
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
