// BEGIN main
package com.darwinsys.csv;

import java.util.List;

/* Simple demo of CSV parser class. */
public class CSVSimple {
	public static void main(String[] args) {
		CSVImport parser = new CSVImport();
		List<String> list = parser.parse(
			"\"LU\",86.25,\"11/4/1998\",\"2:19PM\",+4.0625");
		for (String word : list) {
			System.out.println(word);
		}

		// Now try with a non-default separator
		parser = new CSVImport('|');
		list = parser.parse(
			"\"LU\"|86.25|\"11/4/1998\"|\"2:19PM\"|+4.0625");
		for (String word : list) {
			System.out.println(word);
		}
	}
}
// END main
