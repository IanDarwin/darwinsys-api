import com.darwinsys.util.LabelText;
import java.awt.*;
import javax.swing.*;

/** Test program for LabelText class.
 * STATUS: NOT WORKING!!
 * @author	Ian Darwin, ian@darwinsys.com
 * @version $Id$
 */
public class LabelTextTest extends JComponent {

	/** "main program" method - construct and show */
	public static void main(String[] av) {
		JFrame f = new JFrame("LabelTextTest");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		LabelTextTest comp = new LabelTextTest();
		comp.setBackground(Color.yellow);
		Container cp = f.getContentPane();
		cp.setLayout(new FlowLayout());
		cp.add(comp);
		f.pack();
		f.setLocation(200, 200);
		f.setVisible(true);
	}

	/** Construct the object including its GUI */
	public LabelTextTest() {
		super();
		JComponent foo;
		add(foo = new LabelText("Hello, and welcome to the world of Java"));
		foo.setFont(new Font("helvetica", Font.BOLD, 24));
		add(foo = new LabelText("Exit")); 
		foo.setForeground(Color.red);
	}
}
