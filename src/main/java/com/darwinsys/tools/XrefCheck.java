package com.darwinsys.tools;

import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * AsciiDoc Xref checker:
 * Read one or more chapter files making up an AsciiDoc document; verify that every xref
 * (defined with name, see "ref") is defined elsewhere as a label (defined with [[name]], see "decl").
 */
public class XrefCheck {
	private final static boolean VERBOSE_SELFTEST = false;

	// BEGIN main
	static final Set<String> decls = new HashSet<>();
	static final Map<String,String> refs = new HashMap<>();
	static final Pattern decl = Pattern.compile("^\\[\\[([^,]+?)\\]\\]$");
	static final Pattern ref  = Pattern.compile("<<([^,]+?)>>");
	private final static boolean VERBOSE_RUN = false;

	public static void main(String[] args) throws Exception {
		selfTest(VERBOSE_SELFTEST);
		for (String file : args) {
			try {
				accumulateXrefs(file, VERBOSE_RUN);
			} catch (Exception e) {
				System.out.println(e + " in file " + file);
			}
		}
		for (String s : refs.keySet()) {
			if (!decls.contains(s)) {
				System.out.println(refs.get(s) + " use of undeclared ref " + s);
			}
		}
	}
	// END main

	public static void selfTest(boolean verbose) {
		Matcher md = decl.matcher("[[dog-chapter]]");
		md.find();
		if (verbose)
			System.out.println("DECL: " + md.group(1));
		if (!"dog-chapter".equals(md.group(1))) {
			throw new IllegalStateException("Failed to match decl");
		}

		Matcher mr = ref.matcher("see woof <<dog-chapter>> or meow <<cat_chapter>>.");
		int i = 0;
		while (mr.find()) {
			++i;
			if (verbose)
				System.out.println("REF: " + mr.group(1));

		}
		if (i != 2) {
			throw new IllegalStateException("Failed to match 2 xrefs");
		}
	}

	public static void accumulateXrefs(String fileName, boolean verbose) throws Exception {
		Matcher md, mr;
		try (BufferedReader is = new BufferedReader(new FileReader(fileName))) {
			String line = null;
			int lineNumber = 0;
			while ((line = is.readLine()) != null) {
				++lineNumber;
				if (line.length() == 0) {
					continue;
				}
				// Decls occur on a line by themselves
				if (line.startsWith("[[")) {
					md = decl.matcher(line);
					if (!md.find()) {
						System.out.println("Misformed label: " + line);
						continue;
					}
					decls.add(md.group(1));
					if (verbose)
						System.out.println("decl:" + md.group(1));
					continue;
				}
				// Not a decl, see if any refs in this line
				mr = ref.matcher(line);
				while (mr.find()) {
					String ref = mr.group(1);
					refs.put(ref, fileName + ":" + lineNumber);
					if (verbose)
						System.out.println("ref: " + mr.group(1));
				}
			}
		}
	}
}
