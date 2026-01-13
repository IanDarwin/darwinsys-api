package com.darwinsys.textproc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A utility class to find unique permutations of a given string,
 * useful for finding anagrams.
 * @author Partly written by Google Gemini
 */
public class AnagramFinder {

	final static String DICT = "/usr/share/dict/words";
	static List<String> dictWords;
	static boolean onlyWords;

	static {
		try {
			dictWords = Files.readAllLines(Path.of(DICT));
		} catch (IOException ex) {
			throw new ExceptionInInitializerError(
				"Can't load dict file " + DICT + ": " + ex);
		}
	}
		
	AnagramFinder() {
		this(false);
	}
	AnagramFinder(boolean onlyWords) {
		this.onlyWords = onlyWords;
	}

	public static void main(String[] args) { 
		if (args.length == 0) {
			System.out.println("Usage: AnagramFinder word [...]");
			System.exit(1);
		}
		for (String word : args) {
			System.out.println("Unique permutations for \"" + word + "\":");
			Set<String> permuts = findUniquePermutations(word);
			printPermutations(permuts);
		}
	}

	/**
	 * Find all unique permutations of the letters in the given word.
	 * This method handles words with duplicate letters correctly,
	 * ensuring that each unique permutation is printed only once.
	 *
	 * @param word The input string for which to find unique permutations.
	 */
	public static Set<String> findUniquePermutations(String word) {
		if (word == null || word.isEmpty()) {
			throw new IllegalArgumentException("Input word cannot be null or empty.");
		}

		// Convert the word to a character array for easier manipulation
		char[] chars = word.toCharArray();
		// Sort the character array. This is crucial for handling duplicates
		// and ensuring the algorithm correctly skips redundant permutations.
		Arrays.sort(chars);

		// Use a Set to store unique permutations to avoid duplicates in the output.
		Set<String> uniquePermutations = new HashSet<>();
		// A boolean array to keep track of which characters have been used in the current permutation.
		boolean[] used = new boolean[chars.length];
		// A StringBuilder to build the current permutation.
		StringBuilder currentPermutation = new StringBuilder();

		// Start the recursive backtracking process.
		findPermutationsRecursive(chars, used, currentPermutation, uniquePermutations, onlyWords);

		// Return found unique permutations.
		return uniquePermutations;
	}

	public static void printPermutations(Set<String> uniquePermutations) {
		if (uniquePermutations.isEmpty()) {
			System.out.println("No unique permutations found (this should not happen for non-empty strings).");
		} else {
			for (String perm : uniquePermutations) {
				System.out.println(perm);
			}
		}
	}

	/**
	 * Recursive helper method to find all unique permutations.
	 * This method uses a backtracking approach.
	 *
	 * @param chars The sorted character array of the input word.
	 * @param used A boolean array indicating whether a character at a given index has been used.
	 * @param currentPermutation The StringBuilder holding the permutation being built.
	 * @param uniquePermutations The Set to store all unique permutations found.
	 */
	private static void findPermutationsRecursive(
			char[] chars, boolean[] used,
			StringBuilder currentPermutation,
			Set<String> uniquePermutations,
			boolean onlyWords) {

		// Base case: If the current permutation has the same length as the original word,
		// it means a complete permutation has been formed.
		if (currentPermutation.length() == chars.length) {
			var str = currentPermutation.toString();
			if (dictWords.contains(str)) {
				uniquePermutations.add(str);
			}
			return;
		}

		// Iterate through all characters in the sorted array.
		for (int i = 0; i < chars.length; i++) {
			// Skip if the character is already used.
			if (used[i]) {
				continue;
			}

			// Handle duplicates: If the current character is the same as the previous one,
			// and the previous one was not used (meaning it was skipped in the previous iteration
			// of the loop for this level of recursion), then skipping the current character
			// will avoid duplicate permutations.
			// This condition is crucial because the array is sorted.
			if (i > 0 && chars[i] == chars[i - 1] && !used[i - 1]) {
				continue;
			}

			// Mark the character as used.
			used[i] = true;
			// Append the character to the current permutation.
			currentPermutation.append(chars[i]);

			// Recursively call the function to build the next part of the permutation.
			findPermutationsRecursive(chars, used, currentPermutation, uniquePermutations, onlyWords);

			// Backtrack: Remove the last appended character to explore other possibilities.
			currentPermutation.deleteCharAt(currentPermutation.length() - 1);
			// Mark the character as unused for subsequent permutations.
			used[i] = false;
		}
	}
}

