package com.darwinsys.diff;

import java.util.List;

import com.darwinsys.diff.DiffType;
import com.darwinsys.diff.Diff.Item;

/**
 * None of the many people who asked me about this Diff program ever sent me back a
 * decent print routine, which I'm sure some of them must have written, so eventually
 * I felt I had to write my own. The moreso since I actually needed it for diffing
 * recipe versions on https://androidcookbook.com/
 * @author Ian Darwin
 */
public class DiffPrint {
	public List<String> diffList;

	public DiffPrint() {
	}
	
	/** 
	 * Format some Diff.Item objects for printing.
	 * Format as much as possible like regular diff(1)
	 * The Diff.Item class is just:
	 * public static class Item {
     * 	// Start Line number in Data A.
     * 	public int startA;
     * 	// Start Line number in Data B.
     * 	public int startB;
     * 	//Number of changes in Data A.
     * 	public int deletedA;
     * 	//Number of changes in Data B.
     * 	public int insertedB;
     * }
	 * @param diffItems The array of Diff.Item
	 * @param sOld The old file, as an array of String
	 * @param sNew The new file, as an array of String
	 * @param output The list of differences in textual form.
	 */
	public static void diffPrint(final Item[] diffItems, String[] sOld, String[] sNew, List<String> output) {
		for (Item diff : diffItems) {
			DiffType type;
			if (diff.deletedA != 0 && diff.insertedB != 0) { // i.e., both
				type = DiffType.CHANGED;
			} else if (diff.deletedA != 0) {
				type = DiffType.DELETE_ONLY;
			} else {
				type = DiffType.INSERT_ONLY;
			}
			switch (type) {
			case CHANGED:
				output.add((diff.startA + diff.deletedA) + "c" + diff.startA);
				for (int i = diff.startA; i < diff.startA + diff.deletedA; i++) {
					output.add("< " + sOld[i]);
				}
				output.add("---");
				for (int i = diff.startB; i < diff.startB + diff.insertedB; i++) {
					output.add("> " + sNew[i]);
				}
				break;
			case DELETE_ONLY:
				output.add((diff.startA + diff.deletedA) + "d" + diff.startA);
				for (int i = diff.startA; i < diff.startA + diff.deletedA; i++) {
					output.add("< " + sOld[i]);
				}
				break;
			case INSERT_ONLY:
				output.add(diff.startA + "a" + diff.startA);
				for (int i = diff.startB; i < diff.startB + diff.insertedB; i++) {
					output.add("> " + sNew[i]);
				}
				break;
			}
		}
	}
	
	private static String printRange(int start, int end) {
		if (start == end) {
			return Integer.toString(start);
		}
		return start + "," + end;
	}
}
