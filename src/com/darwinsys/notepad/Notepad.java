package com.darwinsys.notepad;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.darwinsys.swingui.UtilGUI;

/**
 * Simple text editor, making Swing do the work.
 * Is it MVC? Well, the JTextArea is the Model, the
 * Actions are Controllers, and the rest is View.
 */
@SuppressWarnings("serial")
public class Notepad {

	private JFrame theFrame;

	private JTextArea ta;

	private JFileChooser chooser;

	private String fileName;

	private static List<Notepad> windows = new ArrayList<Notepad>();

	private boolean isStandalone = true;

	private JMenu fm, em, hm;

	public Notepad() {
		this(false);
	}

	public Notepad(boolean isStandalone) {

		this.isStandalone = isStandalone;
		// Allow override for testing
		String prop;
		if ((prop = System.getProperty("STANDALONE")) != null) {
			this.isStandalone = Boolean.parseBoolean(prop);
			System.out.println(isStandalone);
		}

		theFrame = new JFrame();
		theFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeThisWindow();
			}
		});

		ta = new JTextArea(30,70);
		theFrame.setContentPane(new JScrollPane(ta));
		theFrame.pack();

		createMenus();

		UtilGUI.centre(theFrame);
		Point loc = theFrame.getLocation();
		synchronized(windows) {
			int windowsCreated = windows.size();
			loc.x += windowsCreated * 20;
			loc.y += windowsCreated * 20;
			windows.add(this);
		}
		theFrame.setLocation(loc);
		theFrame.setVisible(true);
	}

	private void closeThisWindow() {
		if (!okToClose()) {
			return;
		}
		theFrame.setVisible(false);
		theFrame.dispose();
		synchronized(windows) {
			windows.remove(this);
			if (windows.size() == 0) {
				if (isStandalone) {
					System.exit(0);
				} else {
					// imbedded - nothing to do.
				}
			}
		}
	}

	Action openAction = new OpenAction();
	class OpenAction extends AbstractAction {
		OpenAction() {
			super("Open");
		}

		public void actionPerformed(ActionEvent e) {
			if (chooser == null) {
				chooser = new JFileChooser();
			}
			int returnVal = chooser.showOpenDialog(theFrame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				try {
					doLoad(file.getAbsolutePath());
				} catch (IOException e1) {
					error("Can't open file", e1);
				}
			}
		}
	};

	Action newAction = new NewAction();
	class NewAction extends AbstractAction {
		NewAction() {
			super("New");
		}
		public void actionPerformed(ActionEvent e) {
			new Notepad();
		}
	};

	private boolean doingSaveAs; // shared between SaveAction and SaveAsAction

	Action saveAction = new SaveAction();
	class SaveAction extends AbstractAction {
		SaveAction() {
			super("Save");
		}

		/*
		 * This code is used both by Save and SaveAs, differentiated by doingSaveAs
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			try {
				if (fileName != null && !doingSaveAs) {
					doSave(fileName);
					return;
				}
				if (chooser == null) {
					chooser = new JFileChooser();
				}
				int returnVal = chooser.showOpenDialog(theFrame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					if (file.exists() && doingSaveAs) {
						int ret = JOptionPane.showConfirmDialog(theFrame,
								"File already exists, overwrite?", "File Exists",
								JOptionPane.YES_NO_OPTION);
						System.err.println(ret);
						if (ret != 0);	// "Yes" is the 0th option...
							return;
					}
					doSave(file);
				}
			} catch (IOException e1) {
				error("Can't save file", e1);
			}
		}
	};

	Action saveAsAction = new SaveAsAction();
	class SaveAsAction extends AbstractAction {
		SaveAsAction() {
			super("Save As");
		}

		public void actionPerformed(ActionEvent e) {
			doingSaveAs = true;
			saveAction.actionPerformed(e);
			doingSaveAs = false;
		}
	};

	Action closeAction = new CloseAction();
	class CloseAction extends AbstractAction {
		CloseAction() {
			super("Close");
		}
		public void actionPerformed(ActionEvent e) {
			closeThisWindow();
		}
	};
	Action printAction = new PrintAction();
	class PrintAction extends AbstractAction {
		PrintAction() {
			super("Print");
		}
		public void actionPerformed(ActionEvent e) {
			try {
				doPrint();
			} catch (IOException e1) {
				error("Print failure", e1);
			} catch (PrintException e1) {
				error("Print failure", e1);
			}
		}
	};

	Action cutAction = new CutAction();
	class CutAction extends AbstractAction {
		CutAction() {
			super("Cut");
		}
		public void actionPerformed(ActionEvent e) {
			ta.cut();
		}
	}

	Action copyAction = new CopyAction();
	class CopyAction extends AbstractAction {
		CopyAction() {
			super("Copy");
		}
		public void actionPerformed(ActionEvent e) {
			ta.copy();
		}
	}

	Action pasteAction = new PasteAction();
	class PasteAction extends AbstractAction {
		PasteAction() {
			super("Paste");
		}
		public void actionPerformed(ActionEvent e) {
			ta.paste();
		}
	}

	private void error(String message, Exception e) {
		JOptionPane.showMessageDialog(theFrame, message + "\n" + e);
		e.printStackTrace();
	}

	Action exitAction = new ExitAction();
	class ExitAction extends AbstractAction {
		ExitAction() {
			super("Exit");
		}
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	};

	Action helpAboutAction = new HelpAboutAction();
	class HelpAboutAction extends AbstractAction {
		HelpAboutAction() {
			super("About");
		}
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(theFrame,
				"<html><font color='red'>Notepad 0.0</font> A simple text editor");
		}
	};

	private void createMenus() {
		JMenuBar mb = new JMenuBar();
		/** File, Help */

		JMenuItem mi;

		theFrame.setJMenuBar(mb);

		// The File Menu...
		fm = new JMenu("File");
		fm.add(openAction);
		fm.add(saveAction);
		fm.add(saveAsAction);
		fm.add(closeAction);
		fm.add(newAction);
		fm.addSeparator();
		fm.add(printAction);
		fm.addSeparator();
		fm.add(exitAction);
		if (!isStandalone) {
			exitAction.setEnabled(false);
		}
		mb.add(fm);

		// The Edit Menu...
		em = new JMenu("Edit");
		em.add(cutAction);
		em.add(copyAction);
		em.add(pasteAction);
		em.addSeparator();
		mi = new JMenuItem("Search");
		mi.setEnabled(false);
		em.add(mi);
		mi = new JMenuItem("Replace");
		mi.setEnabled(false);
		em.add(mi);
		em.addSeparator();
		JMenuItem insertMenu = new JMenu("Insert");
		em.add(insertMenu);
		JMenuItem menuItem = new JMenuItem("Date");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ta.insert(new Date().toString(), ta.getCaretPosition());
			}
		});
		insertMenu.add(menuItem);
		JMenuItem menuItem2 = new JMenuItem(".signature");
		menuItem2.setEnabled(false);
		insertMenu.add(menuItem2);
		JMenuItem menuItem3 = new JMenuItem("File...");
		menuItem3.setEnabled(false);
		insertMenu.add(menuItem3);
		mb.add(em);

		// The Help Menu...
		hm = new JMenu("Help");
		hm.add(helpAboutAction);
		if (!isStandalone) {
			helpAboutAction.setEnabled(false);
		}
		mb.add(hm);
	}

	private boolean okToClose() {
		// TODO if unsaved changes
		// confirm via JOptionPane
		return true;
	}

	/** Print a file by name
	 * @throws IOException
	 * @throws PrintException
	 */
	public final void doPrint() throws IOException, PrintException {

		System.out.println("Printing ");
		DocFlavor flavor = DocFlavor.CHAR_ARRAY.TEXT_PLAIN;
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		//aset.add(MediaSizeName.NA_LETTER);
		PrintService[] pservices = PrintServiceLookup.lookupPrintServices(
				flavor, aset);
		int i;
		switch(pservices.length) {
		case 0:
			JOptionPane.showMessageDialog(theFrame,
					"Error: No PrintService Found", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		case 1:
			i = 1;
			break;
		default:
			i = JOptionPane.showOptionDialog(theFrame,
					"Pick a printer", "Choice",
					JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE,
					null, pservices, pservices[0]);
			break;
		}
		if (i < 0) {
			return;
		}
		DocPrintJob pj = pservices[i].createPrintJob();
		Doc doc = new MyDocument(flavor);

		pj.print(doc, aset);
	}

	/**
	 * Simple holder for document flavor.
	 */
	final class MyDocument implements Doc {

		private DocFlavor flavor;
		public MyDocument(DocFlavor flavor) {
			this.flavor = flavor;
		}

		public DocFlavor getDocFlavor() {
			return flavor;
		}

		public Object getPrintData() throws IOException {
			return ta.getText();
		}

		public DocAttributeSet getAttributes() {
			return null;
		}

		public Reader getReaderForText() throws IOException {
			return new StringReader(ta.getText());
		}

		public InputStream getStreamForBytes() throws IOException {
			return null;
		}
	}

	public final void doLoad(File file) throws IOException {
		doLoad(file.getAbsolutePath());
	}

	public final void doLoad(String fileName) throws IOException {
		BufferedReader is = null;
		try {
			is = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(theFrame,
				String.format("File %s not found, treating as New File", fileName),
				"Not found", JOptionPane.WARNING_MESSAGE);
			setFileName(fileName);
			return;
		}
		String line;
		while ((line = is.readLine()) != null) {
			ta.append(line);
			ta.append("\n");
		}
		ta.setCaretPosition(0);
		is.close();
		setFileName(fileName);
	}

	/**
	 * Thin wrapper for doSave(File).
	 * @param fileName
	 * @throws IOException
	 */
	public final void doSave(String fileName) throws IOException {
		doSave(new File(fileName));
	}

	/**
	 * Save the file to disk, in such a way as to map the UNIX
	 * line-endings used inside JTextArea to the correct
	 * platform-specific line endings as generated by println().
	 * @param file
	 * @throws IOException
	 */
	public final void doSave(File file) throws IOException {
		PrintWriter w = new PrintWriter(new FileWriter(file));
		BufferedReader is = new BufferedReader(
			new StringReader(ta.getText()));
		String line;
		while ((line = is.readLine()) != null) {
			w.println(line);
		}
		w.close();
		setFileName(file.getAbsolutePath());
	}

	/**
	 * Set the fileName field and the title.
	 * @param fileName The new file name.
	 */
	private void setFileName(String fileName) {
		this.fileName = fileName;
		theFrame.setTitle(fileName);
	}

	public JMenu getEditMenu() {
		return em;
	}

	public JMenu getFileMenu() {
		return fm;
	}

	public JMenu getHelpMenu() {
		return hm;
	}
}
