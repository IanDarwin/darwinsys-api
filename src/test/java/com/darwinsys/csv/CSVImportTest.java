package com.darwinsys.csv;

import java.util.Iterator;
import java.util.List;


/**
 * @author ian
 */
public class CSVImportTest extends CSVParserTestBase {

	public CSVImportTest() {
		csv = new CSVImport();
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
