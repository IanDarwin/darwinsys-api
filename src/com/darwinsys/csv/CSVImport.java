import java.util.*;

import com.darwinsys.util.Debug;

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
 * @author Ben Ballard (rewrote advQuoted to handle '""' and for readability)
 */
public class CSV {	

	public static final char DEFAULT_SEP = ',';

	/** Construct a CSV parser, with the default separator (`,'). */
	public CSV() {
		this(DEFAULT_SEP);
	}

	/** Construct a CSV parser with a given separator. 
	 * @param sep The single char for the separator (not a list of
	 * separator characters)
	 */
	public CSV(char sep) {
		fieldSep = sep;
	}

	/** The fields in the current String */
	protected List list = new ArrayList();

	/** the separator char for this parser */
	protected char fieldSep;

	/** parse: break the input String into fields
	 * @return java.util.Iterator containing each field 
	 * from the original as a String, in order.
	 */
	public List parse(String line)
	{
		StringBuffer sb = new StringBuffer();
		list.clear();			// recycle to initial state
		int i = 0;

		if (line.length() == 0) {
			list.add(line);
			return list;
		}

		do {
            sb.setLength(0);
            if (i < line.length() && line.charAt(i) == '"')
                i = advQuoted(line, sb, ++i);	// skip quote
            else
                i = advPlain(line, sb, i);
            list.add(sb.toString());
            Debug.println("csv", sb.toString());
			i++;
		} while (i < line.length());

		return list;
	}

	/** advQuoted: quoted field; return index of next separator */
	protected int advQuoted(String s, StringBuffer sb, int i)
	{
		int j;
		int len= s.length();
        for (j=i; j<len; j++) {
            if (s.charAt(j) == '"' && j+1 < len) {
                if (s.charAt(j+1) == '"') {
                    j++; // skip escape char
                } else if (s.charAt(j+1) == fieldSep) { //next delimeter
                    j++; // skip end quotes
                    break;
                }
            } else if (s.charAt(j) == '"' && j+1 == len) { // end quotes at end of line
                break; //done
			}
			sb.append(s.charAt(j));	// regular character.
		}
		return j;
	}

	/** advPlain: unquoted field; return index of next separator */
	protected int advPlain(String s, StringBuffer sb, int i)
	{
		int j;

		j = s.indexOf(fieldSep, i); // look for separator
		Debug.println("csv", "i = " + i + ", j = " + j);
        if (j == -1) {               	// none found
            sb.append(s.substring(i));
            return s.length();
        } else {
            sb.append(s.substring(i, j));
            return j;
        }
    }
}
