package com.darwinsys.io;

import java.io.*;

/** The general contract of a class to get bytes.
  * Used in Dumper and related classes.
  */
public interface DumpGetter {
	public int get() throws IOException;
}

