import java.awt.*;
import com.darwinsys.swingui.WindowCloser;

/**
 * Simple class to non-exhaustively test out TabLayout layout manager.
 */
public class TabTest extends Frame {
	Button qb;

	/**
	 * Simple main program to test out TabTest.
	 * Invoke directly from Java interpreter.
	 */
	public static void main(String av[]) {
		TabTest f = new TabTest();
		f.addWindowListener(new WindowCloser(f));
		f.show();
	}

	/** Construct a TabTest test program. */
	TabTest() {
		super("TabTest Tester");
		// setLayout(new FlowLayout());
		// Panel mainp = new Panel();
		// add(mainp);
		Frame mainp = this;
		TabLayout tl = new TabLayout(new Panel());
		mainp.setLayout(tl);
		Panel p = new Panel();
		p.setLayout(new BorderLayout());
		p.add("North", new Button("North Stuff"));
		p.add("Center", new Button("Center Stuff"));
		p.add("South", new Button("South Stuff"));
		mainp.add("General", p);
		mainp.add("Interesting", new Label("More Stuff"));
		mainp.add("A way out", qb = new Button("Quit"));
		tl.show("General");
		pack();
	}

	/** Handle significant user actions, such as button presses. */
	public boolean action(Event e, Object o) {
		if (e.target == qb) {
			System.exit(0);
			return true;
		}
		return false;
	} 
}
