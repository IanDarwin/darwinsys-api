package com.darwinsys.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class FileSaverTest {

	/** Test File name. */
	public static final String FILENAME = "fileSaverTest.dat";

	@TempDir
	static File workdir;
	
	static FileSaver saver;

	/** Test string */
	public static final String MESSAGE =
		"The quick brown fox jumps over the lazy dog.";
	
	private File file;

	@BeforeEach
	void setUpOne() throws IOException {
		// Create file in tempdir with known name and contents
		// It will get overwritten by the FileSaver
		workdir.mkdirs();
		file = new File(workdir, FILENAME);
		Files.writeString(file.toPath(), MESSAGE);
		
		// Create FileSaver to save it.
		saver = new FileSaver(file.toPath());
		
	}

	/** Test that the overwritten file contains something reasonable,
	 * and that nothing gets thrown in normal processing.
	 * @throws IOException
	 */
	@Test
	void one() throws IOException {
		final Writer writer = saver.getWriter();
		PrintWriter out = new PrintWriter(writer);
		out.print(MESSAGE);
		out.close();
		saver.finish();
		final String finalString = FileIO.readerToString(new FileReader(file.getAbsoluteFile()));
		assertEquals(MESSAGE, finalString, "Reading string back");
	}

	/**
	 * Test state forwarding
	 * @throws IOException
	 */
	@Test
	void failures() throws IOException {
		saver.getWriter();
		try {
			saver.getOutputStream();
			fail("Allowed getWriter AND getOutputStream");
		} catch (IllegalStateException e) {
			// Nothing to do
		}
		saver.finish();
		saver.getOutputStream(); // should work, since finish() survived
	}
}
