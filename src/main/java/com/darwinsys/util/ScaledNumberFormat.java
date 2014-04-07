package com.darwinsys.util;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;

/**
 * Format numbers scaled for human comprehension.
 *
 * "Human-readable" output uses43 digits max, and puts unit suffixes at the end.
 * Makes output compact and easy-to-read esp. on huge disks. Formatting code was
 * originally in OpenBSD "df", converted to library routine. Scanning code
 * written by Ian Darwin, originally for OpenBSD libutil.
 *
 * Rewritten in Java in January, 2001; re-synched with OpenBSD in 2004.
 *
 * @author Ian F. Darwin, http://www.darwinsys.com/
 */
public class ScaledNumberFormat extends Format {

	private static final long serialVersionUID = 1030043222640098114L;

	final static int NONE = 0; // to become an enum for 1.5

	final static int KILO = 1;

	final static int MEGA = 2;

	final static int GIGA = 3;

	final static int TERA = 4;

	final static int PETA = 5;

	final static int EXA = 6;

	DecimalFormat df;

	public ScaledNumberFormat() {
		df = new DecimalFormat("0");
	}

	/** The input scaling factors. All three arrays must be in same order. */
	static char scale_chars[] = { 'B', 'K', 'M', 'G', 'T', 'P', 'E', };

	/** The numeric scale values. All three arrays must be in same order. */
	int units[] = { NONE, KILO, MEGA, GIGA, TERA, PETA, EXA };

	/** The input scale sizes. All three arrays must be in same order. */
	static long scale_factors[] = { 1, 1024, 1048576L, 1073741824L,
			1099511627776L, 1125899906842624L, 1152921504606846976L };

	private final static long LLONG_MAX = scale_factors[6];

	/* To prevent numeric overflow (Java doesn't need a "long long") */
	static final int MAX_DIGITS = 10;

	/**
	 * Parse a String expected to contain a number in Human Scaled Form.
	 *
	 * @param s -
	 *            String to be parsed.
	 * @param where
	 *            Ignored - required by API
	 * @return a Long containing the value (value is always integral, even
	 *         though has a fractional part before scaling).
	 */
	public Object parseObject(String s, ParsePosition where) {
		int i, sign = 0, fract_digits = 0;
		long scale_fact = 1, whole = 0;
		long fpart = 0;

		if (s == null)
			return null;

		char[] b = s.trim().toCharArray();
		int p = 0; // the index into b; the # of chars we've scanned.

		/* Then at most one leading + or - */
		while (b[p] == '-' || b[p] == '+') {
			if (b[p] == '-') {
				if (sign != 0)
					throw new NumberFormatException("Number " + s
							+ " has more than one sign.");
				sign = -1;
				++p;
			} else if (b[p] == '+') {
				if (sign != 0)
					throw new NumberFormatException("Number " + s
							+ " has more than one sign.");
				sign = +1;
				++p;
			}
		}

		/*
		 * Main loop: Scan digits, find decimal point, if present. We don't
		 * allow exponentials, so no scientific notation (but note that E for
		 * Exa might look like e to some!). Advance 'p' to end, to get scale
		 * factor.
		 */
		for (; p < b.length && (Character.isDigit(b[p]) || b[p] == '.'); ++p) {
			int ndigits = 0;
			if (b[p] == '.') {
				if (fract_digits > 0) {
					throw new NumberFormatException("Number " + s
							+ " has more than one decimal point ");
				}
				fract_digits = 1;
				continue;
			}

			i = (b[p]) - '0'; // whew! finally a digit we can use
			if (fract_digits > 0) { // fractional digit
				if (fract_digits >= MAX_DIGITS)
					throw new NumberFormatException("Number too large");
				fpart *= 10;
				fpart += i;
				++fract_digits; // track for later scaling
			} else { // normal digit
				if (++ndigits >= MAX_DIGITS)
					throw new NumberFormatException("Number too large");
				whole *= 10;
				whole += i;
			}
		}
		/*
		 * printf("whole=%qd, fpart %ld, fract_digits %ld\n", whole, fpart,
		 * fract_digits);
		 */
		if (sign < 0) {
			whole *= sign;
			fpart *= sign;
		}

		/* If no scale factor given, we're done. fraction is discarded. */
		if (p >= b.length) {
			return new Long(whole);
		}

		/* Validate scale factor, and scale whole and fraction by it. */
		for (i = 0; i < scale_factors.length; i++) {
			if (b[p] == scale_chars[i]
					|| b[p] == Character.toLowerCase(scale_chars[i])) {
				// XXX if digits after this, throw exception
				scale_fact = scale_factors[i];
				// scale whole part: easy
				whole *= scale_fact;
				/*
				 * truncate fpart so it does't overflow. then scale fractional
				 * part.
				 */
				while (fpart >= LLONG_MAX / scale_fact) {
					fpart /= 10;
					fract_digits--;
				}
				fpart *= scale_fact;
				if (fract_digits > 0) {
					for (i = 0; i < fract_digits - 1; i++)
						fpart /= 10;
				}
				whole += fpart;
				return new Long(whole);
			}
		}
		throw new IllegalArgumentException("invalid scale factor " + b[p]);
	}

	/*
	 * Parse a String containing a Human Scaled Number.
	 *
	 * @see ScaledNumberFormat#parseObject(java.lang.String)
	 */
	public Object parseObject(String arg0) throws ParseException {
		return parseObject(arg0, null);
	}

	/*
	 * Format the given Number as a Scaled Numeral, returning the Stringbuffer
	 * (updated), and <em>ignoring</em> the FieldPosition. Method signature is
	 * overkill, but required as a subclass of Format.
	 */
	@Override
	public StringBuffer format(Object on, StringBuffer sb, FieldPosition fp) {
		if (on instanceof String) {
			String son = (String) on;
			if (son.length() == 0) {
				return sb.append("0B");
			}
			on = parseObject(son, null);
		}
		if (!(on instanceof Long)) {
			throw new IllegalArgumentException("Argument " + on
					+ " must be String or Long");
		}
		long n = ((Long) on).longValue();
		sb.append(format(n));
		return sb;
	}

	/**
	 * Non-standard overload:
	 * Format a double as a Scaled Numeral; just truncate to a long, and call
	 * format(long).
	 */
	public String format(double n) {
		return format((long) n);
	}

	/**
	 * Non-standard overload;
	 * Format a given long as a Scaled Numeral. This method is the REAL
	 * FORMATTING ENGINE.
	 */
	public String format(long number) {
		long fract = 0;
		int unit = NONE;

		StringBuffer buf = new StringBuffer();

		long abval = Math.abs(number);

		for (int i = 1/* ! */; i < scale_factors.length; i++) {
			if (abval < scale_factors[i]) {
				unit = units[i - 1];
				fract = i == 1 ? 0 : abval % scale_factors[i - 1];
				number /= scale_factors[i - 1];
				break;
			}
		}

		if (fract < 0)
			fract = -fract;

		/* scale fraction to one digit (truncate, not round) */
		while (fract > 9)
			fract /= 10;

		if (number == 0)
			return "0B";
		else if (unit == NONE || number >= 100 || number <= -100) {
			buf.append(df.format(number)).append(scale_chars[unit]);
		} else {
			buf.append(df.format(number)).append('.').append(fract).append(
					scale_chars[unit]);
		}

		return buf.toString();
	}
}
