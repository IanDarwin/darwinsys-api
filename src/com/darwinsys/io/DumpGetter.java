package com.darwinsys.util;

import java.io.*;


/** The general contract of a class to get bytes. */
public interface DumpGetter {
	public int get() throws IOException;
}

