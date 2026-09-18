package com.darwinsys.util;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;


/**
 * Ordinal formatted numbers: 1st, 2nd, 3rd, 4th, etc.
 * @author Ian Darwin
 */
public class OrdinalFormat extends NumberFormat {

	private static final long serialVersionUID = 3256727294604489521L;

	/** Format an int as a human-readable ordinal.
	 * @param iNum the number to be formatted
	 * @param sb The stringbuffer into which we format
	 * @param fp As you might expect, this value is ignored; required by inheritance
	 * @see java.text.NumberFormat#format(double, java.lang.StringBuffer, java.text.FieldPosition)
	 * @return The StringBuffer for fluent API use.
	 */
	// Not an override
	public StringBuffer format(final long iNum, final StringBuffer sb, final FieldPosition fp) {
		
		sb.append(iNum);
		if (iNum % 10 == 1) {
			sb.append("st");
		} else if (iNum % 10 == 2) {
			sb.append("nd");
		} else if (iNum % 10 == 3) {
			sb.append("rd");
		} else {
			sb.append("th");
		}
		fp.setIndex(fp.getIndex() + sb.length());
		return sb;
	}

	@Override
	public StringBuffer format(final double number, final StringBuffer sb,
			final FieldPosition fp) {
		return format((int)number, sb, fp);
	}

	/* Given a string like 42nd or 1st or 43768th, return it as an Integer.
	 * @see java.text.NumberFormat#parse(java.lang.String, java.text.ParsePosition)
	 */
	@Override
	public Number parse(String str, ParsePosition pos) {
		var ret = str.substring(pos.getIndex()).replaceFirst("[^\\d]+","");
		pos.setIndex(pos.getIndex() + ret.length());
		return Integer.parseInt(ret);
	}
}
