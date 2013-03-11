package com.darwinsys.io;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import org.junit.Test;
import static org.junit.Assert.*;

public class DataIOTest {
	
	byte[] data = {
			1, 2, 3, 4,
			5, 6, 7, 8,
			9, 0, 0, 0,
			0x7f, 00, 00, 00
	};
	
	int[] expect = {
			0x01020304,
			0x05060708,
			0x09000000,
			0x7f000000
	};

	@Test
	public void test() throws Exception {
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
		for (int i : expect) {
			assertEquals("Element " + i, i, DataIO.readUnsignedInt(dis));
		}
	}

}
