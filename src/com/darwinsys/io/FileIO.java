package com.darwinsys.io;

import java.io.*;  // Not much point in "organize imports" here...

import com.darwinsys.lang.StringUtil;

/**
 * Some simple file I-O primitives reimplemented in Java.
 * All methods are static, since there is no state.
 * @version $Id$
 */
public class FileIO {
	
	/** The size of blocking to use */
	protected static final int BLKSIZ = 16384;

	/** String for encoding UTF-8; copied by inclusion from StringUtil. */
	public static final String ENCODING_UTF_8 = StringUtil.ENCODING_UTF_8;
	
	/** Nobody should need to create an instance; all methods are static */
	private FileIO() {
		// Nothing to do
	}

    /** Copy a file from one filename to another */
    public static void copyFile(String inName, String outName)
	throws FileNotFoundException, IOException {
		BufferedInputStream is = null;
		BufferedOutputStream os = null;
		try {
			is = new BufferedInputStream(new FileInputStream(inName));
			os = new BufferedOutputStream(new FileOutputStream(outName));
			copyFile(is, os, false);
		} finally {
			is.close();
			os.close();
		}
	}

	/** Copy a file from an opened InputStream to opened OutputStream */
	public static void copyFile(InputStream is, OutputStream os, boolean close) 
	throws IOException {
		byte[] b = new byte[BLKSIZ];				// the byte read from the file
		int i;
		while ((i = is.read(b)) != -1) {
			os.write(b, 0, i);
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
		BufferedReader ir = new BufferedReader(new FileReader(inName));
		copyFile(ir, pw, close);
	}
	
	/**
	 * Copy one file to another, given File objects representing the files.
	 * @param file File representing the source, must be a single file.
	 * @param target File representing the location, may be file or directory.
	 * @throws IOException
	 */
	public static void copyFile(File file, File target) throws IOException {
		if (!file.exists() || !file.isFile() || !(file.canRead())) {
			throw new IOException(file + " is not a readable file");
		}
		File dest = target;
		if (target.isDirectory()) {
			dest = new File(dest, file.getName());
		}
		InputStream is = null;
		OutputStream os  = null;
		try {
			is = new FileInputStream(file);
			os = new FileOutputStream(dest);
			int count = 0;		// the byte count
			byte[] b = new byte[BLKSIZ];	// the bytes read from the file
			while ((count = is.read(b)) != -1) {
				os.write(b, 0, count);
			}
		} finally {
			is.close();
			os.close();
		}
	}



	/** Copy a data file from one filename to another, alternate method.
	 * As the name suggests, use my own buffer instead of letting
	 * the BufferedReader allocate and use the buffer.
	 */
	public void copyFileBuffered(String inName, String outName) throws
			FileNotFoundException, IOException {
		InputStream is = new FileInputStream(inName);
		OutputStream os = new FileOutputStream(outName);
		int count = 0;		// the byte count
		byte[] b = new byte[BLKSIZ];	// the bytes read from the file
		while ((count = is.read(b)) != -1) {
			os.write(b, 0, count);
		}
		is.close();
		os.close();
	}
	
	/**
	 * Copy all objects found in and under "fromdir", to their places in "todir".
	 * @param fromDir
	 * @param toDir
	 * @throws IOException
	 */
	public static void copyRecursively(File fromDir, File toDir) throws IOException {
		if (!fromDir.exists()) {
			throw new IOException("Source directory does not exist");
		}
		if (!toDir.exists()) {
			throw new IOException("Destination dir must exist");
		}
		File[] all = fromDir.listFiles();
		for (File file : all) {
			if (file.isDirectory()) {
				File destsubdir = new File(toDir, file.getName());
				copyRecursively(file, destsubdir);
			} else if (file.isFile()) {
				copyFile(file, toDir);
			} else {
				System.err.println(
					String.format("Warning: %s is neither file nor directory", file));
			}
		}
	}
	
	// Methods that do reading.
	/** Open a file and read the first line from it. */
	public static String readLine(String inName)
	throws FileNotFoundException, IOException {
		BufferedReader is = null;
		try {
		is = new BufferedReader(new FileReader(inName));
		String line = null;
		line = is.readLine();
		is.close();
		return line;
		} finally {
			if (is != null) 
				is.close();
		}
	}
	
	/** Read the entire content of a Reader into a String */
	public static String readerToString(Reader is) throws IOException {
		StringBuffer sb = new StringBuffer();
		char[] b = new char[BLKSIZ];
		int n;

		// Read a block. If it gets any chars, append them.
		while ((n = is.read(b)) > 0) {
			sb.append(b, 0, n);
		}

		// Only construct the String object once, here.
		return sb.toString();
	}

	/** Read the content of a Stream into a String */
	public static String inputStreamToString(InputStream is)
	throws IOException {
		return readerToString(new InputStreamReader(is));
	}

	/** Write a String as the entire content of a File */
	public static void stringToFile(String text, String fileName)
	throws IOException {
		BufferedWriter os = new BufferedWriter(new FileWriter(fileName));
		os.write(text);
		os.flush();
		os.close();
	}

	/** Open a BufferedReader from a named file. */
	public static BufferedReader openFile(String fileName)
	throws IOException {
		return new BufferedReader(new FileReader(fileName));
	}
}
