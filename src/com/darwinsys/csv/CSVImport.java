/* Inner logic adapted from a C++ original that was */
/* Copyright (C) 1999 Lucent Technologies */
/* Excerpted from 'The Practice of Programming' */
/* by Brian W. Kernighan and Rob Pike */

import java.io.*;
import java.util.*;

/** read and parse comma-separated values
 * sample input: "LU",86.25,"11/4/1998","2:19PM",+4.0625
 * @author Brian W. Kernighan and Rob Pike (C++ original)
 * @author Ian F. Darwin (translation into Java and removal of I/O)
 */
public class CSV {	

	public static final String SEP = ",";

	/** Construct a CSV parser */
	public CSV() {
		this(SEP);
	}

	/** Construct a CSV parser with the given separator. Must be
	 * exactly the string that is the separator; not a list of
	 * separator characters!
	 */
	public CSV(String sep) {
		fieldsep = sep;
	}

	protected Vector list = new Vector();		// field Strings
	protected String fieldsep;		// separator characters

/** split: split line into fields
 * @return java.util.Enumeration containing each field as a String, in order.
 */
public Enumeration split(String line)
{
	StringBuffer fld = new StringBuffer();
	list.removeAllElements();			// discard previous, if any
	int i = 0, j;

	if (line.length() == 0) {
		list.addElement(line);
		return list.elements();
	}

	do {
		fld.setLength(0);
		if (i < line.length() && line.charAt(i) == '"')
			j = advquoted(line, fld, ++i);	// skip quote
		else
			j = advplain(line, fld, i);
		list.addElement(fld.toString());
		i = j + 1;
	} while (j < line.length());

	return list.elements();
}

/** advquoted: quoted field; return index of next separator */
protected int advquoted(String s, StringBuffer fld, int i)
{
	int j;

	for (j = i; j < s.length(); j++) {
		// found end of field if find unescaped quote.
		if (s.charAt(j) == '"' && s.charAt(j-1) != '\\') {
			int k = s.indexOf(fieldsep, j);
			System.out.println("j="+j+", k="+k);
			if (k == -1)	// no separator found after this field
				k += s.length();
			for (k -= j; k-- > 0; )
				fld.append(s.charAt(j++));
			break;
		}
		fld.append(s.charAt(j));
	}
	return j;
}

/** advplain: unquoted field; return index of next separator */
protected int advplain(String s, StringBuffer fld, int i)
{
	int j;

	j = s.indexOf(fieldsep, i); // look for separator
	// System.out.println("i="+i+", j="+j);
	if (j == -1) {               	// none found
		fld.append(s.substring(i));
		return s.length();
	} else {
		fld.append(s.substring(i, j));
		return j;
	}
}

	/** Canonical main program - test lines from a file and print. */
	public static void main(String[] argv) throws IOException
	{
		String line;
		CSV csv = new CSV();

		BufferedReader is = new BufferedReader(
			new InputStreamReader(System.in));
		while ((line = is.readLine()) != null) {
			System.out.println("line = " + line);
			Enumeration e = csv.split(line);
			int i = 0;
			while (e.hasMoreElements()) 
				System.out.println(i++ + ": " + e.nextElement());
		}
	}
}
