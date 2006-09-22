package com.darwinsys.servlet;

import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.darwinsys.graphics.JigglyTextImageWriter;

/**
 * Generate output that requires a human response, that is,
 * output an image of a string that the user has to read and
 * type back into a form
 */
public class HumanResponseServlet extends HttpServlet {

	private static final String SESSION_KEY_RESPONSE = "c.d.s.RESPONSE_STRING";
	private static final long serialVersionUID = -101972891L;
	static final int H = 200;
	static final int W = 400;

	JigglyTextImageWriter jiggler;

	@Override
	public void init(ServletConfig arg0) throws ServletException {
		super.init(arg0);
		Font font = new Font("SansSerif", Font.BOLD, 24);
		jiggler = new JigglyTextImageWriter(font, W, H); // XXX initparams
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();

		// create the random string
		String msg = randomString();

		// save it in the session
		session.setAttribute(SESSION_KEY_RESPONSE, msg);
	    response.setContentType("image/jpeg");
		OutputStream os = response.getOutputStream();

		jiggler.write(msg, os);
	}

	private String randomString() {
		return "QJX" + hashCode();
	}

	/**
	 * Return true iff the user entered the correct string.
	 * Designed to be called from the target servlet,
	 * just to encapsulate the logic for this all in one place.
	 * @param session
	 * @param input
	 * @return True if the user input matches what's in the session.
	 */
	public boolean isValidString(HttpSession session, String input) {
		return input.equals(session.getAttribute(SESSION_KEY_RESPONSE));
	}
}
