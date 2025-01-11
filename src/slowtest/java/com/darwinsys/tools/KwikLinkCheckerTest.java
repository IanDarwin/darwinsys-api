package com.darwinsys.tools;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Tests the link checker, but must be live on the internet.
 * It would be better to start a pico-server on localhost and
 * check most of these conditions that way.
 * @author Ian Darwin
 */
public class KwikLinkCheckerTest {

	KwikLinkChecker checker = new KwikLinkChecker();

	@Test
	public void testCheckGood() {
		assertTrue(checker.check("https://ibm.com/").ok);
	}
	
	@Test
	public void testCheckBadHost() {
		final LinkStatus check = checker.check("http://nosuchdomain/");
		assertFalse(check.ok);
	}
	
	@Test
	public void testBadUrl() {
		final LinkStatus check = checker.check("http:foo-for-you");
		assertFalse(check.ok);
	}
	
	@Test
	public void testConnRefused() {
		assertFalse(checker.check("http://localhost:9999/").ok);
	}
}
