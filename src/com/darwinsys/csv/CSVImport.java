import java.util.*;

/** Parse comma-separated values (CSV), a common Windows file format.
 * Sample input: "LU",86.25,"11/4/1998","2:19PM",+4.0625
 * <p>
 * Inner logic adapted from a C++ original that was
 * Copyright (C) 1999 Lucent Technologies
 * Excerpted from 'The Practice of Programming'
 * by Brian W. Kernighan and Rob Pike.
 * <p>
 * Included by permission of the http://tpop.awl.com/ web site, 
 * which says:
 * "You may use this code for any purpose, as long as you leave 
 * the copyright notice and book citation attached." I have done so.
 * @author Brian W. Kernighan and Rob Pike (C++ original)
 * @author Ian F. Darwin (translation into Java and removal of I/O)
 */
public class CSV {	

	public static final String SEP = ",";

	/** Construct a CSV parser, with the default separator. */
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

	/** The fields in the current String */
	protected ArrayList list = new ArrayList();

	/** the separator string for this parser */
	protected String fieldsep;

	/** parse: break the input String into fields
	 * @return java.util.Iterator containing each field 
	 * from the original as a String, in order.
	 */
	public Iterator parse(String line)
	{
		StringBuffer sb = new StringBuffer();
		list.clear();			// discard previous, if any
		int i = 0;

		if (line.length() == 0) {
			list.add(line);
			return list.iterator();
		}

		do {
			sb.setLength(0);
			if (i < line.length() && line.charAt(i) == '"')
				i = advquoted(line, sb, ++i);	// skip quote
			else
				i = advplain(line, sb, i);
			list.add(sb.toString());
			i++;
		} while (i < line.length());

		return list.iterator();
	}

	/** advquoted: quoted field; return index of next separator */
	protected int advquoted(String s, StringBuffer sb, int i)
	{
		int j;

		for (j = i; j < s.length(); j++) {
			// found end of field if find unescaped quote.
			if (s.charAt(j) == '"' && s.charAt(j-1) != '\\') {
				int k = s.indexOf(fieldsep, j);
				// System.out.println("j="+j+", k="+k);
				if (k == -1)	// no separator found after this field
					k += s.length();
				for (k -= j; k-- > 0; )
					sb.append(s.charAt(j++));
				break;
			}
			sb.append(s.charAt(j));	// regular character.
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
}
