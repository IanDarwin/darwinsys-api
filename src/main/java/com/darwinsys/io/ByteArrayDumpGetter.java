package com.darwinsys.io;

/** A DumpSource that reads from an in-memory array of bytes */
public class ByteArrayDumpGetter implements DumpSource {

	private byte[] data;
	private int offset;
	private int max;
	
	/** Construct this object.
	 * @param d The data
	 */
	public ByteArrayDumpGetter(byte[] d) {
		data = d;
		offset = 0;
		max = data.length;
	}

	/** Retrieve the next value.
	 * @return The next value, or -1 when all done.
	 */
	public int get() {
		if (offset < max)
			return data[offset++];
		return -1;
	}
}
