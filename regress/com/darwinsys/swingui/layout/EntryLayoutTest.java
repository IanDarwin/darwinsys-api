import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** Testbed for EntryLayout layout manager.
 * @author	Ian Darwin, ian@darwinsys.com
 * @version $Id$
 */
public class EntryLayoutTest {

	/** "main program" method - construct and show */
	public static void main(String[] av) {
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
		f.addWindowListener(new WindowCloser(f, true));
		f.setLocation(200, 200);
		f.setVisible(true);
	}
}
