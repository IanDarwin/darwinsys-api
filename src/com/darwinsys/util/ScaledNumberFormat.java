import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

/**
 * Format numbers scaled for human comprehension.
 *
 * "Human-readable" output uses 3 digits max.--put unit suffixes at
 * the end.  Makes output compact and easy-to-read esp. on huge disks.
 * Formatting code was originally in OpenBSD "df", converted to library routine.
 * Scanning code written for OpenBSD libutil by Todd Miller.
 * 
 * Rewritten in Java in January, 2001.
 *
 * @author Ian F. Darwin, http://www.darwinsys.com/
 * @version $Id$
 */
public class ScaledNumberFormat extends Format {

	final static int NONE = 0;
	final static int KILO = 1;
	final static int MEGA = 2;
	final static int GIGA = 3;
	final static int TERA = 4;
	final static int PETA = 5;
	final static int EXA  = 6;

	DecimalFormat df5, df6;

	public ScaledNumberFormat() {
		df5 = new DecimalFormat("0");
		df5.setMinimumIntegerDigits(5);
		df6 = new DecimalFormat("0");
		df6.setMinimumIntegerDigits(6);
	}

	/** The input scaling factors. All three arrays must be in same order. */
	static char scale_chars[] = { 'B', 'K', 'M', 'G', 'T', 'P', 'E',  };
	/** The numeric scale values. All three arrays must be in same order. */
	int units[] = { NONE, KILO, MEGA, GIGA, TERA, PETA, EXA };
	/** The input scale sizes. All three arrays must be in same order. */
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
		for (i = 0; i < scale_factors.length; i++) {
			if (b[p] == scale_chars[i] ||
				b[p] == Character.toLowerCase(scale_chars[i])) {
				scale_fact = scale_factors[i];
				// scale whole part: easy
				whole *= scale_fact;
				// scale fractional part
				fpart *= scale_fact;
				for (i = 0; i < fract_digits - 1; i++)
					fpart /= 10;
				whole += fpart;
				return new Long(whole);
			}
		}
		throw new IllegalArgumentException("invalid scale factor " + b[p]);	
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
	 * This method is the REAL FORMATTING ENGINE.
	 */
	public String format(long number) {
		long fract = 0;
		int unit = NONE;

		StringBuffer buf = new StringBuffer();

		long abval = Math.abs(number);

		for (int i = 1/*!*/; i < scale_factors.length; i++) {
			if (abval < scale_factors[i]) {
				unit = units[i-1];
				fract = i == 1 ? 0 : abval % scale_factors[i-1];
				number /= scale_factors[i-1];
				break;
			}
		}
		// printf("AFTER: unit %d, number %d, fract %d\n", unit, number, fract);

		if (fract < 0)
			fract = -fract;

		/* scale fraction to one digit (truncate, not round) */
		while (fract>9)
			fract /= 10;

		if (number == 0)
			return "     0B";
		else if (number > 10) {
			buf.append(df6.format(number)).append(scale_chars[unit]);
		} else {
			buf.append(df5.format(number)).append('.').
				append(number>10?0:fract).append(scale_chars[unit]);
		}

		// replace leading zeros with spaces
		for (int i = 0; i < buf.length(); i++) {
			if (buf.charAt(i) == '0')
				buf.setCharAt(i, ' ');
			else
				break;
		}

		return buf.toString();
	}
}
