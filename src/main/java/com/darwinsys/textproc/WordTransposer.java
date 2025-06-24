package com.darwinsys.textproc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * Class to find pairs of words in a text file where one word can be formed
 * by transposing two adjacent letters of the other, and both words exist in the file.
 * @author Google Gemini, algorith specified by Ian Darwin
 */
public class WordTransposer {

    /**
     * Finds and prints pairs of words from a given text file.
     * A pair (word1, word2) is found if:
     * 1. Both word1 and word2 are present in the input file.
     * 2. Word2 can be formed by transposing two adjacent letters in word1.
     * 3. Both words have a length between the specified minimum and maximum length (inclusive).
     * 4. The transposed word is not identical to the original word (e.g., "hello" transposing 'll' to 'll').
     *
     * The algorithm uses a two-pass approach:
     * Pass 1: Reads all valid words from the file into a HashSet for quick lookups.
     * Pass 2: Iterates through each word in the HashSet, generates all possible
     * adjacent transpositions, and checks if the transposed word also
     * exists in the HashSet.
     * @author Algorithm and code improvements by Ian Darwin, initial coding by Google Gemini
     * @param filePath The path to the text file, where each line contains one word.
     * @param minLength The minimum allowed length for words to be considered.
     * @param maxLength The maximum allowed length for words to be considered.
     */
    public void findTransposedWordPairs(String filePath, int minLength, int maxLength) {
		if (minLength < 3) {
			throw new IllegalArgumentException("Minlength " + minLength + " is too small - must be >=3");
		}
        // Use a HashSet to store words from the file for O(1) average time lookups.
        Set<String> dictionary = new HashSet<>();
        // Use another HashSet to store pairs that have already been printed,
        // preventing duplicate output (e.g., "adze daze" and "daze adze").
        Set<String> processedPairs = new HashSet<>();

        // --- Pass 1: Load valid words into the dictionary ---
        try {
            Files.lines(Path.of(filePath))
                .filter(word -> word.length() >= minLength && word.length() <= maxLength)
				.forEach(dictionary::add); // Add to the dictionary
        } catch (IOException e) {
            throw new RuntimeException("Unable to read file " + filePath + " (" + e + ")");
        }

        // --- Pass 2: Find transposed pairs ---
        // Iterate through each word that was successfully loaded into our dictionary.
        for (String word : dictionary) {
            // Convert the word to a char array for easy in-place character swapping.
            char[] chars = word.toCharArray();

            // Iterate through all possible adjacent letter pairs to transpose.
            // The loop runs from the first character up to the second-to-last character,
            // as we need 'i' and 'i+1' for transposition.
            for (int i = 0; i < chars.length - 1; i++) {
                // Perform the transposition: swap chars[i] and chars[i+1].
                char temp = chars[i];
                chars[i] = chars[i + 1];
                chars[i + 1] = temp;

                // Create a new String from the modified char array, which is our transposed word.
                String transposedWord = new String(chars);

                // --- IMPORTANT: Swap back to restore the original word for the next iteration ---
                // This step is crucial. After creating 'transposedWord', we must revert the 'chars'
                // array to its original state for the current 'word' so that subsequent transpositions
                // (for different 'i' values within the same 'word') are based on the correct original word.
                temp = chars[i];
                chars[i] = chars[i + 1];
                chars[i + 1] = temp;

                // Check if:
                // 1. The generated transposed word exists in our dictionary.
                // 2. The transposed word is not identical to the original word (prevents "hello" -> "hello" due to 'll').
                if (dictionary.contains(transposedWord) && !word.equals(transposedWord)) {
                    // To avoid printing duplicate pairs (e.g., "adze daze" and then "daze adze" later
                    // when iterating on "daze"), we create a canonical representation of the pair.
                    String firstWord = word;
                    String secondWord = transposedWord;

                    // Ensure lexicographical order for the pair key (e.g., "adze daze" will always be
                    // the key, regardless of whether we started from "adze" or "daze").
                    if (firstWord.compareTo(secondWord) > 0) {
                        String tempStr = firstWord;
                        firstWord = secondWord;
                        secondWord = tempStr;
                    }

                    // Create a unique key for the pair (e.g., "word1 word2").
                    String pairKey = firstWord + " " + secondWord;

                    // If this pair hasn't been processed/printed yet, print it and add its key to the processed set.
                    if (!processedPairs.contains(pairKey)) {
                        System.out.println(word + " " + transposedWord); // Print the pair in the order it was found.
                        processedPairs.add(pairKey); // Add the canonical key to the processed set.
                    }
                }
            }
        }
    }

    /**
     * Minimal main method to illustrate how to instantiate and potentially use the class.
     * For comprehensive testing, refer to the WordTransposerTest.java file.
     *
     * @param args Command line arguments (not used in this example).
     */
    public static void main(String[] args) {
        WordTransposer transposer = new WordTransposer();
		switch(args.length) {
		case 0:
			transposer.findTransposedWordPairs("/usr/share/dict/words", 4, 10);
			break;
		case 3:
			transposer.findTransposedWordPairs(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
			break;
		default:
			System.err.println("Usage: WordTransposer filename minLength maxLength");
			System.exit(0);
		}
    }
}
