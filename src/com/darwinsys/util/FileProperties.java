package com.darwinsys.util;

import java.io.*;
import java.util.*;

/** 
 * The <CODE>FileProperties</CODE> class extends <CODE>Properties</CODE>,
 * "a persistent set of properties [that] can be saved to a stream
 * or loaded from a stream". This subclass attends to all the mundane
 * details of opening the Stream(s) for actually saving and loading
 * the Properties.
 *
 * <P>This subclass preserves the useful feature that
 * a property list can contain another property list as its
 * "defaults"; this second property list is searched if
 * the property key is not found in the original property list.
 *
 * @author Ian F. Darwin, ian@darwinsys.com
 * @version $Id$
 */
public class FileProperties extends Properties {
	protected String fileName = null;

	/** Construct a FileProperties given a fileName. */
	public FileProperties(String loadsaveFileName)
	throws IOException {
		super();
		fileName = loadsaveFileName;
		load();
	}

	/** Construct a FileProperties given a fileName and 
	 * a list of default properties.
	 */
	public FileProperties(String loadsaveFileName, Properties defProp)
	throws IOException {
		super(defProp);
		fileName = loadsaveFileName;
		load();
	}

	/** The InputStream for loading */
	protected InputStream inStr = null;

	/** The OutputStream for loading */
	protected OutputStream outStr = null;

	/** Load the properties from the saved filename.
	 * If that fails, try again, tacking on the .properties extension
	 */
	public Properties load() throws IOException {
		try {
			if (inStr==null) {
				inStr = new FileInputStream(fileName);
			}
		} catch (FileNotFoundException fnf) {
			if (!fileName.endsWith(".properties")) {
				inStr = new FileInputStream(fileName + ".properties");
				// If we succeeded, remember it:
				fileName += ".properties";
			} else
				// It did end with .properties and failed, re-throw exception.
				throw fnf;
		}
		// now message the superclass code to load the file.
		load(inStr);

		// Return "this" for convenience
		return this;
	}

	/** Save the properties to disk for later loading. */
	public void save() throws IOException {
		if (outStr==null) {
			outStr = new FileOutputStream(fileName);
		}
		// Get the superclass to do most of the work for us.
		store(outStr, "# Written by FileProperties.save() at " + new Date());
	}

	public void close() {
		try {
			if (inStr != null)
				inStr.close();
			if (outStr != null)
				outStr.close();
		} catch (IOException e) {
			// don't care 
		}
	}
}
