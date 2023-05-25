package com.darwinsys.jclipboard;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.darwinsys.swingui.UtilGUI;
import com.darwinsys.swingui.layout.EntryLayout;
import com.darwinsys.util.FileProperties;

/**
 * JClipBoard - program to display common items and copy them to
 * the system clipboard on request
 * <br/>
 * @version $Id$
 */
public class JClipBoard extends JComponent {

	private static final long serialVersionUID = 3258689901418723377L;

	private static final String PROPERTIES_FILE_NAME = ".jclipboard.properties";

	private static final String FULL_PROPS_FILE_NAME = 
		System.getProperty("user.home") + File.separator + PROPERTIES_FILE_NAME;

	private static final String iconFileName = "jclipboard/trayicon.gif";

	public static void main(String[] args) throws IOException {

		JFrame jf = new JFrame("jClipboard");
		JClipBoard program = new JClipBoard(jf);
		jf.getContentPane().add(program);
		jf.pack();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Don't need both SystemTray and main window
		if (SystemTray.isSupported()) {
			jf.setState(JFrame.ICONIFIED);
		}
		jf.setVisible(true);
	}

	/** The main frame */
	final JFrame jf;

	/** The list of name-value pairs loaded from disk */
	final Properties projects;

	final Map<String, JTextField> map = new HashMap<String, JTextField>();

	Preferences p = Preferences.userNodeForPackage(JClipBoard.class);

	private SystemTray systemTray;

	/** Must use AWT Menus for Systemtray, not swing :-( */
	private PopupMenu trayPopupMenu;

