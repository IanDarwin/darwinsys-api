package com.darwinsys.jsptags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * LoggedInRole - include body content if user is logged in as a user that has this "role".
 * @version $Id$
 */
public class LoggedInRoleTag extends BodyTagSupport {
	private String role;

	/** Invoked at the start tag boundary; does the work. */
	public int doStartTag() throws JspException {
		String myLabel = null;

		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		return request.isUserInRole(role) ? EVAL_BODY_INCLUDE : SKIP_BODY;

	}

	/**
	 * @param r The role to check for (e.g., "admin");
	 */
	public void setRole(String r) {
		role = r;
	}
}
