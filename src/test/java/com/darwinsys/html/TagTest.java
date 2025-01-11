package com.darwinsys.html;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TagTest {

	private CharArrayWriter sout;
	private PrintWriter pout;

	@BeforeEach
	void setUp() throws Exception {
		sout = new CharArrayWriter();
		pout = new PrintWriter(sout);
	}

	@Test
	void doTag() {
		Tag.dotag(pout, "Xyz");
		String string = sout.toString();
		assertEquals("<Xyz></Xyz>", string);
	}

	@Test
	void doBodyTag() {
		Tag.doTag(pout, "Xyz", "a body is here");
		String string = sout.toString();
		assertEquals("<Xyz>a body is here</Xyz>", string);
	}

}
