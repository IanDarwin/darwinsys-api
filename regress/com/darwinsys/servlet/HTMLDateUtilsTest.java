package com.darwinsys.servlet;

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.TimeZone;

import junit.framework.TestCase;

public class HTMLDateUtilsTest extends TestCase {

	public void testPrintMonthCalendar() throws Exception {

		PrintWriter out = new PrintWriter(System.out);
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Canada/Eastern"));
		c.set(Calendar.YEAR, 2007);
		c.set(Calendar.MONTH, Calendar.MARCH);
		c.set(Calendar.DAY_OF_MONTH, 15);
		HTMLDateUtils.printMonthCalendar(out, c);
		out.flush();
	}
}

