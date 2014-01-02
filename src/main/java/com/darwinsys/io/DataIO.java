package com.darwinsys.io;

import java.io.DataInput;
import java.io.IOException;

/**
 * One I-O primitives for DataInputStream; should probably have just subclassed DataInputStream.
 * All methods are static, since there is no state.
 */
public class DataIO {

	/** Nobody should need to create an instance; all methods are static */
	private DataIO() { 
		// nothing to do
	}

    /** Read an unsigned int from a DataInput
	 * @param is DataInput (DataInputStream, RandomAccessFile, etc).
	 * @return long, to hold an unsigned int.
	 */
    public static long readUnsignedInt(DataInput is) throws IOException {
		// Need to read 4 bytes from the input, unsigned.
		// Do it yourself; there is no readUnsignedInt().
		return
			((long)(is.readUnsignedByte() & 0xff) << 24) |
			((long)(is.readUnsignedByte() & 0xff) << 16) |
			((long)(is.readUnsignedByte() & 0xff) <<  8) |
			((long)(is.readUnsignedByte() & 0xff) <<  0);
	}
    
    // Don't think of adding readUnsignedShort; this already exists in DataInputStream. RTFM before coding.
}
