package com.darwinsys.util;

import java.io.PrintWriter;

/** MkAccessors is a utility to make a series of Java "accessors"
 * (set/get methods a la JavaBeans pattern). 
 * You're probably better off using an IDE!
 * <p>
 * Primarily meant for command-line use, but can be used from within
 * other tools (note that all methods are static).
 * I use it (with a shell script mkAccessors, which is
 * <pre>java -classpath $HOME/classes/ext/com-darwinsys-all.jar \
	com.darwinsys.util.MkAccessors $* </pre>
 * or something similar); in the vi or vim editors you just say
 * <pre>:r !mkAccessors firstName lastName address</pre>
 * to generate the setFirstName/getFirstName, etc. methods
 * right in the Java source while you're editing.
 * Your mileage may vary if you use a less-powerful editing tool.
 * @author Ian Darwin, http://www.darwinsys.com/
 */
public class MkAccessors {

	/** Private constructor, since no instances are allowed */
	private MkAccessors() {
		// Null
	}

	/** make an accessor for each field named in the command line */
	public static void main(String[] args) {
System.out.println("in main(" + args + ")...");
		PrintWriter out = new PrintWriter(System.out);
		for (int i=0; i < args.length; i++) {
			process(args[i], out);
		}
	}

	/** Generate the set and get methods for field "fld"
	 * @param fld - the field name
	 * @param out - the PrintWriter to print to.
	 */
	public static void process(String fld, PrintWriter out) {
System.out.println("process(" + fld + ")...");
		System.out.println( "\t/** Get the value of " + fld + " */");
		out.println( "\tpublic String get" + firstCap(fld) + "() {");
		out.println( "\t	return " + fld + ";");
		out.println( "\t}");
		out.println();
		out.println( "\t/** Set the value of " + fld + " */");
		out.println( "\tpublic void set" + firstCap(fld) + 
			"(String " + fld + ") {");
		out.println( "\t	this." + fld + " = " + fld + ";");
		out.println( "\t}");
		out.println();
	}

	static String firstCap(String fld) {
		StringBuffer sb = new StringBuffer(fld);
		char ch = sb.charAt(0);
		sb.deleteCharAt(0);
		sb.insert(0, Character.toUpperCase(ch));
		return sb.toString();
	}
}
