package com.darwinsys.tools;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.regex.Matcher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class XrefCheckTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void declRE() {
			Matcher md = XrefCheck.decl.matcher("[[dog-chapter]]");
			md.find();
			System.out.println("DECL: " + md.group(1));
			assertEquals("dog-chapter", md.group(1));
	}

	@Test
	void xrefRE() {

			Matcher mr = XrefCheck.ref.matcher("see woof <<dog-chapter>> or meow <<cat_chapter>>.");
			int i = 0;
			while (mr.find()) {
				++i;
				System.out.println("REF: " + mr.group(1));
			}
			assertEquals(2, i, "Failed to match 2 xrefs");
	}
}
