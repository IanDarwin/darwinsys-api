package com.darwinsys.util;

import java.io.*;


/** The general contract of a class to get bytes. */
public interface Getter {
	public int get();
}

/** A Getter that reads from an in-memory array of bytes */
class ByteArrayGetter implements Getter {
	public ByteArrayGetter(byte[] data) {
		this.data = data;
		offset = 0;
		max = data.length;
	}
	private byte[] data;
	private int offset;
	private int max;
	public int get() {
		if (offset < max)
			return data[offset++];
		return -1;
	}
}

class StreamGetter implements Getter {
	private BufferedInputStream is;
	public StreamGetter(InputStream ois) throws IOException {
		if (ois instanceof BufferedInputStream)
			is = (BufferedInputStream)ois;
		else
			is = new BufferedInputStream(ois);
	}

	public int get() throws IOException {
		return is.read();
	}
}
