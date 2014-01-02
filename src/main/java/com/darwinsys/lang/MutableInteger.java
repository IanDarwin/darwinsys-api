// BEGIN main
package com.darwinsys.lang;

/** A MutableInteger is like an Integer but mutable, to avoid the
 * excess object creation involved in 
 * c = new Integer(c.getInt()+1)
 * which can get expensive if done a lot.
 * Not subclassed from Integer, since Integer is final (for performance :-))
 */
public class MutableInteger {
	private int value = 0;

	public MutableInteger(int i) {
		value = i;
	}
	
	public MutableInteger() {
		this(0);
	}

	public int incr() {
		value++;
		return value;
	}

	public int incr(int amt) {
		value += amt;
		return value;
	}

	public int decr() {
		value--;
		return value;
	}

	public int setValue(int i) {
		value = i;
		return value;
	}

	public int getValue() {
		return value;
	}

	public String toString() {
		return Integer.toString(value);
	}

	public static String toString(int val) {
		return Integer.toString(val);
	}

	public static int parseInt(String str) {
		return Integer.parseInt(str);
	}
}
// END main
