package io;

import java.io.PrintWriter;

import javax.swing.JTextArea;

import junit.framework.TestCase;

import com.darwinsys.io.TextAreaWriter;

public class TextAreaOutputStreamTest extends TestCase {
	public void testOne() {
		JTextArea ta = new JTextArea();
		PrintWriter x = new PrintWriter(new TextAreaWriter(ta));
		x.print("Hello");
		x.print(' ');
		x.print("World");
		assertEquals("Hello World", ta.getText());
	}
}
