package regress;

import junit.framework.*;

import com.darwinsys.io.FileIO;

import java.io.*;

public class FileIOTest extends TestCase {
	/** Test File name. This file is created in build.xml */
	public static final String FILENAME="fileiotest.dat";
	/** Test string. */
	public static final String MESSAGE =
		"The quick brown fox jumps over the lazy dog.";

	/** Constructor sets up initial state data file state for testing */
	public FileIOTest() {
		try {
			PrintWriter out = new PrintWriter(new FileWriter(FILENAME));
			out.print(MESSAGE);	// NOT println; FileToString doesn't handle.
			out.close();
		} catch (IOException ex) {
			throw new IllegalStateException("FileIOTest: can't create " + FILENAME);
		}
	}

	public void testReaderToString() {
		try {
			String s = FileIO.readerToString(new FileReader(FILENAME));

			// Make sure that readerToString really reads from the file.
			assertEquals(s, MESSAGE);

			// Make sure that readerToString doesn't append gunk like
			// extraneous nulls.
			assertEquals(s.length(), new File(FILENAME).length());

		} catch (Exception ex) {
			System.err.println(ex);
			throw new IllegalArgumentException(ex.toString());
		}
	}
    public void testCopyFile() {
		String fileName = FILENAME;
		String targetFileName = FILENAME + ".bak";
		try {
			FileIO.copyFile(fileName, targetFileName);
			String s1 = FileIO.readerToString(new FileReader(fileName));
			String s2 = FileIO.readerToString(new FileReader(targetFileName));
			assertEquals(s1, s2);
		} catch (IOException ex) {
			System.err.println(ex);
			throw new IllegalArgumentException(ex.toString());
		}
	}
}
