package com.darwinsys.util;

import java.io.*;

/**
 * Some simple file I-O primitives reimplemented in Java
 * @version
 */
public class FileIO {

    /** Copy a file from one filename to another */
    public void copyFile(String inName, String outName)
	throws FileNotFoundException, IOException {
		BufferedInputStream is = 
			new BufferedInputStream(new FileInputStream(inName));
		BufferedOutputStream os = 
			new BufferedOutputStream(new FileOutputStream(outName));
		copyFile(is, os);
	}

	/** Copy a file from an opened InputStream to opened OutputStream */
	public void copyFile(InputStream is, OutputStream os) 
	throws IOException {
		int b;				// the byte read from the file
		while ((b = is.read()) != -1) {
			os.write(b);
		}
		is.close();
		os.close();
    }

	/** Copy a file from an opened Reader to opened Writer */
	public void copyFile(Reader is, Writer os) 
	throws IOException {
		int b;				// the byte read from the file
		while ((b = is.read()) != -1) {
			os.write(b);
		}
		is.close();
		os.close();
    }

	/** Copy a file from a filename to a PrintWriter. */
	public void copyFile(String inName, PrintWriter pw) 
	throws FileNotFoundException, IOException {
		BufferedReader is = new BufferedReader(new FileReader(inName));
		copyFile(is, pw);
	}

	/** Open a file and read the first line from it. */
	public String readLine(String inName)
	throws FileNotFoundException, IOException {
		BufferedReader is = new BufferedReader(new FileReader(inName));
		String line = null;
		line = is.readLine();
		is.close();
		return line;
	}
}
