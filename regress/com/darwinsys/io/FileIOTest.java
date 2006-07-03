package com.darwinsys.io;

import junit.framework.*;

import com.darwinsys.io.FileIO;

import java.io.*;

public class FileIOTest extends TestCase {
	/** Test File name. */
	public static final String FILENAME = "fileiotest.dat";
	/** A good place to work */
	String tmpDirPath = System.getProperty("java.io.tmpdir");
	/** Test string. */
	public static final String MESSAGE =
		"The quick brown fox jumps over the lazy dog.";

	/** Set up initial state data file state for testing */
	@Override
	public void setUp() {
		try {
			PrintWriter out = new PrintWriter(new FileWriter(
					tmpDirPath + "/" + FILENAME));
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
			assertEquals(MESSAGE, s);

			// Make sure that readerToString doesn't append gunk like
			// extraneous nulls.
			assertEquals(s.length(), new File(FILENAME).length());

		} catch (Exception ex) {
			System.err.println(ex);
			throw new IllegalArgumentException(ex.toString());
		}
	}
	
    public void testCopyFileByName() {
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
    
    public void testCopyFileByFile() {
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
    
    public void testCopyRecursively() throws IOException {
    	
    	System.out.println("java.io.tmpdir = " + tmpDirPath);
		File tmpDir = new File(tmpDirPath);
    	File newDir = new File(tmpDir, "testFrom");
    	File foo = null, bar = null;
    	newDir.delete();
    	File newDestDir = null;
    	File newFoo = null, newBar = null;
    	try {
    	newDir.mkdir();
    	foo = new File(newDir, "/foo"); foo.createNewFile();
    	bar = new File(newDir, "/bar"); bar.createNewFile();
    	newDestDir = new File(tmpDir, "testTo");
    	newDestDir.mkdir();
    	newFoo = new File(newDestDir, "foo");
    	assertFalse(newFoo.exists());
    	newBar = new File(newDestDir, "bar");
		assertFalse(newBar.exists());
    	assertEquals(0, newDestDir.listFiles().length);
    	FileIO.copyRecursively(newDir, newDestDir);
    	assertTrue(newFoo.exists());
    	assertTrue(newBar.exists());
    	assertEquals(2, newDestDir.listFiles().length);
    	} finally {
    		// order matters
    		if (bar != null) bar.delete();
    		if (foo != null) foo.delete();
    		if (newDir != null) newDir.delete();
    		if (newFoo != null) newFoo.delete();
    		if (newBar != null) newBar.delete();
    		if (newDestDir != null) newDestDir.delete();
    	}
    }
}
