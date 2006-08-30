package com.darwinsys.csv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

/** CSV in action: lines from a file and print. */
public class CSVFile {

	public static void main(String[] args) throws IOException {

		// Construct a new CSV parser.
		CSVImport csv = new CSVImport();

		if (args.length == 0) {	// read standard input
			BufferedReader is = new BufferedReader(
				new InputStreamReader(System.in));
			process(csv, is);
		} else {
			for (int i=0; i<args.length; i++) {
				process(csv, new BufferedReader(new FileReader(args[i])));
			}
		}
	}

	protected static void process(CSVImport csv, BufferedReader is) throws IOException {
		String line;
		while ((line = is.readLine()) != null) {
			System.out.println("line = `" + line + "'");
			Iterator e = csv.parse(line).iterator();
			int i = 0;
			while (e.hasNext())
				System.out.println("field[" + i++ + "] = `" +
					e.next() + "'");
		}
	}
}
