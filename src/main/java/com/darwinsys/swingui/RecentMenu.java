package com.darwinsys.swingui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.darwinsys.util.PrefsUtils;
import com.darwinsys.util.RecentItems;

/**
 * Maintain a "Recent Items" menu component. Files must be opened under our
 * control to track them, so the caller must override a "template method"
 * named loadFile, and all open operations must be channeled through the RecentMenu's
 * openFile method, which calls loadFile and then updates the Recent Items.
 * <p>
 * Recent items are persisted across runs of the program, using java.util.prefs.Prefences under a
 * UserNode with the class passed into the constructor, which may be a Class descriptor (from within a static main
 * method using MyClassName.class) or simply passing main program (its getClass method will be called).
 * <p>
 * Will most commonly be used to maintain a list of recent files.
 * Typical usage might be as follows:
 * <pre>
 * 		// Assume this code is in MyMainClass constructor
 * 		JMenuItem open = new JMenuItem("Open");
 * 		fileMenu.add(open);
 * 		final RecentMenu recent = new RecentMenu(this) {
 *			public void loadFile(String fileName) throws IOException {
 *				myModel.openFile(fileName);
 *			}
 *		};
 *		open.addActionListener(() -> {
 *				try {
 *					// maybe get "someFileName" from a JFileChooser...
 *					recent.openFile(someFileName);
 *				} catch (IOException e) {
 *					myErrorPopup("Could not open file" + fileName, e);
 *			}
 *		});
 *		fileMenu.add(recent);
 *		JMenuItem clearItem = new JMenuItem("Clear Recent Files");
 *		clearItem.addActionListener(() -> recents.clear());
 * </pre>
 * @author Ian Darwin
 */
public abstract class RecentMenu extends JMenu implements RecentItems.Callback {

	private static final long serialVersionUID = 2153622797665694360L;

	public final static int DEFAULT_MAX_RECENT_FILES = 5;

	/** The List of recent files */
	protected RecentItems recentItemNames;

	final Preferences prefs;

	/** Construct a RecentMenu with a given class and the number of items to hold
	 * @param mainClassInstance The main class
	 * @param max The max number of items to allow
	 */
	public RecentMenu(Object mainClassInstance, int max) {
		this(PrefsUtils.getUserPrefsNode(mainClassInstance), max);
	}

	/** Construct a RecentMenu with an existing Preferences, and size
	 * @param prefs The Preferences object
	 * @param max The size of the desired list
	 */
	public RecentMenu(Preferences prefs, int max) {
		super("Recent Items");
		this.prefs = prefs;
		recentItemNames = new RecentItems(prefs, this, max);
		reload(recentItemNames.getLiveList());
	}

	/** Construct a RecentMenu with a given class.
	 * @param mainClassInstance The main class
	 */
	public RecentMenu(Object mainClassInstance) {
		this(mainClassInstance, DEFAULT_MAX_RECENT_FILES);
	}

	/** Construct a RecentMenu with an existing Prefs and default size.
	 * @param prefs The existing Preferences object.
	 */
	public RecentMenu(Preferences prefs) {
		this(prefs, DEFAULT_MAX_RECENT_FILES);
	}

	/**
	 * Call back to the main program to load the file, and and update
	 * the recent files list.
	 * @param fileName The input
	 * @throws java.io.IOException on error
	 */
	public void openFile(String fileName) throws IOException {
		loadFile(fileName);
		recentItemNames.putRecent(fileName);
	}

	/**
	 * This is a template method that the caller must provide to
	 * actually open a file.
	 * @param fileName The input
	 * @throws java.io.IOException on error
	 */
	public abstract void loadFile(String fileName) throws IOException;

	/** This just calls the template method
	 * @param file The java.io.File object
	 * @throws java.io.IOException on error
	 */
	public void loadFile(File file) throws IOException {
		loadFile(file.getAbsolutePath());
	}

	/**
	 * ActionListener that is used by all the Menuitems in the Recent Menu;
	 * just opens the file named by the MenuItem's text.
	 */
	private ActionListener recentOpener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JMenuItem mi = (JMenuItem) e.getSource();
			try {
				openFile(mi.getText());
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(RecentMenu.this, "Could not open file " + e1);
			}
		}
	};


	/**
	 * Load or re-load the recentFileMenu
	 * @param list The replacement menu items
	 */
	public void reload(List<String> list) {
		setEnabled(false);
		// Clear out all menu items
		for (int i = getMenuComponentCount() - 1; i >= 0; i--) {
			remove(0);
		}

		// Copy from Prefs into Menu
		JMenuItem mi;
		final Iterator<String> iterator = list.iterator();
		while (iterator.hasNext()) {
			String f = iterator.next();
			if (f == null) {	// Stop on first missing
				break;
			}
			// Drop from list if file has been deleted.
			if (new File(f).exists()) {
				// Since at least one item valid, enable menu
				setEnabled(true);
				// And add to Menu
				this.add(mi = new JMenuItem(f));
				mi.addActionListener(recentOpener);
			} else {
				iterator.remove();
			}
		}
	}

	/** Clear the menu */
	public void clear() {
		// Clear out all menu items
		for (int i = getMenuComponentCount() - 1; i >= 0; i--) {
			remove(0);
		}
		recentItemNames.clear();
	}
}
