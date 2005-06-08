package com.darwinsys.util;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;


/**
 * Ordinal formatted numbers: 1st, 2nd, 3rd, 4th, etc.
 * @author ian
 *
 */
public class OrdinalFormat extends NumberFormat {

	private static final long serialVersionUID = 3256727294604489521L;

	/** Format the ordinal.
	 * @see java.text.NumberFormat#format(double, java.lang.StringBuffer, java.text.FieldPosition)
	 */
	public StringBuffer format(final int iNum, final StringBuffer sb,
			final FieldPosition ignored) {
		
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
		return sb;
	}

	/* (non-Javadoc)
	 * @see java.text.NumberFormat#format(long, java.lang.StringBuffer, java.text.FieldPosition)
	 */
	public StringBuffer format(long number, StringBuffer sb, FieldPosition fp) {
		
		return format((int)number, sb, fp);
	}
	
	public StringBuffer format(final double number, final StringBuffer sb,
			final FieldPosition fp) {
		return format((int)number, sb, fp);
	}

	/* Given a string like 42nd or 1st or 43768th, return it as an Integer.
	 * @see java.text.NumberFormat#parse(java.lang.String, java.text.ParsePosition)
	 */
	public Number parse(String arg0, ParsePosition arg1) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arg0.length(); i++) {
			if (!Character.isDigit(arg0.charAt(i))) {
				break;
			}	
		}
		return Integer.valueOf(sb.toString());
	}
}
