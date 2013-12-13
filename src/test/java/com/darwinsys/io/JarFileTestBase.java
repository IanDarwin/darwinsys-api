package com.darwinsys.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import org.junit.BeforeClass;

/** Creates a Jar file from one of our classes, for use
 * in testing Jar File utilities.
 * N.B. The .class file is in src/main/resources WITHOUT the .class extension
 */
public class JarFileTestBase {

	/** The Java class name of the test subject */
	protected final static String TESTCLASS = "com.darwinsys.util.IndexList";
	/** The physical name of the test subject */
	static final String clazzFile = TESTCLASS.replace('.','/')+".class";

	/** The name of the tempfile containing the Jar for this run */
	protected static String jarFileName;

	@BeforeClass
	public static void createJar() throws Exception {
		File f = File.createTempFile("testjar", ".jar");
		f.deleteOnExit();
		jarFileName = f.getAbsolutePath();
		System.out.println("JarFileTestBase.createJar(): " + jarFileName);
		try (JarOutputStream jf =
				new JarOutputStream(new FileOutputStream(f.getAbsolutePath()))) {
			jf.putNextEntry(new ZipEntry("com/"));
			jf.putNextEntry(new ZipEntry("com/darwinsys/"));
			jf.putNextEntry(new ZipEntry("com/darwinsys/util/"));
			jf.putNextEntry(new ZipEntry(clazzFile));
			InputStream is =
					JarFileTestBase.class.getResourceAsStream("/" + TESTCLASS);
			if (is == null) {
				throw new IOException("Failed to read class file as stream");
			}
			FileIO.copyFile(is, jf, true);
		}
	}
}
