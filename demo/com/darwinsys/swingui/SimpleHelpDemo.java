package com.darwinsys;

import javax.swing.JFrame;

import com.darwinsys.swingui.SimpleHelp;

public class SimpleHelpDemo {
	/** Main */
	public static void main(String argv[]) {
		if (argv.length == 0)
			throw new IllegalArgumentException(
			"Usage: SimpleHelpDemo helpFile");
		JFrame jf = new SimpleHelp("Demo", argv[0]);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
	}
}
