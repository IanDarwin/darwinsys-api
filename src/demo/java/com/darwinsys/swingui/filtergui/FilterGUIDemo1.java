package com.darwinsys.swingui.filtergui;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.darwinsys.swingui.FilterGUI;

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
		final FilterGUI<String> comp = new FilterGUI<String>(filters, DEFAULT_FILTER);
		Container cp = f.getContentPane();
		cp.add(BorderLayout.CENTER, comp);
		JButton b = new JButton("Show");
		cp.add(BorderLayout.SOUTH, b);
		b.addActionListener(e -> {
			comp.getSelected().forEach(o -> {
				System.out.println(o);
			});
		});

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		f.pack();
		f.setLocation(200, 200);
		f.setVisible(true);
	}
}
