package com.darwinsys.io;

/** Simple wrapper class for native nextfd method; nextfd is used to watch filedesc leaks.
 */
public class NextFD {

	private static final int INVALID_FD = -1;

	/** The name of the shared library */
	public static final String LIBRARY_NAME = "darwinsys";
	
	/** True if the shared library loaded successfully */
	private static boolean loaded;

	/** Native method to return the next available file descriptor.
	 * Obviously this will only work on POSIX-like systems that support the
	 * notion of numeric file descriptors, but it's a good technique for
	 * tracking file descriptor leaks in APIs used e.g., when indexing
	 * significant numbers of files.
	 */
	private static native int nextfd();
	
	/** Public method to try to return the next available file descriptor, if available.
	 * @return the next free system file descriptor, or INVALID_FD (-1) if not available.
	 */
	public static int getNextFD() {
		if (loaded) {
			return nextfd();
		}
		return INVALID_FD;
	}
	
	static {
		/** Load the .so or dll, set loaded = true if it succeeds. */
		try {
			System.loadLibrary(LIBRARY_NAME);
			loaded = true;
		} catch (UnsatisfiedLinkError e) {
			System.err.printf("*** Could not load the %s library (.so or .dll)%n",
				LIBRARY_NAME);
			System.err.println("This means that NextFD.getNextFD() will always return -1.");
			System.err.println("Please check that you have installed the shared library and");
			System.err.println("if necessary set your LD_LIBRARY_PATH or equivalent.");
		}
	}
}
