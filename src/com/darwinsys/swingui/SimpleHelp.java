package com.darwinsys.util;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

/*
 * Simple Help Frame based on JFC JEditorPane
 * May someday rewrite using JavaHelp API.
 */
public class SimpleHelp extends JFrame implements HyperlinkListener {
	/** The contentpane */
	protected Container cp;
	/** The editorpane */
	JEditorPane help;

	/* Construct a Help object. Just construct a JEditorPane
	 * with a URL, and it does all the help from there.
	 */
    public SimpleHelp(String name) {
		super(name + " Help Window");
		cp = getContentPane();
		getAccessibleContext().setAccessibleName(name + " Help Window");
		getAccessibleContext().setAccessibleDescription(
			"A window for viewing the help for " + name +
			", which is somewhat hyperlinked.");
	
		try {
			URL url = new File("help/index.html").toURL();
			// Only create the window once.
			if (help == null) {
				// System.out.println("Creat-ing help window for " + url);
				help = new JEditorPane(url);
				// System.out.println("Done!");
				help.setEditable(false);
				help.addHyperlinkListener(this);
				JScrollPane scroller = new JScrollPane();
				scroller.setBorder(BorderFactory.createTitledBorder(name + " Help"));
				scroller.getViewport().add(help);
				cp.add(BorderLayout.CENTER, scroller);
				addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						SimpleHelp.this.setVisible(false);
						SimpleHelp.this.dispose();
					}
				});
				setSize(500,400);
			} else {
				System.out.println("Re-using help window!");
			}
		} catch (MalformedURLException e) {
			System.out.println("Malformed URL: " + e);
		} catch (IOException e) {
			System.out.println("IOException: " + e);
		}
    }

    /**
     * Notification of a change relative to a hyperlink. 
	 * From: java.swing.event.HyperlinkListener
     */
    public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			URL target = e.getURL();
			// System.out.println("linkto: " + target);

			// Get the help panel's cursor and the wait cursor
			Cursor oldCursor = help.getCursor();
			Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
			help.setCursor(waitCursor);

			// Now arrange for the page to get loaded asynchronously,
			// and the cursor to be set back to what it was.
			SwingUtilities.invokeLater(new PageLoader(target, oldCursor));
		}
	}

    /**
     * Inner class that loads a URL synchronously into the help panel.
	 * Loads it later than the request so that a cursor change
     * can be done at the very end.
	 * @author BORROWED FROM SUN'S SWING DEMO, UNTIL JAVAHELP AVAILABLE
     */
    class PageLoader implements Runnable {
		URL url;
		Cursor cursor;
	
		PageLoader(URL u, Cursor c) {
			url = u;
			cursor = c;
		}

        public void run() {
			// System.out.println("PageLoader: u=" + url);
			if (url == null) {
				// restore the original cursor
				help.setCursor(cursor);

				// PENDING(prinz) remove this hack when 
				// automatic validation is activated.
				Container parent = help.getParent();
				parent.repaint();
			} else {
				Document doc = help.getDocument();
				try {
					help.setPage(url);
				} catch (Exception ioe) {
					help.setDocument(doc);
					getToolkit().beep();
				} finally {
					// schedule the cursor to revert after
					// the paint has happended.
					url = null;
					SwingUtilities.invokeLater(this);
				}
			}
		}
    }

}
