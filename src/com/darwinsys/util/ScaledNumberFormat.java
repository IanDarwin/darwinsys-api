import java.text.*;
import java.util.*;

/**
 * Format numbers scaled for human comprehension.
 *
 * "Human-readable" output uses 3 digits max.--put unit suffixes at
 * the end.  Makes output compact and easy-to-read esp. on huge disks.
 * Formatting code was originally in OpenBSD "df", converted to library routine.
 * Scanning code written for OpenBSD libutil.
 * 
 * Rewritten in Java in January, 2001.
 *
 * @author Ian F. Darwin, ian@darwinsys.com
 * @version $Id$
 */
public class ScaledNumberFormat extends Format {

	final static int NONE = 0;
	final static int KILO = 1;
	final static int MEGA = 2;
	final static int GIGA = 3;
	final static int TERA = 4;
	final static int PETA = 5;
	/* final static int EXA  = 6; */

	/** The input scaling factors. Must be in same order as scale_factors. */
	static char scale_chars[] = { 'B', 'K', 'M', 'G', 'T', 'P', 'E',  };
	/** The input scale sizes. Must be in same order as scale_chars. */
	static long scale_factors[] = {
		1,
		1024,
		1048576L,
		1073741824L,
		1099511627776L,
		1125899906842624L,
		1152921504606846976L
	};


	/* To prevent numeric overflow (Java doesn't yet have "long long") */
	static final int MAX_DIGITS = 10;

	/** Parse a generic object, returning an Object */
	public Object parseObject(String s, ParsePosition where) {
		int i, neg = +1, fract_digits = 0;
		long scale_fact = 1, whole = 0;
		long fpart = 0;

		if (s == null)
			return null;

		char[] b = s.trim().toCharArray();
		int p = 0;	// the index into b; the # of chars we've scanned.
		
		/* Main loop: Scan digits, find sign & decimal point, if present.
		 * We don't allow exponentials, so no e E f F g G etc.
		 * Advance 'p' to end, to get scale factor. *
		 */
		if (b[p] == '-') {
			neg = -1;
			++p;
		} else if (b[p] == '+') {
			/* nothing much to do */
			++p;
		}
		for (; p<b.length && (Character.isDigit(b[p]) || b[p]=='.'); ++p) {
			if (b[p] == '.') {
				if (fract_digits>0) {
					throw new NumberFormatException(
					"Number " + s + " has more than one decimal point ");
				}
				fract_digits = 1;
				continue;
			}
			if (fract_digits==0 && p > MAX_DIGITS) {
				throw new IllegalArgumentException("Number too large");
			}
			i = (b[p]) - '0';			// whew! finally a digit we can use
			if (fract_digits > 0) {
				fpart *= 10;
				fpart += i;
				++fract_digits;		// track for later scaling
			} else {
				whole *= 10;
				whole += i;
			}
		}
		/* printf("whole=%qd, fpart %ld, fract_digits %ld\n",
		 *	whole, fpart, fract_digits);
		 */
		whole *= neg;
		fpart *= neg;

		/* If no scale factor given, we're done. fraction is discarded. */
		if (p >= b.length) {
			return new Long(whole);
		}

		/* Validate scale factor, and scale whole and fraction by it. */
		for (i=0; i<scale_factors.length; i++) {
			if (b[p] == scale_chars[i] ||
				b[p] == Character.toLowerCase(scale_chars[i])) {
				scale_fact = scale_factors[i];
				// scale whole part: easy
				whole *= scale_fact;
				// scale fractional part
				fpart *= scale_fact;
				for (i=0; i<fract_digits-1; i++)
					fpart /= 10;
				whole += fpart;
				return new Long(whole);
			}
		}
		throw new IllegalArgumentException("invalid scale factor " + b[p]);	
	}

	class ValAndFract {	
		long value;
		long fract;
		ValAndFract(long v, long f) {
			value = v; fract = f;
		}
	}

	/* Adjust units. Used by fmt, not by scan */
	private int unit_adjust(ValAndFract vaf) {
		long abval;
		int unit = NONE;

		abval = Math.abs(vaf.value);
		if (abval < 1024) {
			unit = NONE;
			vaf.fract = 0;
		} else if (abval < 1048576L) {
			unit = KILO;
			vaf.fract = vaf.value % 1024;
			vaf.value /= 1024;
		} else if (abval < 1073741824L) {
			unit = MEGA;
			vaf.fract = vaf.value % 1048576;
			vaf.value /= 1048576;
		} else if (abval < 1099511627776L) {
			unit = GIGA;
			vaf.fract = vaf.value % 1073741824L;
			vaf.value /= 1073741824L;
		} else if (abval < 1125899906842624L) {
			unit = TERA;
			vaf.fract = vaf.value % 1099511627776L;
			vaf.value /= 1099511627776L;
		} else if (abval < 1152921504606846976L) {
			unit = PETA;
			vaf.fract = vaf.value % 1152921504606846976L;
			vaf.value /= 1125899906842624L;
		}
		return (unit);
	}


	/* Format the given Number as a Scaled Numeral, returning the
	 * Stringbuffer (updated), and updating the FieldPosition.
	 * Method signature is overkill, but required as a subclass of Format.
	 */
	public StringBuffer format(Object on, StringBuffer sb, FieldPosition fp) {
		long n = 0 /* ((Number)on).getLongValue(); */;
		sb.append(format(n));
		return sb;
	}

	/** Format a double as a Scaled Numeral; just truncate to a
	 * long, and call format(long).
	 */
	public String format(double n) {
		return format((long)n);
	}

	/** Format a given long as a Scaled Numeral.
	 * This method is the REAL FORMATTING ENGINE. Shouldn't be.
	 */
	public String format(long number) {
		long fract = 0;
		int unit;

		StringBuffer buf = new StringBuffer();

		// printf("BEFORE: number is %d, ", number);
		ValAndFract vaf = new ValAndFract(number, fract);
		unit = unit_adjust(vaf);
		number = vaf.value;
		fract = vaf.fract;
		// printf("AFTER: unit %d, number %d, fract %d\n", unit, number, fract);

		if (fract < 0)
			fract = -fract;

		/* scale fraction to one digit (truncate, not round) */
		while (fract>9)
			fract /= 10;

		if (number == 0)
			return "     0B";
		else if (number > 10)
			buf.append(df6.format(number)).append(scale_chars[unit]);
		else
			buf.append(df5.format(number)).append('.').
				append(number>10?0:fract).append(scale_chars[unit]);

		return buf.toString();
	}
	DecimalFormat df6 = new DecimalFormat("#####0");
	DecimalFormat df5 = new DecimalFormat("####0");
}
