package com.darwinsys.installers;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

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
		//JTextArea jta = new JTextArea(30, 60);
		JEditorPane jta = new JEditorPane(new URL("file:" + f.getAbsolutePath()));
//		BufferedReader in = null;
//		try {
//		in = new BufferedReader(new FileReader(f));
//		String line;
//		while ((line = in.readLine()) != null) {
//			jta.getDocument().append(line);
//			jta.append("\n");
//		}
//		finally { if (in != null) in.close(); }
		jta.setEditable(false);
		jta.setCaretPosition(0);

		int ret = JOptionPane.showConfirmDialog(parent, 
				new JScrollPane(jta), "Accept License", 
				JOptionPane.YES_NO_OPTION);
		return ret == 0;	// "Yes" is the 0th option...
	}
}
