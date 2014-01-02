// BEGIN main
package com.darwinsys.csv;

import java.util.Iterator;
import java.util.List;

/* Simple demo of CSV parser class. */
public class CSVSimple {
	public static void main(String[] args) {
		CSVImport parser = new CSVImport();
		List list = parser.parse(
			"\"LU\",86.25,\"11/4/1998\",\"2:19PM\",+4.0625");
		Iterator it = list.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}

		// Now try with a non-default separator
		parser = new CSVImport('|');
		list = parser.parse(
			"\"LU\"|86.25|\"11/4/1998\"|\"2:19PM\"|+4.0625");
		it = list.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
}
// END main
