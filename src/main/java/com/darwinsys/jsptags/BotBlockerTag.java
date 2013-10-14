package com.darwinsys.jsptags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class BotBlockerTag extends TagSupport {
	private static final long serialVersionUID = 342193689201407L;

	@Override
	public int doEndTag() throws JspException {
		try {
			final HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
			final JspWriter out = pageContext.getOut();
			final String ua = request.getHeader("user-agent");
			final HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
			if (isBlockable(ua)) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				out.print("<h1>403 Forbidden</h1>");
				out.println("<p>You do not have permission to access this resource.</p>");
			}
			return SKIP_BODY;
		} catch (Exception t) {
			System.err.println("Tag caught: " + t);
			throw new JspException(t.toString());
		}
	}

	private boolean isBlockable(String ua) {
		if (ua.indexOf("bot") >= 0) {
			return true;
		}
		if (ua.indexOf("spider") >= 0) {
			return true;
		}
		return false;
	}
}
