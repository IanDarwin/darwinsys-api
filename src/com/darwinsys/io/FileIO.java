package com.darwinsys.util;

import java.io.*;

/**
 * Some simple file I-O primitives reimplemented in Java.
 * All methods are static, since there is no state.
 * @version $Id$
 */
public class FileIO {

    /** Copy a file from one filename to another */
    public static void copyFile(String inName, String outName)
	throws FileNotFoundException, IOException {
		BufferedInputStream is = 
			new BufferedInputStream(new FileInputStream(inName));
		BufferedOutputStream os = 
			new BufferedOutputStream(new FileOutputStream(outName));
		copyFile(is, os, true);
	}

	/** Copy a file from an opened InputStream to opened OutputStream */
	public static void copyFile(InputStream is, OutputStream os, boolean close) 
	throws IOException {
		int b;				// the byte read from the file
		while ((b = is.read()) != -1) {
			os.write(b);
		}
		is.close();
		if (close)
			os.close();
    }

	/** Copy a file from an opened Reader to opened Writer */
	public static void copyFile(Reader is, Writer os, boolean close) 
	throws IOException {
		int b;				// the byte read from the file
		while ((b = is.read()) != -1) {
			os.write(b);
		}
		is.close();
		if (close)
			os.close();
    }

	/** Copy a file from a filename to a PrintWriter. */
	public static void copyFile(String inName, PrintWriter pw, boolean close) 
	throws FileNotFoundException, IOException {
		BufferedReader is = new BufferedReader(new FileReader(inName));
		copyFile(is, pw, close);
	}

	/** Open a file and read the first line from it. */
	public static String readLine(String inName)
	throws FileNotFoundException, IOException {
		BufferedReader is = new BufferedReader(new FileReader(inName));
		String line = null;
		line = is.readLine();
		is.close();
		return line;
	}
}
