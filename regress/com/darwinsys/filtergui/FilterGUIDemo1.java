import com.darwinsys.util.WindowCloser;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/** A simple demo of FilterGUI */
public class FilterGUIDemo1 {

	/** "main program" method - construct and show */
	public static void main(String[] av) {

		String[] filters = { 
			"Apples",
			"Bananas",
			"Run for it!"
		};

		int DEFAULT_FILTER = 1;	// i.e., filters[DEFAULT_FILTER] is default

		// create a this object, tell it to show up
		final JFrame f = new JFrame("FilterGUI Demo 1");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FilterGUI comp = new FilterGUI(filters, DEFAULT_FILTER);
		f.getContentPane().add(comp);
		f.addWindowListener(new WindowCloser(f));
		f.pack();
		f.setLocation(200, 200);
		f.setVisible(true);
	}
}
