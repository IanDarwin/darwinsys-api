package com.darwinsys.util;

public class ArrayIteratorDemo {
		// BEGIN
		private final static String[] names = {
				"rose", "petunia", "tulip"
		};
		public static void main(String[] args) {
		ArrayIterator<String> ait = new ArrayIterator<>(names);
		for (String s : ait) {
			System.out.println(s);
		}
		// END
	}

}
