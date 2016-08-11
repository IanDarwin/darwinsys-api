/*
 * Copyright Notice:
 * This code is copyright by Ian Darwin but is BSD licensed and
 * can thus be used for anything by anybody.
 * If you get rich off it, send me all your money. :-)
 */

package com.darwinsys.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * Save a user data file, as safely as we can.
 * The basic algorithm is:
 <ol>
 <li>We create a temporary file, in the same directory as the input
 file so we can safely rename it. Set it to with deleteOnExit(true);
 <li>Our client writes the user data to this file.  Data format or
 translation errors, if any, will be thrown during this process,
 leaving the user's original file intact. Client closes file.
 <li>We delete the previous backup file, if one exists;
 <li>We rename the user's previous file to filename.bak;
 <li>We rename the temporary file to the save file.
 </ol>
 * This algorithm all but guarantees not to fail for reasons of
 * disk full, permission denied, etc.  Alternate algorithms could
 * be employed that would preserve the original file ownership and
 * permissions (e.g., on POSIX filesystems) but they can not then
 * guarantee not to fail due to disk full conditions.
 * <p>
 * Step 1 is implemented in the constructor.
 * Step 2 you do, by calling getWriter or getOutputStream (not both).
 * Step 3, 4 and 5 are done in finish().
 * <p>
 * Normal usage is thus:
 * <pre>
 * try {
 * 	FileSaver saver = new FileSaver(file);
 * 	final Writer writer = saver.getWriter();
 * 	PrintWriter out = new PrintWriter(writer);
 * 	myWriteOutputFile(out);
 * 	out.close();
 * 	saver.finish();
 * 	System.out.println("Saved OK");
 * } catch (IOException e) {
 * 	System.out.println("Save FAILED");
 * }
 * </pre>
 * <p>
 * Objects of this class may be re-used sequentially (for the
 * same file) but are not thread-safe and should not be shared
 * among different threads.
 * @author Extracted and updated by Ian Darwin from an older
 * application I wrote, prompted by discussion started by Brendon McLean
 * on a private mailing list.
 */
// BEGIN main
// package com.darwinsys.io;
public class FileSaver {

	private enum State {
		/** The state before and after use */
		AVAILABLE,
		/** The state while in use */
		INUSE
	}
	private State state;
	private final File inputFile;
	private final File tmpFile;
	private final File backupFile;
	
	private OutputStream mOutputStream;
	private Writer mWriter;

	public FileSaver(File input) throws IOException {

		// Step 1: Create temp file in right place
		this.inputFile = input;
		tmpFile = new File(inputFile.getAbsolutePath() + ".tmp");
		tmpFile.createNewFile();
		tmpFile.deleteOnExit();
		backupFile = new File(inputFile.getAbsolutePath() + ".bak");
		state = State.AVAILABLE;
	}

	/**
	 * Return a reference to the contained File object, to
	 * promote reuse (File objects are immutable so this
	 * is at least moderately safe). Typical use would be:
	 * <pre>
	 * if (fileSaver == null ||
	 *   !(fileSaver.getFile().equals(file))) {
	 *		fileSaver = new FileSaver(file);
	 * }
	 * </pre>
	 * @return the File object for the file to be saved
	 */
	public File getFile() {
		return inputFile;
	}

	/** Return an output file that the client should use to
	 * write the client's data to.
	 * @return An OutputStream, which should be wrapped in a
	 * 	buffered OutputStream to ensure reasonable performance.
	 * @throws IOException if the temporary file cannot be written
	 */
	public OutputStream getOutputStream() throws IOException {

		if (state != State.AVAILABLE) {
			throw new IllegalStateException("FileSaver not opened");
		}
		mOutputStream = new FileOutputStream(tmpFile);
		state = State.INUSE;
		return mOutputStream;
	}

	/** Return an output file that the client should use to
	 * write the client's data to.
	 * @return A Writer, which should be wrapped in a
	 * 	buffered Writer to ensure reasonable performance.
	 * @throws IOException if the temporary file cannot be written
	 */
	public Writer getWriter() throws IOException {

		if (state != State.AVAILABLE) {
			throw new IllegalStateException("FileSaver not opened");
		}
		mWriter = new FileWriter(tmpFile);
		state = State.INUSE;
		return mWriter;
	}

	/** Close the output file and rename the temp file to the original name.
	 * @throws IOException If anything goes wrong
	 */
	public void finish() throws IOException {

		if (state != State.INUSE) {
			throw new IllegalStateException("FileSaver not in use");
		}
		
		// Ensure both are closed before we try to rename.
		if (mOutputStream != null) {
			mOutputStream.close();
		}
		if (mWriter != null) {
			mWriter.close();
		}

		// Delete the previous backup file if it exists;
		if (backupFile.exists() && !backupFile.delete()) {
			throw new IOException("Failed to delete backup file " + backupFile);
		}

		// Rename the user's previous file to itsName.bak,
		// UNLESS this is a new file ;
		if (inputFile.exists() && !inputFile.renameTo(backupFile)) {
			throw new IOException("Could not rename file to backup file " + backupFile);
		}

		// Rename the temporary file to the save file.
		if (!tmpFile.renameTo(inputFile)) {
			throw new IOException("Could not rename temp file to save file");
		}
		state = State.AVAILABLE;
	}
}
// END main
