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
	public FileProperties(String loadsaveFileName) {
		super();
		fileName = loadsaveFileName;
	}

	/** Construct a FileProperties given a fileName and 
	 * a list of default properties.
	 */
	public FileProperties(String loadsaveFileName, Properties defProp) {
		super(defProp);
		fileName = loadsaveFileName;
	}

	/** The InputStream for loading */
	protected InputStream inStr = null;

	/** The OutputStream for loading */
	protected OutputStream outStr = null;

	/** Load the proerties from the saved file */
	public void load() throws IOException {
		if (inStr==null) {
			inStr = new FileInputStream(fileName);
		}
		load(inStr);
	}

	/** Save the properties for later loading. */
	public void save() throws IOException {
		if (outStr==null) {
			outStr = new FileOutputStream(fileName);
		}
		load(inStr);
	}
}
