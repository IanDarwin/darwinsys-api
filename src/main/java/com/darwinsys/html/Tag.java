package com.darwinsys.html;

import java.io.PrintWriter;

/** A series of methods for writing HTML/XML tags.
 * Substantially less capable than Jakarta Element Constructor Set (ECS),
 * but simpler to get started with.
 * <p>
 * All methods are static for ease of use.
 */
public class Tag {
	protected static final char LB = '<';
	protected static final char RB = '>';
	protected static final char END = '/';

	/** Output an empty tag */
	public static void dotag(PrintWriter out, String tag) {
		startTag(out, tag);
		endTag(out, tag);
	}

	/** Output an body-content tag */
	public static void doTag(PrintWriter out, String tag, String content) {
		startTag(out, tag);
		out.println(content);
		endTag(out, tag);
	}

	/** Output a start tag */
	public static void startTag(PrintWriter out, String tag) {
		out.print(
			new StringBuffer(LB).append(tag).append(RB).toString());
	}

	/** Output an end tag */
	public static void endTag(PrintWriter out, String tag) {
		out.print(
			new StringBuffer(LB).append(END).append(tag).append(RB).toString());
	}
}
