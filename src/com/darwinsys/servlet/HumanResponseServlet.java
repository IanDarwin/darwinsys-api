package com.darwinsys.servlet;

import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.darwinsys.graphics.JigglyTextImageWriter;
import com.darwinsys.security.PassPhrase;

/**
 * Generate output that requires a human response, that is,
 * output an image of a string that the user has to read and
 * type back into a form. The complication is that we can't
 * generate the image back to the middle of a JSP, so we create
 * it in a temp file, and write the &lt;IMG&gt; tag back to the user
 */
public class HumanResponseServlet extends HttpServlet {

	public static final String SESSION_KEY_RESPONSE = "c.d.s.RESPONSE_STRING";
	private static final long serialVersionUID = -101972891L;
	private static final int NUM_CHARS = 7;
	static final int H = 100;
	static final int W = 400;

	private JigglyTextImageWriter jiggler;

	@Override
	public void init(ServletConfig arg0) throws ServletException {
		super.init(arg0);
		jiggler = new JigglyTextImageWriter(new Font("SansSerif", Font.BOLD, 24), W, H); // XXX initparams
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		final ServletContext application = getServletContext();
		final HttpSession session = request.getSession();

		// create the random string
		final String challenge = randomString();

		// save it in the session
		session.setAttribute(SESSION_KEY_RESPONSE, challenge);

		final File dir = new File(application.getRealPath("/tmp"));
        final File tempFile = File.createTempFile("img", "jpg", dir);

		// Generate the image
		OutputStream os = null;
		try {
			os = new FileOutputStream(tempFile);

			jiggler.write(challenge, os);
		} finally {
			if (os != null) {
				os.close();
			}
		}

		// If that didn't throw an exception, print an IMG tag
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.printf("<img src='/tmp/%s' width='%d' height='%d' alt='image to read for human verification'>%n",
				tempFile.getName(), W, H);
		out.flush();
	}

	private String randomString() {
		return PassPhrase.getNext(NUM_CHARS);
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
