package com.darwinsys.io;

import java.io.*;

/** Simple demo of nextfd method; nextfd is used to watch filedesc leaks.
 */
public class NextFD {

	public static final String LIBRARY_NAME = "darwinsys";

	/** Native method to return the next available file descriptor.
	 * Obviously this will only work on POSIX-like systems that support the
	 * notion of numeric file descriptors, but it's a good technique for
	 * tracking file descriptor leaks in APIs used e.g., when indexing
	 * significant numbers of files.
	 */
	public static native int nextfd();

	/** A file that exists on most POSIX systems; it is the file we open
	 * for reading iff no argument is passed on the command line.
	 */
	public static final String COMMON_FILE = "/etc/passwd";
	
	/**
	 * Main: run it, open a file, run it, close the file, run it again.
	 * <b>Doubles as a regression test</b>
	 * @param args One optional filename, which must exist and be readable.
	 */
	public static void main(String[] args) throws IOException {

		/** Load the .so or dll */
		try {
		System.loadLibrary(LIBRARY_NAME);
		} catch (UnsatisfiedLinkError e) {
			System.err.printf("*** Could not load the %s library (.so or .dll)%n",
				LIBRARY_NAME);
			System.err.println("Check that you have installed the library and");
			System.err.println("if necessary set your LD_LIBRARY_PATH or equivalent.");
		}

		int start = nextfd();
		System.out.println("nextfd returned " + start);

		String fileName = args.length > 0 ? args[0] : COMMON_FILE;

		InputStream is = new FileInputStream(fileName);
		System.out.printf("File to %s is open.%n", fileName);

		int high = nextfd();
		System.out.println("nextfd returned " + high);
		if (high != start + 1) {
			throw new IllegalStateException("high != start + 1");
		}

		is.close();
		System.out.printf("File to %s is closed.%n", fileName);

		int end = nextfd();
		System.out.println("nextfd returned " + end);
		if (end != start) {
			throw new IllegalStateException("end != start");
		}
	}
}
