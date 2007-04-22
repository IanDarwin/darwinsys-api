package com.darwinsys.io;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import junit.framework.TestCase;

public class FileSaverTest extends TestCase {

	/** Test File name. */
	public static final String FILENAME = "fileiotest.dat";

	FileSaver saver;

	/** Test string */
	public static final String MESSAGE =
		"The quick brown fox jumps over the lazy dog.";

	/** Set up initial state data file state for testing */
	@Override
	public void setUp() {
		try {
			// Create file in "." with known name and contents
			FileIO.stringToFile(MESSAGE, FILENAME);
			final File file = new File(FILENAME);
			file.deleteOnExit();
			// Create FileSaver to save it.
			saver = new FileSaver(file);
		} catch (IOException ex) {
			throw new IllegalStateException("FileIOTest: can't create " + FILENAME);
		}
	}

	public void testOne() throws IOException {
		final Writer writer = saver.getWriter();
		PrintWriter out = new PrintWriter(writer);
		out.print(MESSAGE);
		out.close();
		saver.finish();
	}

}
