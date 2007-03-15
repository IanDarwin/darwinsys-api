package com.darwinsys.servlet;

import java.io.PrintWriter;

import junit.framework.TestCase;

public class HTMLDateUtilsTest extends TestCase {

	public void testPrintMonthCalendar() throws Exception {

		PrintWriter out = new PrintWriter(System.out);
		HTMLDateUtils.printMonthCalendar(out);
		out.flush();
	}
}

