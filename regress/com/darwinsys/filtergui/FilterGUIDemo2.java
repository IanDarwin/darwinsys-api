package regress.filtergui;

import javax.swing.JFrame;

import com.darwinsys.swingui.FilterGUI;

/** A simple demo of FilterGUI */
public class FilterGUIDemo2 {

	/** "main program" method - construct and show */
	public static void main(String[] av) {

		/** Inner class to represent real MyFilter implementations */
		class BasicFilter extends MyFilter {
			String title;
			BasicFilter(String s) {
				title = s;
			}
			public String toString() {
				return title;
			}
			public void write(byte[] data) throws MyFilterException {
				next.write(data);
			}
		}

		MyFilter[] filters = { 
			new BasicFilter("Basic Copy"),
			new BasicFilter("Noise Reduction"),
			new BasicFilter("RLE")
		};

		int DEFAULT_FILTER = 1;	// i.e., filters[DEFAULT_FILTER] is default

		// create a this object, tell it to show up
		final JFrame f = new JFrame("Filter FilterGUI");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FilterGUI comp = new FilterGUI(filters, DEFAULT_FILTER);
		f.getContentPane().add(comp);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.setLocation(200, 200);
		f.setVisible(true);
	}
}
