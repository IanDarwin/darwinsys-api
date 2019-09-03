package com.darwinsys.io;

import java.io.PrintWriter;

import javax.swing.JTextArea;

import junit.framework.TestCase;

// tag::main[]
public class TextAreaWriterTest extends TestCase {

	private static final String HELLO_WORLD = "Hello World";

	JTextArea ta = new JTextArea();

	public void testOne() {
		PrintWriter x = new PrintWriter(new TextAreaWriter(ta));
		x.print("Hello");
		x.print(' ');
		x.print("World");
		x.close();
		assertEquals(HELLO_WORLD, ta.getText());
	}
}
// end::main[]