	/** Construct the gui, setting up the VIEW and connecting CONTROLLERs to it */
	JClipBoard(JFrame theFrame) throws IOException {

		jf = theFrame;
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final URL iconFileURL = getClass().getClassLoader().getResource(iconFileName);
		final Image image =
			Toolkit.getDefaultToolkit().getImage(iconFileURL);
		jf.setIconImage(image);

		trayPopupMenu = new PopupMenu();
		if (SystemTray.isSupported()) {
			systemTray = SystemTray.getSystemTray();
			TrayIcon trayIcon =
				new TrayIcon(image, "JClipBoard", trayPopupMenu);
			trayIcon.setImageAutoSize(true);

			try {
				systemTray.add(trayIcon);
			} catch (AWTException e) {
				final String trayAddFailure = "Boring - TrayIcon could not be added.";
				JOptionPane.showMessageDialog(
						jf, trayAddFailure);
				final ExceptionInInitializerError newException = new ExceptionInInitializerError(trayAddFailure);
				newException.initCause(e);
				throw newException;
			}
		}

		// Set the frame's location to saved value, if set, and arrange
		// for the location to be set whenever the window is moved.
		UtilGUI.monitorWindowPosition(jf, p);

		JMenuItem mi;
		JMenuBar mb = new JMenuBar();
		jf.setJMenuBar(mb);
		JMenu fileMenu = new JMenu("File");
		mb.add(fileMenu);
		JMenuItem saveMenuItem = new JMenuItem("Save");
		fileMenu.add(saveMenuItem);

		final ActionListener saveAction = new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							doSave();
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(jf, e1.toString(),
									"Error", JOptionPane.ERROR_MESSAGE);
						}
					}

				};
		saveMenuItem.addActionListener(saveAction);

		fileMenu.addSeparator();
		JMenuItem quitMenuitem = new JMenuItem("Exit");
		fileMenu.add(quitMenuitem);
		final ActionListener quitAction = new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				};
		quitMenuitem.addActionListener(quitAction);

		JMenu editMenu = new JMenu("Edit");
		mb.add(editMenu);
		JMenuItem newMenuItem = new JMenuItem("New");
		editMenu.add(newMenuItem);
		final ActionListener newAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] message = new Object[4];
				message[0] = "Name";
				final JTextField nameTextField = new JTextField();
				message[1] = nameTextField;
				message[2] = "Value";
				final JTextField valueTextField = new JTextField();
				message[3] = valueTextField;

				// Options
				String[] options = { "OK", "Cancel" };
				int result =
					JOptionPane.showOptionDialog(jf, message, // contents
						"New Field", 				// window title
						JOptionPane.DEFAULT_OPTION, // option type
						JOptionPane.INFORMATION_MESSAGE, // message type
						null, 						// no icon
						options, 					// button labels
						options[0]					// default button
						);
				switch (result) {
				case 0: // yes
					String newName = nameTextField.getText();
					if (map.containsKey(newName)) {
						JOptionPane.showMessageDialog(jf, String.format(
								"Field %s already exists", newName));
						return;
					}
					addField(newName, valueTextField.getText());
					jf.pack();
					break;
				case 1: case -1:
					JOptionPane.showMessageDialog(jf, "What a waste!");
					break;
				default:
					JOptionPane.showMessageDialog(jf, "What am I doing here?");
					break;
				}
			}
		};
		newMenuItem.addActionListener(newAction);

		// The SystemTray popup menu
		MenuItem popupNewMenuItem = new MenuItem("New");
		popupNewMenuItem.addActionListener(newAction);
		trayPopupMenu.add(popupNewMenuItem);
		MenuItem popupSaveMenuItem = new MenuItem("Save");
		popupSaveMenuItem.addActionListener(saveAction);
		trayPopupMenu.add(popupSaveMenuItem);
		MenuItem popupQuitMenuItem = new MenuItem("Exit");
		trayPopupMenu.add(popupQuitMenuItem);
		popupQuitMenuItem.addActionListener(quitAction);
		trayPopupMenu.addSeparator();

		JMenu helpMenu = new JMenu("Help");
		mb.add(helpMenu);
		helpMenu.add(mi = new JMenuItem("About"));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(jf,
					"<html><font color='red'>jClipboard</font> - Ian's 'Clipper'<br>" +
					"$Id$",
					"jTkr", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		//setLayout(new GridLayout(0, 2));
		setLayout(new EntryLayout(new double[] {.3, .7}));

		projects = new FileProperties(FULL_PROPS_FILE_NAME);

		// Use Enumeration instead of keySet() in a failed
		// attempt to avoid generics warnings
		Enumeration<String> propertyNames = (Enumeration<String>)projects.propertyNames();
		List<String> keyNames = new ArrayList<String>();
		while (propertyNames.hasMoreElements()) {
			keyNames.add(propertyNames.nextElement());
		}
		Collections.sort(keyNames, String.CASE_INSENSITIVE_ORDER);
		Iterator projectsIterator = keyNames.iterator();
		while (projectsIterator.hasNext()) {
			String name = (String)projectsIterator.next();
			String describe = (String)projects.get(name);
			addField(name, describe);
		}
	}

	private void addField(String name, String describe) {
		JButton b = new JButton(name);
		MenuItem mi = new MenuItem(name);
		trayPopupMenu.add(mi);
		this.add(b);
		JTextField copy = new JTextField(describe);
		map.put(name, copy);
		final Copier listener = new Copier(copy);
		b.addActionListener(listener);
		mi.addActionListener(listener);
		add(copy);
	}

	class Copier implements ActionListener {
		JTextField b;
		Copier(JTextField b) {
			this.b = b;
		}
		public void actionPerformed(ActionEvent e) {
			// System.out.println("Copier.actionPerformed()");
			b.setSelectionStart(0);
			b.setSelectionEnd(b.getText().length());
			b.copy();
		}
	}

	/**
	 * Set the saved location to the current location,
	 * but either coordinate will be set to 0 if negative.
	 */
	protected void setSavedLocation(ComponentEvent e) {
		Point where = jf.getLocation();
		int x = (int)where.getX();
		p.putInt("mainwindow.x", Math.max(0, x));
		int y = (int)where.getY();
		p.putInt("mainwindow.y", Math.max(0, y));
	}

	/**
	 * Return the saved location from Preferences.
	 * Either coordinate will be set to 0 if it is not found
	 * in Preferences or if it is less than 0 (this seems to happen
	 * with KDE or FVWM).
	 */
	protected Point getSavedLocation() {
		int x = Math.max(0, p.getInt("mainwindow.x", 0));
		int y = Math.max(0, p.getInt("mainwindow.y", 0));
		return new Point(x, y);
	}

	// A sort of model...

	void doSave() throws IOException {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter(FULL_PROPS_FILE_NAME));
			out.println("# created by JClipBoard " + new Date());
			Iterator<String> projectsIterator = map.keySet().iterator();
			while (projectsIterator.hasNext()) {
				String name = projectsIterator.next();
				out.println(name + "=" + map.get(name).getText());
			}
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}
