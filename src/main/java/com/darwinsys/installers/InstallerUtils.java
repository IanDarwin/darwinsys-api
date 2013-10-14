package com.darwinsys.installers;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import com.darwinsys.swingui.UtilGUI;

/**
 * Misc. utilities for Installer-type applications.
 */
public class InstallerUtils {
	
	/**
	 * Display a License file and ask the user to Accept it
	 * @param parent
	 * @param licensePath A valid path to the License file to be displayed.
	 * @return True if the user accepts the license, false if not.
	 * @throws IOException If anything goes wrong.
	 */
	public static boolean acceptLicense(final JFrame parent, 
			final String licensePath) throws IOException {
		
		System.out.println("licensePath = " + licensePath);
		File f = new File(licensePath);
		if (!f.canRead()) {
			throw new IOException("Cannot read file " + licensePath);
		}
		JEditorPane jta = new JEditorPane(new URL("file:" + f.getAbsolutePath()));

		jta.setEditable(false);
		jta.setCaretPosition(0);
		
		JOptionPane pane = new JOptionPane(
				new JScrollPane(jta),
				JOptionPane.QUESTION_MESSAGE,
				JOptionPane.YES_NO_OPTION);
		JDialog dialog = pane.createDialog(parent, "Accept License");
		dialog.setSize(new Dimension(600, 500));
		UtilGUI.centre(dialog);
		dialog.setVisible(true);
		Object selectedValue = pane.getValue();
		if (selectedValue == null)
			return false;
		if (selectedValue instanceof Integer)
			// "Yes" is the 0th option...
			return 0 == ((Integer)selectedValue).intValue();
			
		return false;
	}
}
