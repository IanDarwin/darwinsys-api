import javax.swing.*;

/** Simple test for LabelText.
 * @version $Id$
 */
public class LabelTextTest {
	/** "main program" for testing - construct and show */
	public static void main(String[] av) {
		// create a LabelText object, tell it to show up
		final JFrame f = new JFrame("LabelText");
		Container cp = f.getContentPane();
		cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));

		LabelText comp;
		comp = new LabelText("Test");
		comp.setLabelAlignment(JLabel.LEFT);
		comp.setText("Hello world");
		cp.add(comp);
		comp = new LabelText("Test 2");
		comp.setLabelAlignment(JLabel.CENTER);
		cp.add(comp);
		comp = new LabelText("Test 3");
		comp.setLabelAlignment(JLabel.RIGHT);
		comp.setText("Entry area");
		cp.add(comp);

		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				f.setVisible(false);
				f.dispose();
				System.exit(0);
			}
		});
		f.pack();
		f.setLocation(200, 200);
		f.setVisible(true);
	}
}
