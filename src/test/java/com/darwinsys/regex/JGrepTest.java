package com.darwinsys.regex;

import java.io.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JGrepTest {

	private InputStream oldIn;
	private PrintStream oldOut;
	private ByteArrayInputStream in;
	private ByteArrayOutputStream out;

	// Common code for setting the stdin and stdout
	void setupStreamsFromString(String stdin) {
		oldIn = System.in;
		oldOut = System.out;
		in = new ByteArrayInputStream(stdin.getBytes());
		out = new ByteArrayOutputStream();
		System.setIn(in);
		System.setOut(new PrintStream(out));
	}

	String getStdOut() throws IOException {
		out.close();
		return out.toString();
	}

	public void tearDown() {
		restore();
	}

	private void restore() {
		System.setIn(oldIn);
		System.setOut(oldOut);
	}

	@Test
	void noFileNoOptions()  throws IOException {
		setupStreamsFromString("line 1 abc\nline 2 def\n");
		JGrep.main(new String[] { "de" });
		String result = getStdOut();
		restore();
		System.out.println("RESULT=" + result);
		assertNotNull(result);
		assertTrue(result.indexOf("def") != -1);
	}

	public void testNoFileWithOptions() {
		fail("Not yet implemented");
	}

	public void testOneFileNoOptions() {
		fail("Not yet implemented");
	}

	public void testOneFileWithOptions() {
		fail("Not yet implemented");
	}

}
