package com.darwinsys.jsptags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * LoggedInUser - include body content if the user is logged in as "user".
 */
public class LoggedInUserTag extends BodyTagSupport {

	private static final long serialVersionUID = 3258135738884108850L;
	private String userName;

	/** Invoked at the tag start transition; does the work */
	public int doStartTag() throws JspException {
		if (userName == null) {
			throw new JspException("LoggedInUserTag requires the 'user' attribute");
		}
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		return request.getRemoteUser().equals(userName) ? EVAL_BODY_INCLUDE : SKIP_BODY;

	}

	/**
	 * @param label The role to check for (e.g., "admin");
	 */
	public void setUser(String label) {
		this.userName = label;
	}
}
