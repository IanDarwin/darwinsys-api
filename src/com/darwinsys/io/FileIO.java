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
	public copyFile(InputStream is, OutputStream os) 
	throws IOException {
		long count = 0;		// the byte count
		int b;				// the byte read from the file
		while ((b = is.read()) != -1) {
			os.write(b);
		}
		is.close();
		os.close();
    }
}
