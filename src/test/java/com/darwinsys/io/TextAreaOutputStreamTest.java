package com.darwinsys.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.PrintStream;

import javax.swing.JTextArea;

import org.junit.jupiter.api.Test;

class TextAreaOutputStreamTest {
	private static final String HELLO_WORLD = "Hello World";

	JTextArea ta = new JTextArea();

	@Test
	void one() {
		PrintStream x = null;
		try {
			x = new PrintStream(new TextAreaOutputStream(ta));
			x.print("Hello");
			x.print(' ');
			x.println("World");
			assertEquals(HELLO_WORLD, ta.getText());
		} finally {
			if (x != null)
				x.close();
		}
	}

	@Test
	void setOut() {
		PrintStream oldOut = null;
		try {
			oldOut = System.out;
			System.setOut(new PrintStream(new TextAreaOutputStream(ta)));
			System.out.println(HELLO_WORLD); // part of test!
			assertEquals(HELLO_WORLD, ta.getText());
			System.setOut(oldOut);
		} finally {
			if (oldOut != null)
				oldOut.close();
		}
	}
}
