package com.darwinsys.textproc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JUnit 5 test class for the WordTransposer.
 * It tests various scenarios including valid transpositions, length filtering,
 * and handling of identical words after transposition.
 */
public class WordTransposerTest {

	// Test target / subject
	WordTransposer transposer;

    // Path to the temporary file used for tests.
    private Path tempFile;
    // To capture System.out.println output for verification in tests.
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    /**
     * Sets up the test environment before each test method runs.
     * This includes creating a temporary file and redirecting System.out.
     */
    @BeforeEach
    void setUp() throws IOException {
		// Create the test target
        transposer = new WordTransposer();
        // Create a unique temporary file for each test, ensuring no interference between tests.
        tempFile = Files.createTempFile("test_words_", ".txt");
        // Redirect standard output to capture what WordTransposer prints.
        System.setOut(new PrintStream(outContent));
    }

    /**
     * Cleans up the test environment after each test method runs.
     * This includes deleting the temporary file and restoring System.out.
     */
    @AfterEach
    void tearDown() throws IOException {
        // Delete the temporary file. Files.deleteIfExists handles cases where it might already be gone.
        Files.deleteIfExists(tempFile);
        // Restore standard output to its original stream.
        System.setOut(originalOut);
    }

    /**
     * Helper method to write content to the temporary file.
     * @param content The string content to write to the file.
     * @throws IOException If an I/O error occurs during writing.
     */
    private void writeToFile(String content) throws IOException {
        Files.write(tempFile, content.getBytes());
    }

    /**
     * Helper method to parse captured output into a set of sorted strings,
     * allowing for order-independent comparison.
     * Each line of output is considered a pair. The words within each pair
     * are sorted lexicographically to create a canonical representation (e.g., "adze daze").
     *
     * @param output The raw string output captured from System.out.
     * @return A Set of canonical pair strings.
     */
    private Set<String> parseOutput(String output) {
        Set<String> result = new HashSet<>();
        // Split the output by new line and filter out empty lines.
        for (String line : output.trim().split("\\r?\\n")) {
            if (!line.isEmpty()) {
                // Split each line into two words.
                String[] words = line.split(" ");
                if (words.length == 2) {
                    // Sort the words lexicographically to create a canonical representation of the pair.
                    Arrays.sort(words);
                    result.add(words[0] + " " + words[1]);
                }
            }
        }
        return result;
    }

    @Test
    void testBasicTranspositions() throws IOException {
        // Test case with standard transpositions: adze/daze, satin/stain
        String content = "adze\ndaze\nsatin\nstain\n";
        writeToFile(content);
        transposer.findTransposedWordPairs(tempFile.toString(), 3, 10);

        Set<String> expected = new HashSet<>();
        expected.add("adze daze");
        expected.add("satin stain");

        // Assert that the captured output matches the expected set of pairs.
        assertEquals(expected, parseOutput(outContent.toString()));
    }

    @Test
    void testNoTranspositions() throws IOException {
        // Test case where no transposable pairs exist.
        String content = "apple\norange\nbanana\npear\n";
        writeToFile(content);
        transposer.findTransposedWordPairs(tempFile.toString(), 3, 10);

        // Expect no output.
        assertTrue(outContent.toString().trim().isEmpty());
    }

    @Test
    void testMinLengthFiltering() throws IOException {
        // Words shorter than 3 should be ignored.
        String content = "an\na\nabc\nbac\n"; // "an", "a" are too short. "abc/bac" should be found.
        writeToFile(content);
        transposer.findTransposedWordPairs(tempFile.toString(), 3, 10); // MaxLength high enough for abc/bac

        Set<String> expected = new HashSet<>();
        expected.add("abc bac"); // Only this pair should be found.

        assertEquals(expected, parseOutput(outContent.toString()));
    }

    @Test
    void testMaxLengthFiltering() throws IOException {
        // Words longer than maxLength should be ignored.
        String content = "elephant\nelephnat\napple\npaple\nlongword\n";
        writeToFile(content);

        // Test with maxLength 6: apple/paple should be found, elephant/elephnat should be ignored.
        transposer.findTransposedWordPairs(tempFile.toString(), 3, 6);
        Set<String> expected6 = new HashSet<>();
        expected6.add("apple paple");
        assertEquals(expected6, parseOutput(outContent.toString()));

        // Clear output for next test.
        outContent.reset();

        // Test with maxLength 10: elephant/elephnat should now be found as well.
        transposer.findTransposedWordPairs(tempFile.toString(), 3, 10);
        Set<String> expected10 = new HashSet<>();
        expected10.add("apple paple");
        expected10.add("elephant elephnat");
        assertEquals(expected10, parseOutput(outContent.toString()));
    }


