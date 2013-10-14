package com.darwinsys.util;

import java.util.List;


/**
 * Polymorphic forms of some standard Math functions.
 * See http://www.diasparsoftware.com/weblog/archives/00000100.html.
 */
public class MiniMax {

	public static int max(int[] data) {
		int v = Integer.MIN_VALUE;
		for (int i : data) {
			if (i > v) {
				v = i;
			}
		}
		return v;
	}

	public static Integer max(List<Integer> data) {
		Integer v = Integer.MIN_VALUE;
		for (Integer i : data) {
			if (i > v) {
				v = i;
			}
		}
		return v;
	}

	public static Integer max(Integer[] data) {
		Integer v = Integer.MIN_VALUE;
		for (Integer i : data) {
			if (i > v) {
				v = i;
			}
		}
		return v;
	}
}
