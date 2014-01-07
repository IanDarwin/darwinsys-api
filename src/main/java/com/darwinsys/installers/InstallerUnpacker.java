package com.darwinsys.installers;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import com.darwinsys.io.FileIO;
import com.darwinsys.swingui.UtilGUI;

/**
 * The intention is that you put this as Main-class: in a Jar
 * along with everything else - your installer and its data classes
 * and it unpacks the Jar into a temp directory then starts
 * the "real" installer. This class thus corresponds to the
 * "Setup is preparing the installer" screen.
 */
public class InstallerUnpacker implements Runnable {

	// CONSTANTS

	static final int BLOCK_SIZE = 8092;
	/** As the name implies, the installer MUST be stored under this name */
	public static final String REQUIRED_NAME = "installer.jar";

	// GUI componenets
	JFrame jf;
	JLabel status;
	JProgressBar progress;

	/**
	 * Start things running...
	 */
	public static void main(String[] args) {
		System.out.println("InstallerUnpacker.main()");

		InstallerUnpacker installerUnpacker;

		installerUnpacker = new InstallerUnpacker(REQUIRED_NAME);

		installerUnpacker.run();
	}

	/** Cache of paths we've mkdir()ed. */
	protected SortedSet<String> dirsMade = new TreeSet<String>();
	private boolean warnedMkDir;
	private String fileName;

	/**
	 * Construct the GUI.
	 */
	InstallerUnpacker(String fileName) {
		this.fileName = fileName;
		// Get this part of the GUI up quickly...
		jf = new JFrame("Setup");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel infoLabel =
			new JLabel("Setup is unpacking the installer. Please wait...",
				JLabel.CENTER);
		infoLabel.setPreferredSize(new Dimension(400,100));
		jf.add(infoLabel, BorderLayout.NORTH);
		status = new JLabel("", JLabel.CENTER);
		jf.add(status, BorderLayout.CENTER);
		progress = new JProgressBar();
		progress.setIndeterminate(true);
		jf.add(progress, BorderLayout.SOUTH);
		jf.setSize(500, 250);
		UtilGUI.center(jf);
		jf.setVisible(true);
	}

	/** Do the work of unpacking the installer, and launching it.
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		// Now read the installer.jar and unpack it.
		File tmpDir = null;
		try {
			tmpDir = File.createTempFile("darwinstaller", ".tmp");
			tmpDir.delete();
			tmpDir.mkdir();
			// Might alternately use a JarURLConnection, as in
			// URL url = getResource(REQUIRED_NAME);
			// JarURLConnection jarConnection = (JarURLConnection)url.openConnection();
			// Manifest manifest = jarConnection.getManifest();
			File f = new File(fileName);
			if (!f.exists() || !f.canRead()) {
				throw new IOException("Can't read installer file " + fileName);
			}
			System.out.println("Starting on " + fileName + " created " +
					new Date(f.lastModified()));
			JarFile jarFile = new JarFile(f);
			Manifest m = jarFile.getManifest();
			if (m == null) {
				throw new IOException(fileName + " file has no Manifest!");
			}
			Attributes installerClassAttributes = m.getMainAttributes();
			String installerClassName = null;
			if (installerClassAttributes == null) {
				System.err.println("warning: no attributes");
			} else {
				installerClassName = installerClassAttributes.getValue("Installer-class");
				System.out.println("Installer class name is " + installerClassName);
			}
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				String thisEntryName = entry.getName();

				// Don't leave the Manifest in the temp directory
				if (thisEntryName.startsWith("META-INF") ||
					thisEntryName.startsWith("MANIFEST")) {
					System.out.println("Ignoring " + thisEntryName);
					continue;
				}
				// Don't leave a copy of this file itself (or inner classes) either.
				if (thisEntryName.startsWith(getClass().getName())) {
					System.out.println("Ignoring " + thisEntryName);
					continue;
				}
				// if a directory, just skip it.. We mkdir for every file,
				// since some widely-used Zip creators don't put out
				// any directory entries, or put them in the wrong place.
				if (thisEntryName.endsWith("/")) {
					continue;
				}

				// OK, so we're going to process this entry.
				String message = "Processing " + thisEntryName;
				// System.out.println(message);
				Thread.sleep(50);	// Brief pause to let user see it! :-)
				status.setText(message);
				if (thisEntryName.startsWith("/")) {
					if (!warnedMkDir)
						System.out.println("Ignoring absolute path in " + thisEntryName);
					warnedMkDir = true;
					thisEntryName = thisEntryName.substring(1);
				}

				// Else must be a file; open the file for output. But first,
				// Get the directory part, mkdir it if not already done.
				int ix = thisEntryName.lastIndexOf('/');
				if (ix > 0) {
					String dirName = thisEntryName.substring(0, ix);
					if (!dirsMade.contains(dirName)) {
						File d = new File(tmpDir, dirName);
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
				System.out.println("Creating " + thisEntryName);
				InputStream  is = jarFile.getInputStream(entry);
				FileOutputStream os =
					new FileOutputStream(new File(tmpDir, thisEntryName));
				byte[] b = new byte[BLOCK_SIZE];
				int n = 0;
				while ((n = is.read(b)) >0)
					os.write(b, 0, n);
				is.close();
				os.close();
			}

			// All done. Stop the progress bar.
			progress.setIndeterminate(false);
			progress.setValue(100);
			status.setText("Unpacking completed.");

			// Now run the actual installer...

			// Need to run it in the created temp directory.
			// Since Java is deficient in not providing a chdir
			// mechanism, we have to do this with a Runtime.exec,
			// Hope this '"java.home" + /bin/java' trick is portable!
			String[] argv = {
					System.getProperty("java.home") + "/bin/java",
					installerClassName,
					"-l",
					tmpDir + "/" + "darwinstall.log"
			};
			Process p = Runtime.getRuntime().exec(argv, null, tmpDir);

			// Leave unpacker GUI up for a few seconds to overlap,
			// then close down the unpacker gui
			status.setText("Starting the installer...");
			Thread.sleep(8000);
			jf.setVisible(false); jf.dispose(); jf = null;

			p.waitFor();	// Wait for the installer to finish.

		} catch (Exception e) {
			JOptionPane.showMessageDialog(jf,
					"Error unpacking/starting installer:\n" + e,
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(1);
		} finally {
			// Now that installer is done with the temp dirctory, delete it.
			try {
				FileIO.deleteRecursively(tmpDir);
			} catch (IOException e) {
				System.err.println("Part of tmp dir cleanup failed:\n" + e);
			}

			System.exit(0);
		}
	}
}