    @Test
    void testIdenticalWordsAfterTransposition() throws IOException {
        // Test that "hello" (l<->l) does not produce output, but "olleh" (l<->e) does.
        String content = "hello\nolleh\nhlelo\n";
        writeToFile(content);
        transposer.findTransposedWordPairs(tempFile.toString(), 3, 10);

        Set<String> expected = new HashSet<>();
        expected.add("hello hlelo"); // 'hloel' won't be in dictionary
        // "olleh" transposing 'l' and 'l' is 'olleh', filtered out.
        // "olleh" transposing 'o' and 'l' -> 'loleh'. If present.
        // "olleh" transposing 'e' and 'h' -> 'ollhe'. If present.
        // The original code only swaps adjacent characters.
        // olleh -> loleh (o,l)
        // olleh -> oel lh (l,l) filtered
        // olleh -> olh el (l,e) -> olelh. If present.
        // olleh -> olle h (e,h) -> ollhe. If present.
        // Given the provided list, 'hello' should not form a pair with itself or 'olleh' by direct adjacent swap if the rule is strict.
        // "hello" -> 'ehllo', 'hlelo', 'helol'
        // "olleh" -> 'loleh', 'ohell', 'ollhe'
        // From the sample content: "hello", "olleh", "hlelo"
        // When processing "hello":
        //  - swap 'h','e' -> "ehllo". Not in dictionary.
        //  - swap 'e','l' -> "hlelo". IN DICTIONARY. -> hello hlelo pair
        //  - swap 'l','l' -> "helol". Not in dictionary and filtered due to same word.
        //  - swap 'l','o' -> "hell_o". Not in dictionary.
        // When processing "hlelo":
        //  - swap 'h','l' -> "lhleo". Not in dictionary.
        //  - swap 'l','e' -> "helo". (this is wrong, it should be "h_e_lo"). -> "hel_lo".
        //  - swap 'l','e' -> "helo". Transposing 'l','e' in 'hlelo' gives 'helol'
        //     'hlelo' (swap l,e) -> 'hlelo' becomes 'h_e_l_o'
        //     h l e l o
        //     0 1 2 3 4
        //     swap(h,l) -> lh elo
        //     swap(l,e) -> h el lo -> hel lo -> hlelo -> hel_lo (wrong logic, must be 'helol')
        //     char[] ch = {'h','l','e','l','o'}
        //     i=0: h,l -> {'l','h','e','l','o'} -> lh elo
        //     i=1: l,e -> {'h','e','l','l','o'} -> hell o -> 'hello' (YES, IN DICTIONARY!)
        //     Therefore: hlelo hello pair
        //     i=2: e,l -> {'h','l','l','e','o'} -> hl leo
        //     i=3: l,o -> {'h','l','e','o','l'} -> hleo l
        //
        // So, expected pairs for hello, olleh, hlelo:
        //  - from "hello" -> "hlelo" (swap e,l) -> pair: hello hlelo
        //  - from "hlelo" -> "hello" (swap l,e) -> pair: hello hlelo (canonical form, already processed)
        // The previous code had a bug where 'olleh' was generated from 'hello' in comments.
        // The `!word.equals(transposedWord)` check correctly handles `hello` transposing 'l' and 'l'.
        expected.add("hello hlelo");
        assertEquals(expected, parseOutput(outContent.toString()));
    }

    @Test
    void testEmptyFile() throws IOException {
        // An empty file should produce no output.
        String content = "";
        writeToFile(content);
        transposer.findTransposedWordPairs(tempFile.toString(), 3, 10);

        assertTrue(outContent.toString().trim().isEmpty());
    }

    @Test
    void testFileNotFound() {
		assertThrows(RuntimeException.class, () -> transposer.findTransposedWordPairs("non_existent_file.txt", 3, 10));
    }
}

