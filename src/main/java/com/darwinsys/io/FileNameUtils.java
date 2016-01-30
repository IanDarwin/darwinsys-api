package com.darwinsys.io;

/**
 * Stuff for messing with filenames.
 */
public class FileNameUtils {
	
	/**
	 * The name format we use; can be used for laying out templates etc.
	 */
	private static final String FILENAME_FORMAT = "YYYYMMDDHHMM";

	/** Convert a date (or millis) into a String matching FILENAME_FORMAT.
	 * The %1 are needed throughout since we only pass one argument.
	 */
	private final static String FILENAME_PRINTF_TEMPLATE = "%1$tY%1$tm%1$td%1$tH%1$tM";

	// This could become a facility for interpreting filename formats
	//	private static Map<String,String> formatMap = new HashMap<String,String>();
	//	static {
	//		formatMap.put("YYYY", "$tY");
	//	}

	public static String getDefaultFilenameFormat() {
		return FILENAME_FORMAT;		
	}

	/** Return the next file name; given the suffix (which should NOT include
	 * a leading '.', since we provide that!)
	 * @param suffix The filename extention sans '.', e.g., "mp3"
	 * @return The filename, e.g., foo.mp3
	 */
	public static String getNextFilename(String suffix) {
		return getNextFilenamePrefix() + "." + suffix;
	}

	/**
	 * Return the date/time formatted to match FILENAME_FORMAT.
	 * @return the formatted string
	 */
	public static String getNextFilenamePrefix() {
		return String.format(FILENAME_PRINTF_TEMPLATE, System.currentTimeMillis());		
	}
}
