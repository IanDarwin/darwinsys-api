package com.darwinsys.diff;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.darwinsys.diff.Diff.Item;

class DiffPrintTest {

	/** The input - "left" or old file */
	String[] dataOld = {
			"one",
			"two",
			"three",
			"four",
			"five",
			"six",
	};

	/** The input - "right" or new file */
	String[] dataNew = {
			"one",
			"three",
			"four",
			"funf",
			"six",
			"heaven",
	};

	/** The output - what diff(1) would produce */
	String[] expected = {
			"2d1",
			"< two",
			"5c4",
			"< five",
			"---",
			"> funf",
			"6a6",
			"> heaven",
	};

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void test() {
		final Item[] diffItems = Diff.diffText(arrayToString(dataOld), arrayToString(dataNew));
		List<String> output = new ArrayList<>();
		DiffPrint.diffPrint(diffItems, dataOld, dataNew, output);
		assertEquals(Arrays.asList(expected), output);
	}

	private String arrayToString(String[] data) {
		StringBuilder sb = new StringBuilder();
		for (String s : data) {
			sb.append(s).append("\n");
		}
		return sb.toString();
	}

}
