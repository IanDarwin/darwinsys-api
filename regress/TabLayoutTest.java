package regress;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.darwinsys.swingui.layout.TabLayout;

/**
 * Simple class to non-exhaustively test out TabLayout layout manager.
 */
public class TabLayoutTest extends JFrame {
	JButton qb;

	/**
	 * Simple main program to test out TabLayout.
	 */
	public static void main(String av[]) {
		TabLayoutTest f = new TabLayoutTest();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.show();
	}

	/** Construct a TabLayoutTest test program. */
	TabLayoutTest() {
		super("TabLayout Tester");
		Container mainp = getContentPane();
		TabLayout tl = new TabLayout(new JPanel());
		mainp.setLayout(tl);
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add("North", new JButton("North Stuff"));
		p.add("Center", new JButton("Center Stuff"));
		p.add("South", new JButton("South Stuff"));
		mainp.add("General", p);
		mainp.add("Interesting", new JLabel("More Stuff"));
		mainp.add("A way out", qb = new JButton("Quit"));
		qb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					System.exit(0);
				}
			});
		tl.show("General");
		pack();
	}
}
