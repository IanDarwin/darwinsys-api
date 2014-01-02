// BEGIN main
package com.darwinsys.io;

import java.io.*;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.darwinsys.lang.StringUtil;
import com.darwinsys.util.Debug;

/**
 * Some file I-O primitives reimplemented in Java.
 * All methods are static, since there is no state.
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
			if (is != null) {
				is.close();
			}
			if (os != null) {
				os.close();
			}
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
	 * Copy a tree of files to directory, given File objects representing the files.
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
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(inName);
			os = new FileOutputStream(outName);
			int count = 0;		// the byte count
			byte[] b = new byte[BLKSIZ];	// the bytes read from the file
			while ((count = is.read(b)) != -1) {
				os.write(b, 0, count);
			}
		} finally {
			if (is != null) {
				is.close();				
			}
			if (os != null) {
				os.close();
			}
		}
	}
	
	/**
	 * Copy all objects found in and under "fromdir", to their places in "todir".
	 * @param fromDir
	 * @param toDir
	 * @throws IOException
	 */
	public static void copyRecursively(File fromDir, File toDir, boolean create) throws IOException {
		Debug.printf("fileio", "copyRecursively(%s, %s%n", fromDir, toDir);
		if (!fromDir.exists()) {
			throw new IOException(
				String.format("Source directory %s does not exist", fromDir));
		}
		if (create) {
			toDir.mkdirs();
		} else if (!toDir.exists()) {
			throw new IOException(String.format("Destination dir %s must exist", toDir));
		}
		for (File src : fromDir.listFiles()) {
			if (src.isDirectory()) {
				File destSubDir = new File(toDir, src.getName());
				copyRecursively(src, destSubDir, true);
			} else if (src.isFile()) {
				copyFile(src, toDir);
			} else {
				System.err.println(
					String.format("Warning: %s is neither file nor directory", src));
			}
		}
	}
	
	public static void copyRecursively(File fromDir, File toDir) throws IOException {
		copyRecursively(fromDir, toDir, false);
	}
	
	public static void deleteRecursively(File startDir) throws IOException {
		
		String startDirPath = startDir.getCanonicalPath();
		
		// Pass one - delete recursively
		for (File f : startDir.listFiles()) {
			if (!f.getCanonicalPath().startsWith(startDirPath)) {
				throw new IOException("Attempted to go out of " + startDir);
			}
			if (f.isDirectory()) {
				deleteRecursively(f);
			}
		}
		// Pass two - delete whatever's left: files and (empty) directories
		for (File f : startDir.listFiles()) {
			f.delete();
			if (f.exists()) {
				System.err.println(f + " did not get deleted!");
			}
		}
		
		// Pass three - delete the (now empty) starting directory
		startDir.delete();
	}
	
	/**
	 * Copy a tree of files to directory, given File objects representing the files.
	 * @param base File representing the source, must be a single file.
	 * @param startingDir
	 * @param toDir File representing the location, may be file or directory.
	 * @throws IOException 
	 */
	public static void copyRecursively(JarFile base, JarEntry startingDir,
			File toDir) throws IOException {
		if (!startingDir.isDirectory()) {
			throw new IOException(String.format(
					"Starting point %s is not a directory", startingDir));
		}
		if (!toDir.exists()) {
			throw new IOException(String.format(
					"Destination dir %s must exist", toDir));
		}
		Enumeration<JarEntry> all = base.entries();
		while (all.hasMoreElements()) {
			JarEntry file = all.nextElement();
			// XXX ensure that it matches starting dir
			if (file.isDirectory()) {
				copyRecursively(base, file, new File(toDir, file.getName()));
			} else {
				InputStream is = null;
				OutputStream os = null;
				try {
					is = base.getInputStream(file);
					os = new FileOutputStream(new File(toDir, file
							.getName()));
					copyFile(is, os, false);
				} finally {
					if (os != null)
						os.close();
					if (is != null)
						is.close();
				}
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
	
	/** Read the entire content of a Reader into a String;
	 * of course Readers should only be used for text files;
	 * please do not use this to read a JPEG file, for example.
	 */
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

	public static String readAsString(String filename) throws IOException {
		return readerToString(new FileReader(filename));
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
// END main
