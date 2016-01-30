package com.darwinsys.tools;

import static org.junit.Assert.*;

import java.util.regex.Matcher;

import org.junit.Before;
import org.junit.Test;

public class XrefCheckTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testDeclRE() {
			Matcher md = XrefCheck.decl.matcher("[[dog-chapter]]");
			md.find();
			System.out.println("DECL: " + md.group(1));
			assertEquals("dog-chapter", md.group(1));
	}
	@Test
	public void testXrefRE() {

			Matcher mr = XrefCheck.ref.matcher("see woof <<dog-chapter>> or meow <<cat_chapter>>.");
			int i = 0;
			while (mr.find()) {
				++i;
				System.out.println("REF: " + mr.group(1));
			}
			assertEquals("Failed to match 2 xrefs", 2, i);
	}
}
