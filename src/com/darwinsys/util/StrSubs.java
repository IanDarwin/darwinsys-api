package com.darwinsys.util;

/** A few String functions that are missing from String
 * @author Ian Darwin
 * @version $Id$
 */
public class StrSubs {

	/** A simple main for testing */
	public static void main(String[] args) {
		System.out.println(
			subst("lazy", "supine", "A quick bronze fox lept a lazy bovine"));
		System.out.println(
			subst("$DIR", "/home/ian", "$DIR/xxx"));
	}

	/** Replace oldstr with newstr in instring */
	public static String subst(String oldStr, 
		String newStr, String inString) {

		int start = inString.indexOf(oldStr);
		if (start == -1) {
			return inString;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(inString.substring(0, start));
		sb.append(newStr);
		sb.append(inString.substring(start+oldStr.length()));
		return sb.toString();
	}
} 
