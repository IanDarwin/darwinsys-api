package com.darwinsys.csv;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;


/**
 * @author ian
 */
public abstract class CSVParserTestBase extends TestCase {

	CSVParser csv;	// must be set in subclass constructor.

	class X {
		int expectLength;String input;
		public X(int expectLength, String input) {
			super();
			this.expectLength = expectLength;
			this.input = input;
		}
	}
	X[] data = {
		new X(1, "abc"),
		new X(2, "hello, world"),
		new X(3, "a,b,c"),
		new X(3, "a\"bc,d,e"),
		new X(3, "\"a,a\",b,\"c:\\foo\\bar\""),
		// new X(1, "\"he\"llo"), -- fails - returns 2(!)
		new X(2, "123,456"),
		new X(5, "\"LU\",86.25,\"11/4/1998\",\"2:19PM\",+4.0625"),
		new X(2, "bad \"input\",123e01"),
		//new X(0,
		//"XYZZY,\"\"|\"OReilly & Associates| Inc."|"Darwin| Ian"|"a \"glug\" bit|"|5|"Memory fault| core NOT dumped"),
	};

	/** test all the Strings in "data" */
	public void testCSV() throws Throwable {
		if (csv == null) {
			throw new IllegalArgumentException("csv parser not created");
		}
		int i = 0;
		try {
			for (i = 0; i < data.length; i++){
				List l = csv.parse(data[i].input);
				assertEquals(data[i].expectLength, l.size());
				for (int k = 0; k < l.size(); k++){
					System.out.print("[" + l.get(k) + "],");
				}
				System.out.println();
			}
		} catch (Exception t) {
			System.err.printf("Error occured in data[%d], %s%n", i, data[i].expectLength);
			throw t;
		}
	}

	/** Test one String with a non-default delimiter */
	public void testBarDelim() {
		CSVImport parser = new CSVImport('|');
		List l = parser.parse(
			"\"LU\"|86.25|\"11/4/1998\"|\"2:19PM\"|+4.0625");
		assertEquals(l.size(), 5);
		Iterator it = l.iterator();
		while (it.hasNext()) {
			System.out.print("[" + it.next() + "],");
		}
		System.out.println();
	}
}
