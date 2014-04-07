package com.darwinsys.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

/**
 * The <code>FileProperties</code> class extends <code>Properties</code>,
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
 * @author Ian F. Darwin, http://www.darwinsys.com/
 */
public class FileProperties extends Properties {
	private static final long serialVersionUID = 3760847843971053111L;
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

	public FileProperties(String readonlyFileName, Class clazz)
	throws IOException {
		super();
		load(clazz.getResourceAsStream(readonlyFileName));
	}

	public FileProperties(InputStream is) throws IOException {
		load(is);
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
		// the superclass load method still requires.
		InputStream inStr = new FileInputStream(fileName);

		// now message the superclass code to load the file.
		load(inStr);

		inStr.close();

		// Return "this" for convenience
		return this;
	}

	/** Save the properties to disk for later loading.
	 * May only be used if this FileProperties was created
	 * with a filename; otherwise, use store() and give an
	 * OutputStream
	 * @deprecated Use store(OutputStream) instead.
	 */
	@Deprecated
	public void save() throws IOException {
		if (fileName == null) {
			throw new IOException("Tried to save a FileProperties loaded from CLASSPATH");
		}
		OutputStream outStr = new FileOutputStream(fileName);

		// Get the superclass to do most of the work for us.
		store(outStr, "# Written by FileProperties.save() at " + new Date());

		outStr.close();
	}

	@SuppressWarnings("unchecked")
	public Iterator<String> iterator() {
		return new EnumerationIterator(keys());
	}
}
