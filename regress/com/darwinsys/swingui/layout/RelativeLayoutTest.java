package  swingui;

import java.awt.Button;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.darwinsys.swingui.layout.RelativeLayout;

/**
 * Simple class to non-exhaustively test out RelativeLayout layout manager.
 */
public class RelativeLayoutTest extends JFrame {

	private static final long serialVersionUID = -5153658589244661773L;

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
		Button qb;			// quit
		Container cp = getContentPane();
		cp.setLayout(new RelativeLayout(300, 150));
		cp.add("80,20", new Button("MidWidth"));
		cp.add("150,75", qb = new Button("Quit"));
		qb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		pack();
	}
}
