package com.darwinsys.csv;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;


/**
 * @author ian
 */
public abstract class CSVParserTestBase extends TestCase {

	CSVParser csv;	// must be set in subclass constructor.

	String[] data = {
		"abc",
		"hello, world",
		"a,b,c",
		"a\"bc,d,e",
		"\"a,a\",b,\"c:\\foo\\bar\"",
		"\"he\"llo",
		"123,456",
		"\"LU\",86.25,\"11/4/1998\",\"2:19PM\",+4.0625",
		"bad \"input\",123e01",
		//"XYZZY,\"\"|\"OReilly & Associates| Inc."|"Darwin| Ian"|"a \"glug\" bit|"|5|"Memory fault| core NOT dumped"

	};
	int[] listLength = {
					1,
					2,
					3,
					3,
					3,
					1,
					2,
					5,
					2
	};

	/** test all the Strings in "data" */
	public void testCSV() throws Throwable {
		if (csv == null) {
			throw new IllegalArgumentException("csv parser not created");
		}
		int i = 0;
		try {
			for (i = 0; i < data.length; i++){
				List l = csv.parse(data[i]);
				assertEquals(l.size() , listLength[i]);
				for (int k = 0; k < l.size(); k++){
					System.out.print("[" + l.get(k) + "],");
				}
				System.out.println();
			}
		} catch (Throwable t) {
			System.err.printf("Error occured in data[%d], %s%n", i, data[i]);
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
