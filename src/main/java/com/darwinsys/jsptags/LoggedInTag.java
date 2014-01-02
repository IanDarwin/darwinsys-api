package com.darwinsys.jsptags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * LoggedInUser - include body content if the user is logged in as "user".
 */
public class LoggedInTag extends BodyTagSupport {

	private static final long serialVersionUID = 1028271617838262L;

	/** Invoked at the tag start boundary; does the work */
	public int doStartTag() throws JspException {

		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		return request.getRemoteUser() == null ? 
				SKIP_BODY :
				EVAL_BODY_INCLUDE  ;

	}
}
