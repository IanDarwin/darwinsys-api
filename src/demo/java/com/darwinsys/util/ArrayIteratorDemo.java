// BEGIN main
package com.darwinsys.util;

public class ArrayIteratorDemo {
		private final static String[] names = {
			"rose", "petunia", "tulip"
		};
		public static void main(String[] args) {
		ArrayIterator<String> arrayIterator = new ArrayIterator<>(names);
		for (String s : arrayIterator) {
			System.out.println(s);
		}
	}
}
// END main
