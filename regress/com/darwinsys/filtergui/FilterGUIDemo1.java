package regress.filtergui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

import com.darwinsys.swingui.*;

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
		final FilterGUI comp = new FilterGUI(filters, DEFAULT_FILTER);
		Container cp = f.getContentPane();
		cp.add(BorderLayout.CENTER, comp);
		JButton b = new JButton("Show");
		cp.add(BorderLayout.SOUTH, b);
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				java.util.List l = comp.getSelected();
				Iterator it = l.iterator();
				while (it.hasNext()) {
					System.out.println(it.next());
				}
			}
		});

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		f.pack();
		f.setLocation(200, 200);
		f.setVisible(true);
	}
}
