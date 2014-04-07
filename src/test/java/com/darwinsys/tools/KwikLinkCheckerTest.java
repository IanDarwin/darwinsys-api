package com.darwinsys.tools;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

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
		assertTrue(checker.check("http://ibm.com/").ok);
	}
	
	@Test
	public void testCheckBadHost() {
		assertFalse(checker.check("http://ibm.moc/").ok);
	}
	
	@Test
	public void testCheck404() {
		assertFalse(checker.check("http://darwinsys.com/noSuchFileHere").ok);
	}
	
	@Test
	public void testBadUrl() {
		assertFalse(checker.check("http:foo-for-you").ok);
	}
	
	@Test
	public void testConnRefused() {
		checker.check("http://localhost:9999/");
	}
}
