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
	/** The name of the file this FileProperties represents. */
	protected String fileName = null;
	/** True if the file represented by fileName exists */
	private boolean exists = false;

	/** Construct a FileProperties given a fileName. */
	public FileProperties(String loadsaveFileName)
	throws IOException {
		super();
		setFileName(loadsaveFileName);
		load();
	}

	/** Construct a FileProperties given a fileName and 
	 * a list of default properties.
	 */
	public FileProperties(String loadsaveFileName, Properties defProp)
	throws IOException {
		super(defProp);
		setFileName(loadsaveFileName);
		load();
	}

	/** Set the fileName. If it exists not, but it+".properties" does,
	 * save the full name.
	 */
	void setFileName(String newName) {
		fileName = newName;
		if (new File(fileName).exists()) {
			exists = true;
			return;
		}
		if (!newName.endsWith(".properties")) {
			File f2 = new File(newName + ".properties");
			if (f2.exists()) {
				exists = true;
				fileName = newName + ".properties";
				return;
			}
		}
	}

	public String getFileName() {
		return fileName;
	}

	/** Load the properties from the saved filename.
	 * If that fails, try again, tacking on the .properties extension
	 */
	public Properties load() throws IOException {

		if (!exists)
			return this;

		// Sorry it's an InputStream not a Reader, but that's what
		// the superclass load method still requires (as of 1.4 at least).
		InputStream inStr = new FileInputStream(fileName);

		// now message the superclass code to load the file.
		load(inStr);

		inStr.close();

		// Return "this" for convenience
		return this;
	}

	/** Save the properties to disk for later loading. */
	public void save() throws IOException {
		OutputStream outStr = new FileOutputStream(fileName);
		
		// Get the superclass to do most of the work for us.
		store(outStr, "# Written by FileProperties.save() at " + new Date());

		outStr.close();
	}

	/** No longer needed.
	 * @deprecated No longer needed.
	 */
	public void close() {
	}
}
