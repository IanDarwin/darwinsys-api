package com.darwinsys.util;

import java.awt.Window;
import java.awt.event.*;

/** A WindowCloser - watch for Window Closing events, and
 * follow them up with setVisible(false) and dispose().
 * <p>
 * OBSOLETE in Swing applications later than 1.3, see the
 * "see also" section of Recipe 13.6; use JFrame.setDefaultCloseOperation().
 * @author Ian F. Darwin
 * @version $Id$
 */
public class WindowCloser extends WindowAdapter {

	/** The window we are to close */
	Window win;

	/** True if we are to exit as well. */
	boolean doExit = false;

	public WindowCloser(Window w) {
		this(w, false);
	}

	public WindowCloser(Window w, boolean exit) {
		win = w;
		doExit = exit;
	}

	public void windowClosing(WindowEvent e) {
		win.setVisible(false);
		win.dispose();
		if (doExit)
			System.exit(0);
	}
}
