package com.darwinsys.util;

import java.io.*;

/** A Getter that reads from an in-memory array of bytes */
class ByteArrayDumpGetter implements DumpGetter {

	public ByteArrayDumpGetter(byte[] data) {
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
