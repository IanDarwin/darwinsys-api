package com.darwinsys.diff;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.darwinsys.diff.Diff.Item;

/**
 * This set of tests came with the original program. Don't ask me any questions about it.
 */
class DiffTest {

	/**
	 * The output of this method is not intended to be generally useful, only
	 * to make a representation of the Diff.Item list that is easily
	 * comparable in calls to assertEquals()
	 * @param f The array of Diff.Item objects
	 * @return A single string representation of the input array
	 */
	private String diffsToString(Diff.Item[] f) {
		StringBuilder ret = new StringBuilder();
		for (int n = 0; n < f.length; n++) {
			ret.append(f[n].deletedA + "." + f[n].insertedB + "." + f[n].startA
					+ "." + f[n].startB + "*");
		}
		return (ret.toString());
	}

	@Test
	void selfTestDiffsToString() {
		Item input = new Item();
		input.startA = 42;
		input.startB = 51;
		input.deletedA = 33;
		input.insertedB = 24;
		Diff.Item[] diffs = { input  };
		assertEquals("33.24.42.51*", diffsToString(diffs));
	}

	@Test
	void allDifferent() {
		String a = "a\nb\nc\nd\ne\nf\ng\nh\ni\nj\nk\nl\n";
		String b = "0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n";
		assertEquals(
			"12.10.0.0*", diffsToString(Diff.diffText(a, b, false, false, false)),
			"all-changes test");
	}

	@Test
	void allSame() {
		String a = "a\nb\nc\nd\ne\nf\ng\nh\ni\nj\nk\nl\n";
		String b = a;
		assertEquals(
			"", diffsToString(Diff.diffText(a, b, false, false, false)),
			"all-same test");
	}

	@Test
	void snake() {
		String a = "a\nb\nc\nd\ne\nf\n";
		String b = "b\nc\nd\ne\nf\nx\n";
		assertEquals(
			"1.0.0.0*0.1.6.5*", diffsToString(Diff.diffText(a, b, false, false, false)),
			"snake test");
	}

	@Test
	void repro() {
		String a = "c1\na\nc2\nb\nc\nd\ne\ng\nh\ni\nj\nc3\nk\nl";
		String b = "C1\na\nC2\nb\nc\nd\ne\nI1\ne\ng\nh\ni\nj\nC3\nk\nI2\nl";
		assertEquals(
			"1.1.0.0*1.1.2.2*0.2.7.7*1.1.11.13*0.1.13.15*", diffsToString(Diff.diffText(a, b, false, false, false)),
			"repro20020920 test");
	}

	@Test
	void repro20030207() {
		// 2003.02.07 - repro
		String a = "F";
		String b = "0\nF\n1\n2\n3\n4\n5\n6\n7";
		assertEquals(
			"0.1.0.0*0.7.1.2*", diffsToString(Diff.diffText(a, b, false, false, false)),
			"repro20030207 test");
	}

	// Is it the test or the code that's wrong? I think the test...
	@Test
	@Disabled("BROKEN")
	void muegelRepro() {
		String a = "HELLO\nWORLD";
		String b = "\n\nhello\n\n\n\nworld\n";
		assertEquals(
			"2.8.0.0*", diffsToString(Diff.diffText(a, b, false, false, false)),
			"repro20030409 test");
	}

	@Test
	void someDiffs() {
		String a = "a\nb\n-\nc\nd\ne\nf\nf";
		String b = "a\nb\nx\nc\ne\nf";
		assertEquals(
			"1.1.2.2*1.0.4.4*1.0.7.6*", diffsToString(Diff.diffText(a, b, false, false, false)),
			"some-changes test");
	}

	@Test
	void oneChangeWithinLongChainOfRepeats() {
		String a = "a\na\na\na\na\na\na\na\na\na\n";
		String b = "a\na\na\na\n-\na\na\na\na\na\n";
		assertEquals(
			"0.1.4.4*1.0.9.10*", diffsToString(Diff.diffText(a, b, false, false, false)),
			"long chain of repeats test");
	}
}
