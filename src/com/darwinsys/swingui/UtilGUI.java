package com.darwinsys.util;

import java.awt.*;

/** Utilities for GUI work.
 * @version $Id$
 */
public class UtilGUI {
	/** Centre a Window, Frame, JFrame, Dialog, etc. */
	public static void centre(Window w) {
		// After packing a Frame or Dialog, centre it on the screen.
		Dimension us = w.getSize(), 
			them = Toolkit.getDefaultToolkit().getScreenSize();
		int newX = (them.width - us.width) / 2;
		int newY = (them.height- us.height)/ 2;
		w.setLocation(newX, newY);
	}
	/** Center a Window, Frame, JFrame, Dialog, etc., 
	 * but do it the American Spelling Way :-)
	 */
	public static void center(Window w) {
		UtilGUI.centre(w);
	}
}
