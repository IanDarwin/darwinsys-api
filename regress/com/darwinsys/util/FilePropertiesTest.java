package regress;

import java.io.*;
import java.util.*;

import junit.framework.TestCase;

import com.darwinsys.util.FileProperties;

public class FilePropertiesTest extends TestCase {
	private static final String TEST_FILE_NAME = "erewhon";
	Properties p;
	File erewhon = new File(TEST_FILE_NAME);
	/** Set up for junit test.
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		erewhon.delete();
		// no such file should exist; should not throw IOException
		p = new FileProperties(TEST_FILE_NAME); 
		
	}
	
    public void testSet() throws Exception {
		System.out.println("Properties p should be empty:");
		assertEquals(p.size(), 0);
		p.list(System.out);
		p.setProperty("foo", "bar");
		((FileProperties)p).save();

		p = new FileProperties(TEST_FILE_NAME);
		System.out.println("This properties should be have foo=bar:");
		p.list(System.out);

		new File("no such file").delete();
	}
}
