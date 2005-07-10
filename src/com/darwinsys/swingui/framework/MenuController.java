package com.darwinsys.swingui.framework;

import java.awt.Graphics;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.darwinsys.swingui.SimpleHelp;
import com.darwinsys.swingui.UtilGUI;

/** Partly-internationalized Menu Controller for general use.
 * To try it out with different languages, use
 *		java Menus
 *		java -Duser.language=es Menus
 */
public class MenuController {
	/** The main frame for the puzzle editing/display. */
	JFrame parentFrame;
	/** The main program; link to the data model */
	Model model;
	/** A print job */
	PrintJob printJob;

	/** Construct the object including its GUI */
	public MenuController(JFrame prnt, Model mod) {
		parentFrame = prnt;
		model = mod;
	}
	
	protected JFileChooser chooser;

	/** Create all the menus.
	 * XXX Should split into makeFileMenu(), makeEditMenu(), etc.
	 * Then subclass could override and call super, override and not call super, or do nothing,
	 * for each menu that we provide.
	 */
	public void makeMenus() {

		JMenuItem mi;		// used in various spots

		JMenuBar mb = new JMenuBar();
		parentFrame.setJMenuBar(mb);

		ResourceBundle b = ResourceBundle.getBundle("Menu");

		JMenu fm = mkMenu(b, "file");
		fm.add(mi = mkMenuItem(b, "file", "open"));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fileName = null;
				
				if (model.getFileName() != null) {
					model.openFile((String)null);
					return;
				}
				
				if (chooser == null) {
					chooser =  new JFileChooser();
				}
				
				int returnVal = chooser.showOpenDialog(parentFrame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = chooser.getSelectedFile();
                        fileName = file.getPath();
				}
				model.openFile(fileName);
				parentFrame.pack();	// fit to new size
			}
		});
		fm.add(mi = mkMenuItem(b, "file", "new"));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fileName = JOptionPane.showInputDialog(parentFrame, "FileName");
				model.newFile(fileName);
			}
		});
		fm.add(mi = mkMenuItem(b, "file", "save"));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.saveFile(null);
			}
		});
		fm.add(mi = mkMenuItem(b, "file", "close"));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.closeFile();
			}
		});
		fm.addSeparator();

		/** Inner class for printing */
		class PrintListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				printJob = Toolkit.getDefaultToolkit().getPrintJob(
					parentFrame, "Print CrossWord", null);
				if (printJob /* still */ == null)
					return;
				Graphics g = printJob.getGraphics();
				if (g == null)
					throw new IllegalArgumentException(
						"pjob.getGraphics() gave me a null!");
				model.doPrint(g);
				g.dispose();
				printJob.end();
			}
		}
		// Print
		fm.add(mi = mkMenuItem(b, "file", "print"));
		mi.addActionListener(new PrintListener());

		fm.addSeparator();

		fm.add(mi = mkMenuItem(b, "file", "exit"));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.exit(0);
			}
		});
		mb.add(fm);

		
		// Edit Menu
		JMenu em = mkMenu(b, "edit");
		em.add(mi = mkMenuItem(b, "edit", "copy"));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.copySelection();
				parentFrame.repaint();
			}
		});
		em.add(mi = mkMenuItem(b, "edit", "paste"));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.pasteSelection();
			}
		});
		em.add(mi = mkMenuItem(b, "edit", "clear"));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.clearSelection();
			}
		});
		mb.add(em);
		
		addAdditionalMenus(mb);

		JMenu hm = mkMenu(b,  "help");
		hm.add(mi = mkMenuItem(b, "help", "topics"));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Window jh = new SimpleHelp("CW-Edit", "??");
				UtilGUI.centre(jh);
				jh.setVisible(true);
			}
		});
		hm.addSeparator();
		hm.add(mi = mkMenuItem(b, "help", "about"));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAboutPane();
			}
		});
		mb.add(hm);					// until setHelpMenu gets implemented!
		// mb.setHelpMenu(hm);		// needed for portability (Motif, etc.).
	}

	/**
	 * Show the "Help About" dialog
	 */
	protected void showAboutPane() {
		JOptionPane.showMessageDialog(parentFrame, "About your program - you need to override\n"+
				"the showAboutPane() method", "Incomplete program!", JOptionPane.ERROR_MESSAGE);
 	}

	/**
	 * Add any complete menuse after File and Edit have been put in.
	 */
	protected void addAdditionalMenus(JMenuBar mb) {
		// TODO Auto-generated method stub
		
	}

	/** Convenience routine to make a Menu */
	public JMenu mkMenu(ResourceBundle b, String name) {
		String menuLabel;
		try { menuLabel = b.getString(name+".label"); }
		catch (MissingResourceException e) { menuLabel=name; }
		return new JMenu(menuLabel);
	}

	/** Convenience routine to make a JMenuItem */
	public JMenuItem mkMenuItem(ResourceBundle b, String menu, String name) {
		String miLabel;
		try { miLabel = b.getString(menu + "." + name + ".label"); }
		catch (MissingResourceException e) { miLabel=name; }
		String key = null;
		try { key = b.getString(menu + "." + name + ".key"); }
		catch (MissingResourceException e) { key=null; }

		if (key == null)
			return new JMenuItem(miLabel);
		else
			return new JMenuItem(miLabel/*, new MenuShortcut(key.charAt(0))*/);
	}

	/** Convenience routine to make a MenuItem */
	public JCheckBoxMenuItem mkCheckboxMenuItem(ResourceBundle b, String menu, String name) {
		String miLabel;
		try { miLabel = b.getString(menu + "." + name + ".label"); }
		catch (MissingResourceException e) { miLabel=name; }

		return new JCheckBoxMenuItem(miLabel);
	}
}
