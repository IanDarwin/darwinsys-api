package com.darwinsys.jsptags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * LoggedInUser - include body content if the user is logged in as "user".
 * @version $Id$
 */
public class LoggedInTag extends BodyTagSupport {
	private String userName;

	public int doStartTag() throws JspException {
		String myLabel = null;

		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		return request.getRemoteUser().equals(null) ? 
				SKIP_BODY :
				EVAL_BODY_INCLUDE  ;

	}
}
