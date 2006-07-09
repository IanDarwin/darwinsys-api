package com.darwinsys.installers;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import com.darwinsys.swingui.UtilGUI;

/**
 * The intention is that you put this as Main-class: in a Jar
 * along with everything else - your installer and its data classes 
 * and it unpacks the Jar into a temp directory then starts
 * the "real" installer. This class thus corresponds to the
 * "Setup is preparing the installer" screen.
 */
public class InstallerUnpacker {

	private static final int BLOCK_SIZE = 8092;
	/** As the name implies, the installer MUST be stored under this name */
	public static final String REQUIRED_NAME = "installer.jar";
	
	/**
	 * Start things running...
	 */
	public static void main(String[] args) {
		System.out.println("InstallerUnpacker.main()");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new InstallerUnpacker();
			}
		});
	}
	
	/** Cache of paths we've mkdir()ed. */
	protected SortedSet<String> dirsMade = new TreeSet<String>();
	private boolean warnedMkDir;
	
	InstallerUnpacker() {
		// Get this part of the GUI up quickly...
		JFrame jf = new JFrame("Setup");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel infoLabel = 
			new JLabel("Setup is preparing the installer. Please wait...",
				JLabel.CENTER);
		infoLabel.setPreferredSize(new Dimension(400,100));
		jf.add(infoLabel, BorderLayout.NORTH);
		JLabel status = new JLabel("", JLabel.CENTER);
		jf.add(status, BorderLayout.CENTER);
		JProgressBar progress = new JProgressBar();
		progress.setIndeterminate(true);
		jf.add(progress, BorderLayout.SOUTH);
		jf.setSize(500, 250);
		UtilGUI.center(jf);
		jf.setVisible(true);
		
		// Now read the installer.jar and unpack it.
		try {
			File tmpDir = File.createTempFile("darwinstaller", ".tmp");
			tmpDir.delete();
			tmpDir.mkdir();
			// Might alternately use a JarURLConnection, as in
			// URL url = getResource(REQUIRED_NAME);
			// JarURLConnection jarConnection = (JarURLConnection)url.openConnection();
			// Manifest manifest = jarConnection.getManifest();
			File f = new File(REQUIRED_NAME);
			System.out.println("Starting in on " + REQUIRED_NAME + " created " +
					new Date(f.lastModified()));
			JarFile jarFile = new JarFile(f);
			Manifest m = jarFile.getManifest();
			if (m == null) {
				throw new IOException(REQUIRED_NAME + " file has no Manifest!");
			}
			Attributes installerClassAttributes = m.getMainAttributes();
			String installerClassName = null; 
			if (installerClassAttributes == null) {
				System.err.println("warning: no attributes");
			} else {
				installerClassName = installerClassAttributes.getValue("Installer-class");
				System.out.println(installerClassName);
			}
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				String thisEntryName = entry.getName();
				status.repaint();
				Thread.sleep(500);
				// Don't leave the Manifest in the temp directory
				if (thisEntryName.startsWith("META-INF")) {
					System.out.println("Ignoring " + thisEntryName);
					continue;
				}
				// Don't leave a copy of this file itself there either.
				if (thisEntryName.equals(getClass().getName())) {
					System.out.println("Ignoring " + thisEntryName);
					continue;
				}
				
				// OK, so we're going to process this entry.
				String message = "Processing " + thisEntryName;
				System.out.println(message);
				status.setText(message);
				if (thisEntryName.startsWith("/")) {
					if (!warnedMkDir)
						System.out.println("Ignoring absolute path in " + thisEntryName);
					warnedMkDir = true;
					thisEntryName = thisEntryName.substring(1);
				}
				// if a directory, just return. We mkdir for every file,
				// since some widely-used Zip creators don't put out
				// any directory entries, or put them in the wrong place.
				if (thisEntryName.endsWith("/")) {
					return;
				}				
				// Else must be a file; open the file for output
				// Get the directory part.
				int ix = thisEntryName.lastIndexOf('/');
				if (ix > 0) {
					String dirName = thisEntryName.substring(0, ix);
					if (!dirsMade.contains(dirName)) {
						File d = new File(dirName);
						// If it already exists as a dir, don't do anything
						if (!(d.exists() && d.isDirectory())) {
							// Try to create the directory, warn if it fails
							System.out.println("Creating Directory: " + dirName);
							if (!d.mkdirs()) {
								System.err.println(
								"Warning: unable to mkdir " + dirName);
							}
							dirsMade.add(dirName);
						}
					}
				}
				System.err.println("Creating " + thisEntryName);
				InputStream  is = jarFile.getInputStream(entry);
				FileOutputStream os = new FileOutputStream(thisEntryName);
				byte[] b = new byte[BLOCK_SIZE];
				int n = 0;
				while ((n = is.read(b)) >0)
					os.write(b, 0, n);
				is.close();
				os.close();
				break;
			}
			progress.setIndeterminate(false);
			progress.setValue(100);

			// So we got past unpacking the installer, now run it!
			Class c = Class.forName(installerClassName);
			Class[] args = { String.class };
			Method main = c.getMethod("main", args);
			// Shut down the old GUI
			jf.setVisible(true); jf.dispose(); jf = null;
			// And bring on the new!
			main.invoke(null, (Object[])new String[0]);
			
		} catch (Throwable e) {
			JOptionPane.showMessageDialog(jf, 
					"Error unpacking/starting installer:\n" + e, 
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(0);
		}
		

	}

}
