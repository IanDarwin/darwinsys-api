package com.darwinsys.io;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataIOTest {
	
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
	void test() throws Exception {
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
		for (int i : expect) {
			assertEquals(i, DataIO.readUnsignedInt(dis), "Element " + i);
		}
	}

}
