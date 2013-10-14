package com.darwinsys.io;

/** A Getter that reads from an in-memory array of bytes */
public class ByteArrayDumpGetter implements DumpSource {

	private byte[] data;
	private int offset;
	private int max;
	
	public ByteArrayDumpGetter(byte[] d) {
		data = d;
		offset = 0;
		max = data.length;
	}

	public int get() {
		if (offset < max)
			return data[offset++];
		return -1;
	}
}
