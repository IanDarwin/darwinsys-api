package regress;

import com.darwinsys.swingui.LabelText;
import java.awt.*;
import javax.swing.*;

/** Test program for LabelText class.
 * @author	Ian Darwin, http://www.darwinsys.com/
 * @version $Id$
 */
public class LabelTextTest extends JPanel {

	/** "main program" method - construct and show */
	public static void main(String[] argv) {
		JFrame f = new JFrame("LabelTextTest");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		LabelTextTest testee = new LabelTextTest();
		f.setContentPane(testee);	// it is a JPanel
		f.pack();
		f.setLocation(200, 200);
		f.setVisible(true);
	}

	/** Construct the object including its GUI */
	public LabelTextTest() {
		super();
		setLayout(new GridLayout(0, 1));
		JComponent foo;
		add(foo = new LabelText("Hello, and welcome to the world of Java"));
		foo.setFont(new Font("helvetica", Font.BOLD, 24));
		add(foo = new LabelText("Exit")); 
		foo.setForeground(Color.red);
	}
}
