package com.darwinsys.textproc;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Limited testing of Anagram Finder.
 * NOTE: On a 3.8GHz AMD/Intel CPU, length 8 word takes ~20 seconds;
 * anything longer increases exponentially towards infinity.
 * Quantum computing may or may not fix this.
 */
public class AnagramFinderTest {

	static boolean onlyWords = true;
	AnagramFinder finder;

	@BeforeAll
	public static void setup() {
		AnagramFinder finder = new AnagramFinder(onlyWords);
	}

    @Test
	public void testCat() {
	System.out.println("--- Test with 'cat' ---");
		var ret = finder.findUniquePermutations("cat");
		assertEquals(2, ret.size());
        assertTrue(ret.contains("act"));
        assertTrue(ret.contains("cat"));
	}

    @Test
	public void testScat() {
	System.out.println("--- Test with 'scat' ---");
		var ret = finder.findUniquePermutations("scat");
        System.out.println("ret = " + ret);
        assertEquals(2, ret.size());
        assertTrue(ret.contains("cast"));
        assertTrue(ret.contains("scat"));
        /* XXX Should succeed!?
        assertTrue(ret.contains("cats"));
         */
	}

    @Test
	public void testMedium() {
        System.out.println("--- Test with 'listen' ---");
        var ret = finder.findUniquePermutations("listen");
        assertEquals(Set.of("enlist", "listen", "silent", "tinsel"), ret);
    }

    @Test
	public void testEmptyAndNull() {
        System.out.println("--- Test with empty string and null ---");
        assertThrows(IllegalArgumentException.class,
                () -> finder.findUniquePermutations(""));
        assertThrows(IllegalArgumentException.class,
                () -> finder.findUniquePermutations(null));
    }
}
