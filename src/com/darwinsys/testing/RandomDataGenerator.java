package com.darwinsys.testing;

import java.util.Random;

public class RandomDataGenerator {
	
	private final static Random r = new Random();
	private final static byte[] bytes = new byte[1];
	private static boolean b;
	
	public static Object getRandomValue(Class<?> t) {
		if (t == byte.class) {
			r.nextBytes(bytes);
			return bytes[0];
		}
		if (t == char.class) {
			return (char)r.nextInt();
		}
		if (t == short.class) {
			return (short)r.nextInt();
		}
		if (t == int.class) {
			return r.nextInt();
		}
		if (t == long.class) {
			return r.nextLong();
		}
		if (t == float.class) {
			return r.nextFloat();
		}
		if (t == double.class) {
			return r.nextGaussian();
		}
		if (t == boolean.class) {
			return b = !b;
		}
		if (t == String.class) {
			return Integer.toString(r.nextInt());
		}
		System.out.println("TestSettersGetters.getRandomValue() needs case for " + t);
		return null;
	}
}
