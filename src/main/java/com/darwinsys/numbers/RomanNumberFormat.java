package com.darwinsys.numbers;

import java.io.Serial;
import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 * Roman Number class. Not localized, since "Latin's a Dead Dead Language..."
 * and we don't display Roman Numbers differently in different Locales.
 * Filled with quick-n-dirty algorithms.
 *
 * @author Ian F. Darwin, <a href="https://darwinsys.com/">DarwinSys.com</a>
 */
// tag::main[]
public class RomanNumberFormat extends Format {

	@Serial
	private static final long serialVersionUID = -2303809319102357783L;
	
	/** Characters used in "Arabic to Roman", that is, format() methods. */
	final static char[][] A2R = {
			{ 0, 'M' },
			{ 0, 'C', 'D', 'M' },
			{ 0, 'X', 'L', 'C' },
			{ 0, 'I', 'V', 'X' },
	};

	record R2A(char ch, int amount) {}
	
	final static R2A[] R2A = {
		new R2A('M', 1000),
		new R2A('D', 500),
		new R2A('C', 100),
		new R2A('L', 50),
		new R2A('X', 10),
		new R2A('V', 5),
		new R2A('I', 1),
	};

	/** Format a given double as a Roman Numeral; just truncate to a
	 * long, and call format(long).
	 */
	// Not an override
	public String format(double n) {
		return format((long)n);
	}

	/** Format a given long as a Roman Numeral. Just call the
	 * three-argument form.
	 */
	// Not an override
	public String format(long n) {
		StringBuffer sb = new StringBuffer();
		format((int) n, sb,
			new FieldPosition(NumberFormat.INTEGER_FIELD));
		return sb.toString();
	}

	/* Format the given Number as a Roman Numeral, returning the
	 * StringBuilder (updated), and updating the FieldPosition.
	 * This method is the REAL FORMATTING ENGINE.
	 * Method signature is overkill, but required as a subclass of Format.
	 */
	@Override
	public StringBuffer format(Object on, StringBuffer sb, FieldPosition fp) {
		if (!(on instanceof Number))
			throw new IllegalArgumentException(on + " must be a Number object");
		if (fp.getField() != NumberFormat.INTEGER_FIELD)
			throw new IllegalArgumentException(
			fp + " must be FieldPosition(NumberFormat.INTEGER_FIELD");
		int n = ((Number)on).intValue();	// TODO: check in range.
		if (n <= 0 || n >= 4000) {
			throw new NumberFormatException(n + " must be > 0 && < 4000");
		}

		// First, put the digits on a tiny stack. Must be 4 digits.
		for (int i=0; i<4; i++) {
			int d=n%10;
			push(d);
			// System.out.println("Pushed " + d);
			n=n/10;
		}

		// Now pop and convert.
		for (int i=0; i<4; i++) {
			int ch = pop();
			// System.out.println("Popped " + ch);
			if (ch==0)
				continue;
			else if (ch <= 3) {
				for(int k=1; k<=ch; k++)
					sb.append(A2R[i][1]); // I
			}
			else if (ch == 4) {
				sb.append(A2R[i][1]);	// I
				sb.append(A2R[i][2]);	// V
			}
			else if (ch == 5) {
				sb.append(A2R[i][2]);	// V
			}
			else if (ch <= 8) {
				sb.append(A2R[i][2]);	// V
				for (int k=6; k<=ch; k++)
					sb.append(A2R[i][1]);	// I
			}
			else { // 9
				sb.append(A2R[i][1]);
				sb.append(A2R[i][3]);
			}
		}
		return sb;
	}

	@Override
	public Object parseObject(String what) {
		return parseObject(what, null);
	}

	/**
	 * Parse a generic object, returning an Object.
	 */
	@Override
	public Object parseObject(String what, ParsePosition where) {
		int n = 0;
		final char[] chars = what.toUpperCase().toCharArray();
		for (int i = 0; i < chars.length; i++) {
			R2A cur = getR2A(chars[i]);
			if (i < chars.length - 1) {
				var next = getR2A(chars[i + 1]);
				if (cur.amount < next.amount) {
					n += next.amount - cur.amount;
					++i;
				} else {
					n += cur.amount;
				}
			} else {
				n += cur.amount;
			}

		}
		return Long.valueOf(n);
	}

	private R2A getR2A(char ch) {
		for (R2A r : R2A) {
			if (r.ch == ch)
				return r;
		}
		throw new IllegalArgumentException("Character " + ch + " not valid");
	}

	/* Implement a toy stack */
	protected int stack[] = new int[10];
	protected int depth = 0;

	/* Implement a toy stack */
	protected void push(int n) {
		stack[depth++] = n;
	}
	/* Implement a toy stack */
	protected int pop() {
		return stack[--depth];
	}
}
// end::main[]
