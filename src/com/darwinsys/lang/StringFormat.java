package com.darwinsys.lang;

import java.text.*;

/** Bare-minimum "String formatter": format a string to a given
 * maximum length with left, centre, or right justification.
 */
public class StringFormat extends Format {
	/* Constant for left justification. */
	public static final int JUST_LEFT = 'l';
	/* Constant for centering. */
	public static final int JUST_CENTRE = 'c';
	/* Constant for centering, for those who spell "centre" the American way. */
	public static final int JUST_CENTER = JUST_CENTRE;
	/** Constant for right-justified Strings. */
	public static final int JUST_RIGHT = 'r';

	/** Current justification */
	private int just;
	/** Current max length */
	private int maxChars;

	public StringFormat(int maxCh, int justn) {
		switch(justn) {
		case JUST_LEFT:
		case JUST_CENTRE:
		case JUST_RIGHT:
			this.just = justn;
			break;
		default:
			throw new IllegalArgumentException("invalid justification arg.");
		}
		if (maxCh < 1) {
			throw new IllegalArgumentException("maxChars must be positive.");
		}
		this.maxChars = maxCh;
	}

	/** Format a String */
	public StringBuffer format(
		Object obj, StringBuffer where, FieldPosition ignore)  {

		String s = (String)obj;
		String wanted = s.substring(0, Math.min(s.length(), maxChars));

		// If no space left for justification, return maxChars' worth */
		if (wanted.length() > maxChars) {
			where.append(wanted);
		}
		// Else get the spaces in the right place.
		else switch (just) {
			case JUST_RIGHT:
				pad(where, maxChars - wanted.length());
				where.append(wanted);
				break;
			case JUST_CENTRE:
				int startPos = where.length();
				pad(where, (maxChars - wanted.length())/2);
				where.append(wanted);
				pad(where, (maxChars - wanted.length())/2);
				// Adjust for "rounding error"
				pad(where, maxChars - (where.length() - startPos));
				break;
			case JUST_LEFT:
				where.append(wanted);
				pad(where, maxChars - wanted.length());
				break;
			}
		return where;
	}

	protected final void pad(StringBuffer to, int howMany) {
		for (int i=0; i<howMany; i++)
			to.append(' ');
	}

	/** Convenience Routine */
	String format(String s) {
		return format(s, new StringBuffer(), null).toString();
	}

	/** ParseObject is required by Format interface, 
	 *but not useful here.
	 */
	public Object parseObject (String source, ParsePosition pos)  {
		return source;
	}
}
