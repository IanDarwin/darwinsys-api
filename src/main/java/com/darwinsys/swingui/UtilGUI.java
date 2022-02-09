package com.darwinsys.swingui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/** Utilities for GUI work.
 */
// tag::main[]
// package com.darwinsys.swingui;
public class UtilGUI {

	/** Centre a Window, Frame, JFrame, Dialog, etc.
	 * @param w The window
	 */
	public static void centre(final Window w) {
		// After packing a Frame or Dialog, centre it on the screen.
		Dimension us = w.getSize(), 
			them = Toolkit.getDefaultToolkit().getScreenSize();
		int newX = (them.width - us.width) / 2;
		int newY = (them.height- us.height)/ 2;
		w.setLocation(newX, newY);
	}

	/** Center a Window, Frame, JFrame, Dialog, etc., 
	 * but do it the American Spelling Way :-)
	 * @param w The window
	 */
	public static void center(final Window w) {
		UtilGUI.centre(w);
	}

	/** Pack and Center the given JFrame
	 * @Return the JFrame for method chaining
	 */
	public static JFrame packAndCenter(JFrame jf) {
		jf.pack();
		UtilGUI.centre(jf);
		return jf;
	}

	/** Maximize a window, the hard way.
	 * @param w The Window instance
	 */
	public static void maximize(final Window w) {
		Dimension them = 
			Toolkit.getDefaultToolkit().getScreenSize();
		w.setBounds(0,0, them.width, them.height);
	}
	
	/** 
	 * Copy a string value to the system copy buffer
	 * @param c The The component
	 * @param srcData The data to copy
	 */
	public static void setSystemClipboardContents(Component c, String srcData) {
		if (srcData != null) {
			Clipboard clipboard = c.getToolkit().getSystemClipboard();
			StringSelection contents = new StringSelection(srcData);
			clipboard.setContents(contents, new ClipboardOwner() {
				public void lostOwnership(Clipboard clipboard,
					Transferable contents) {

					// don't care
				}
			});
		}
	}

	/** Print a yes/no prompt; return true if the user presses yes
	 * @param parent The parent frame
	 * @param message The prompt string
	 * @return True if they confirmed the message
	 */
	public static boolean confirm(JFrame parent, String message) {
		int confirm = JOptionPane.showConfirmDialog(parent, message, "Confirm", 
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		// Only selecting Yes choice will result in true
		return confirm == 0;
	}
	
	/**	Save the X and Y locations in Preferences node provided.
	 * @param pNode The Prefs node for the location
	 * @param w The window whose location is to be saved
	 */
	public static void setSavedLocation(
		final Preferences pNode, final Window w) {

		Point where = w.getLocation();
		int x = (int)where.getX();
		pNode.putInt("mainwindow.x", Math.max(0, x));
		int y = (int)where.getY();
		pNode.putInt("mainwindow.y", Math.max(0, y));
	}

	/** Retrieve the saved X and Y from Preferences
	 * @param pNode The Preferences node to hold the location
	 * @return The saved location
	 */
	public static Point getSavedLocation(final Preferences pNode) {
		int savedX = pNode.getInt("mainwindow.x", -1);
		int savedY = pNode.getInt("mainwindow.y", -1);
		return new Point(savedX, savedY);
	}
	
	/** 
	 * Track a Window's position across application restarts; location is saved
	 * in a Preferences node that you pass in; we attach a ComponentListener to
	 * the Window.
	 * @param w The Window itself
	 * @param pNode The preferences node to hold the location
	 */
	public static void monitorWindowPosition(
		final Window w, final Preferences pNode) {

		// Get the current saved position, if any
		Point p = getSavedLocation(pNode);
		int savedX = (int)p.getX();
		int savedY = (int)p.getY();
		if (savedX != -1) {
			// Move window to is previous location
			w.setLocation(savedX, savedY);
		} else {
			// Not saved yet, at least make it look nice
			centre(w);
		}
		// Now make sure that if the user moves the window,
		// we will save the new position.
		w.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				setSavedLocation(pNode, w);
			}
		});
	}
}
// end::main[]
