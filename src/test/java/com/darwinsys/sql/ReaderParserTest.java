package com.darwinsys.sql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import junit.framework.TestCase;

public class ReaderParserTest extends TestCase {

	private BufferedReader getReader(String data) {
		return new BufferedReader(new StringReader(data));
	}
	
	private String getStatement(String input) throws IOException {
		return SQLRunner.getStatement(getReader(input));
	}
	
	public void testGetStatement1() throws Exception {
		String[] data = { "\\d;", "\\d\n;" };
		String r1 = getStatement(data[0]);
		String r2 = getStatement(data[1]);
		assertEquals(r1.trim(), r2.trim());
	}

	public void testGetStatement2() throws Exception {
		
		String command = "-- This is a comment";
		assertNull(SQLRunner.getStatement(getReader(command)));
		
		command = "# This is a comment";
		assertNull(SQLRunner.getStatement(getReader(command)));
		
		command = "*;\n";
		assertEquals("*", getStatement(command));
		
		command = "select\n# a comment\n*;\n";
		assertEquals("select *", getStatement(command));
	}
}
