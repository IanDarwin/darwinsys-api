package com.darwinsys.jsptags;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Calendar JSP Tag, cut down from Java Cookbook CalendarPage.jsp; just displays
 * current, does not have navigator.
 * 
 * @version $Id$
 */
public class CalendarTag extends TagSupport {

	/** The days in each month. */
	int dom[] = {
	31, 28, 31, 30, /* jan feb mar apr */
	31, 30, 31, 31, /* may jun jul aug */
	30, 31, 30, 31 /* sep oct nov dec */
	};

	/** The names of the months */
	String[] months = { "January", "February", "March", "April", "May", "June",
			"July", "August", "September", "October", "November", "December"
	};

	/** Invoked at the end tag boundary, does the work */
	public int doEndTag() throws JspException {

		try {
			final HttpServletRequest request = (HttpServletRequest) pageContext
					.getRequest();
			final JspWriter out = pageContext.getOut();

			doCal(request, out);

			out.flush();
			return SKIP_BODY;
		} catch (IOException t) {
			System.err.println("Tag caught: " + t);
			throw new JspException(t.toString());
		}
	}

	private void doCal(HttpServletRequest request, JspWriter out)
			throws IOException {

		out.println("<!-- $Id$ -->");

		Calendar calendar = Calendar.getInstance();
		int yy = calendar.get(Calendar.YEAR);
		int mm = calendar.get(Calendar.MONTH);

		out.println("<table border=1>");
		out.println("<tr><th colspan=7>" + months[mm] + ", " + yy + "</tr>");

		out.println("<tr><td>Su<td>Mo<td>Tu<td>We<td>Th<td>Fr<td>Sa</tr>");

		// Compute how much to leave before the first.
		// getDay() returns 0 for Sunday, which is just right.
		int leadGap = calendar.get(Calendar.DAY_OF_WEEK) - 1;

		int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		out.print("<tr>");

		// Blank out the labels before 1st day of month
		for (int i = 0; i < leadGap; i++) {
			out.print("<td>&nbsp;");
		}

		// Fill in numbers for the day of month.
		for (int i = 1; i <= daysInMonth; i++) {

			out.print("<td>");
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
