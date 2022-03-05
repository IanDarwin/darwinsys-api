package com.darwinsys.io;

import java.io.IOException;

/** The general contract of a class to get bytes.
 * Used in Dumper and related classes.
 */
public interface DumpSource {
	/** Get the next byte, or, -1.
	 * @return The next byte
	 * @throws IOException If duh.
	 */
	public int get() throws IOException;
}
