package com.darwinsys.io;

import java.io.IOException;

/** The general contract of a class to get bytes.
 * Used in Dumper and related classes.
 */
public interface DumpSource {
	public int get() throws IOException;
}
