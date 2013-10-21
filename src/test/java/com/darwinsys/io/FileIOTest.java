package com.darwinsys.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import junit.framework.TestCase;

import com.darwinsys.util.Debug;

public class FileIOTest extends TestCase {
	/** Test File name. */
	public static final String FILENAME = "fileiotest.dat";

	/** A good place to work; this gets set to a random tmp directory;
	 * place everything under this.
	 */
	File tmpDir;

	/** Test string; does not get newline appended when written. */
	public static final String MESSAGE =
		"The quick brown fox jumps over the lazy dog.";

	/** Set up initial state data file state for testing */
	@Override
	public void setUp() {
		try {
			tmpDir = File.createTempFile("test", "dir");
			tmpDir.delete();
			tmpDir.mkdir();
		} catch (IOException ex) {
			throw new IllegalStateException("FileIOTest: can't create " + FILENAME);
		}
	}
	
	@Override
	public void tearDown() throws Exception {
		File file = new File(tmpDir, FILENAME);
		if (file.exists()) {
			file.delete();
		}
		String backupFileName = tmpDir + "/" + FILENAME + ".bak";
		new File(backupFileName).delete();
		if (tmpDir != null & tmpDir.exists()) {
			tmpDir.delete();
		}
	}

	private void makeFileIOTestDat() throws IOException {
		PrintWriter out = new PrintWriter(new FileWriter(
				tmpDir + "/" + FILENAME));
		out.print(MESSAGE);	// NOT println; FileToString doesn't handle.
		out.close();
	}

	public void testReaderToString() {
		try {
			makeFileIOTestDat();
			String s = FileIO.readerToString(new FileReader(tmpDir + "/" + FILENAME));

			// Make sure that readerToString really reads from the file.
			assertEquals(MESSAGE, s);

			// Make sure that readerToString doesn't append gunk like
			// extraneous nulls.
			assertEquals(s.length(), new File(tmpDir + "/" + FILENAME).length());

		} catch (Exception ex) {
			System.err.println(ex);
			throw new IllegalArgumentException(ex.toString());
		}
	}
	
    public void testCopyFileByName() {
		String fileName = tmpDir + "/" + FILENAME;
		String targetFileName = tmpDir + "/" + FILENAME + ".bak";
		try {
			makeFileIOTestDat();
			FileIO.copyFile(fileName, 
					targetFileName);
			String s1 = FileIO.readerToString(new FileReader(fileName));
			String s2 = FileIO.readerToString(new FileReader(targetFileName));
			assertEquals(s1, s2);
		} catch (IOException ex) {
			System.err.println(ex);
			throw new RuntimeException(ex.toString());
		}
	}
    
    public void testCopyFileByFile() {
		String fileName = tmpDir + "/" + FILENAME;
		String targetFileName = tmpDir + "/" + FILENAME + ".bak";
		try {
			makeFileIOTestDat();
			FileIO.copyFile(fileName, targetFileName);
			String s1 = FileIO.readerToString(new FileReader(fileName));
			String s2 = FileIO.readerToString(new FileReader(targetFileName));
			assertEquals(s1, s2);
		} catch (IOException ex) {
			System.err.println(ex);
			throw new RuntimeException(ex.toString());
		}
	}
    
    public void testCopyRecursivelyFiles() throws IOException {
    	
    	Debug.println("fileio", "my tmpdir = " + tmpDir);
      
    	File thisTestWorkDir = new File(tmpDir, "testFrom");
    	File foo = null, bar = null;
    	File targetCopyDir = null;
    	File newFoo = null, newBar = null, bleah = null;
      	try {
      		thisTestWorkDir.delete();
      		thisTestWorkDir.mkdir();
      		foo = new File(thisTestWorkDir, "foo");
			foo.createNewFile();
			bleah = new File(thisTestWorkDir, "bleah");
			bleah.mkdir();
			bar = new File(thisTestWorkDir, "bleah/bar");
			bar.createNewFile();

      		targetCopyDir = new File(tmpDir, "testTo");
      		targetCopyDir.mkdir();
      		// Should be empty as yet.
      		assertEquals(0, targetCopyDir.listFiles().length);
      		
      		// Set up File objects for the things we expect the copy 
      		// routing to create; be sure they dont exist yet.
      		newFoo = new File(targetCopyDir, "foo");
      		assertFalse(newFoo.exists());
      		newBar = new File(targetCopyDir, "bleah/bar");
      		assertFalse(newBar.exists());
      		
      		FileIO.copyRecursively(thisTestWorkDir, targetCopyDir);

      		assertTrue(newFoo.exists());
      		assertTrue(bleah.exists());
      		assertTrue(newBar.exists());

      		// Should contain source files foo and bar, and the directory.
      		assertEquals(2, targetCopyDir.listFiles().length);
    	} finally {
    		// order matters
    		if (bar != null) bar.delete();
    		if (bleah != null) bleah.delete();
    		if (foo != null) foo.delete();
    		if (thisTestWorkDir != null) thisTestWorkDir.delete();
    		if (newFoo != null) newFoo.delete();
    		if (newBar != null) newBar.delete();
    		if (targetCopyDir != null) targetCopyDir.delete();
    	}
    }
    
    public void testDeleteRecursively() throws Exception {
    	File f1 = new File(tmpDir, "file1");
    	f1.createNewFile();
    	File d1 = new File(tmpDir, "happyDir1");
    	d1.mkdir();
    	File d1f1 = new File(tmpDir, "d1f1");
    	d1f1.createNewFile();
    	
    	assertTrue(d1f1.exists());
    	
    	FileIO.deleteRecursively(tmpDir);
    	
    	assertFalse(d1f1.exists());
    	assertFalse(d1.exists());
    	assertFalse(f1.exists());
    	assertFalse(tmpDir.exists());
    }
    	
    public void testCopyRecursivelyFromJar() throws IOException {
        	
    	Debug.println("fileio", " my tmpdir = " + tmpDir);
    	File targetFoo = null, targetBar = null, 
    	newDestDir = null;
    	File jarFile = new File(tmpDir, "test.jar");
    	try {

    		JarOutputStream jf = 
    			new JarOutputStream(new FileOutputStream(jarFile));
    		ZipEntry ze = new ZipEntry("foo");
    		jf.putNextEntry(ze);
    		jf.write("Hello\n".getBytes());
    		ZipEntry zBar = new ZipEntry("bar");
    		jf.putNextEntry(zBar);
    		jf.write("Hello II\n".getBytes());
    		jf.close();

    		newDestDir = new File(tmpDir, "testTo");
    		newDestDir.mkdir();

    		targetFoo =  new File(newDestDir, "foo");
    		assertFalse(targetFoo.exists());
    		targetBar = new File(newDestDir, "bar");
    		assertFalse(targetBar.exists());
    		// Should be empty at this point
    		assertEquals(0, newDestDir.listFiles().length);

    		FileIO.copyRecursively(new JarFile(jarFile), 
    				new JarEntry("/"), newDestDir);

    		assertTrue(targetFoo.exists());
    		assertEquals(6, targetFoo.length());
    		assertTrue(targetBar.exists());
    		assertEquals(9, targetBar.length());
    		assertEquals(2, newDestDir.listFiles().length);
    	} finally {
    		if (jarFile != null) jarFile.delete();
    		if (targetFoo != null) targetFoo.delete();
    		if (targetBar != null) targetBar.delete();
    		// Must be after we delete the files in it.
    		if (newDestDir != null) newDestDir.delete();
    	}
    }
}
