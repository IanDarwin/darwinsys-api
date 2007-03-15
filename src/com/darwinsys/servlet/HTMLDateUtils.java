package com.darwinsys.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import com.darwinsys.util.DateUtils;

/**
 * Utilities for dealing with Dates in the context of Servlets;
 * the print...Calendar routines depend on "calendar.css" being
 * extracted from the distribution and copied to the root of the
 * web application; without this it'll work but but look coyote ugly.
 */
public class HTMLDateUtils {

	/**
	 * Generate a small HTML month calendar for the current month onto the given PrintWriter
	 * @param out The PrintWriter
	 * @throws IOException
	 */
	public static void printMonthCalendar(PrintWriter out)
	throws IOException {
		printMonthCalendar(out, Calendar.getInstance());
	}

	/**
	 * Generate a small HTML month calendar for the given month onto the given PrintWriter
	 */
	public static void printMonthCalendar(PrintWriter out, Calendar calendar)
	throws IOException {

		int yy = calendar.get(Calendar.YEAR);
		int mm = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		out.println("<link rel='stylesheet' type='text/css' href='/calendar.css' />");

		out.println("<table class='cal-table-month'>");
		out.println("<tr><th colspan='7' class='cal-header-month'>" + DateUtils.getMonthName(mm) + ", " + yy + "</tr>");

		out.println("<tr class='cal-header-days'><th>Su<th>Mo<th>Tu<th>We<th>Th<th>Fr<th>Sa</tr>");

		//	Compute how much to leave before the first day.
		//	getDay() returns 0 for Sunday, which is just right.
		int leadGap = calendar.get(Calendar.DAY_OF_WEEK) - 1;

		int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		out.print("<tr>");

		//	Blank out the labels before 1st day of month
		for (int i = 0; i < leadGap; i++) {
			out.print("<td class='cal-cell-empty'>&nbsp;");
		}

		//	Fill in numbers for the day of month.
		for (int i = 1; i <= daysInMonth; i++) {

			out.printf("<td class='%s'>", i != day ? "cal-cell-day" : "cal-cell-today");
			out.print(i);
			out.print("</td>");

			if ((leadGap + i) % 7 == 0) { // wrap if end of line.
				out.println("</tr>");
				out.print("<tr>");
			}
		}

		out.println("</tr>");
		out.println("</table>");

	}
}
