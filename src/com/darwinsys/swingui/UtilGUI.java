package com.darwinsys.swingui;

import java.awt.*;

/** Utilities for GUI work.
 * @version $Id$
 */
public class UtilGUI {

	/** Return true if we are running MacOS; needs a few GUI tweaks if so. */
	public static boolean isMacOS() {
		return System.getProperty("mrj.version") != null;
	}

	/** Set a few common properties for the given application if
	 * we are running under MacOS.
	 * Usage Example:
	 * <pre>
	 *	if (UtilGUI.isMacOS()) {
	 *		UtilGUI.setMacOS("JabaDex");
	 * </pre>
	 * @parameter appName - the name of the Application.
	 */
	public static void setMacOS(String appName) {
		System.setProperty("apple.laf.useScreenMenuBar",  "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name",
			appName);
	}

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

	/** Maximize a window, the hard way. */
	public static void maximize(Window w) {
		Dimension us = w.getSize(),
			them = Toolkit.getDefaultToolkit().getScreenSize();
		w.setBounds(0,0, them.width, them.height);
	}
}
