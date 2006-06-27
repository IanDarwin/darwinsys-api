package io;

import java.io.PrintStream;

import javax.swing.JTextArea;

import com.darwinsys.io.TextAreaOutputStream;

import junit.framework.TestCase;

public class TextAreaWriterTest extends TestCase {
	public void testOne() {
		JTextArea ta = new JTextArea();
		PrintStream x = new PrintStream(new TextAreaOutputStream(ta));
		x.print("Hello");
		x.print(' ');
		x.println("World");
		assertEquals("Hello World", ta.getText());
	}
}
