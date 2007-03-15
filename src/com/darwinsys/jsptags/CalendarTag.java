package com.darwinsys.jsptags;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.darwinsys.servlet.HTMLDateUtils;

/**
 * Calendar JSP Tag, cut down from Java Cookbook CalendarPage.jsp; just displays
 * current, does not have navigator.
 *
 * @version $Id$
 */
public class CalendarTag extends TagSupport {

	private static final long serialVersionUID = -1398213412504718137L;

	/** Invoked at the end tag boundary, does the work */
	public int doEndTag() throws JspException {

		try {
			final HttpServletRequest request = (HttpServletRequest) pageContext
					.getRequest();
			final JspWriter out = pageContext.getOut();

			out.println("<!-- Start of output from $Id$ -->");

			HTMLDateUtils.printMonthCalendar(new PrintWriter(out));

			out.println("<!-- end of output from CalendarTag -->");

			return SKIP_BODY;
		} catch (IOException t) {
			System.err.println("Tag caught: " + t);
			throw new JspException(t.toString());
		}
	}


}
