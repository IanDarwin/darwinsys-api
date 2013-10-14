package com.darwinsys.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/** A DumpSource that reads from a file. */
public class StreamDumpGetter implements DumpSource {
	private BufferedInputStream is;
	public StreamDumpGetter(InputStream ois) throws IOException {
		if (ois instanceof BufferedInputStream)
			is = (BufferedInputStream)ois;
		else
			is = new BufferedInputStream(ois);
	}

	public int get() throws IOException {
		return is.read();
	}

	public void close() throws IOException {
		is.close();
	}
}
