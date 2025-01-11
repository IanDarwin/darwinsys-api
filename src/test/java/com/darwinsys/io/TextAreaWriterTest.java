package com.darwinsys.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.PrintWriter;

import javax.swing.JTextArea;

import org.junit.jupiter.api.Test;

// tag::main[]
class TextAreaWriterTest {

	private static final String HELLO_WORLD = "Hello World";

	JTextArea ta = new JTextArea();

	@Test
	void one() {
		PrintWriter x = new PrintWriter(new TextAreaWriter(ta));
		x.print("Hello");
		x.print(' ');
		x.print("World");
		x.close();
		assertEquals(HELLO_WORLD, ta.getText());
	}
}
// end::main[]
