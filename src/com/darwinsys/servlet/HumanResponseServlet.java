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
 * Also known to some as "CAPTCHA", this servlet will
 * generate one of those annoying but necessary images that the user has to read
 * and verify to confirm that she's a human being not a spambot. The complication is that we can't
 * generate the image back to the middle of a JSP, so we create
 * it in a temp file, and write the &lt;IMG&gt; tag back to the user
 * <p>
 * Because of this, there must be a <em>writable</em> Temporary
 * Directory /tmp inside the web app directory; this is unusual
 * from a security point of view but would be quite hard to subvert
 * since the servlet does not accept any parameters from the user
 * that are used in creating the file.
 * <p>
 * Typical usage of this servlet (in, e.g, contact.jsp):
 * <pre>&lt;jsp:include page="/servlet/HumanResponseServlet"&gt;</pre>
 * <p>
 * Typical code in response servlet, e.g., ContactServlet:
 * <pre>
 * final String actualChallenge = request.getParameter("challenge");
 * final String expectedChallenge =
 *     (String)request.getSession().getAttribute(HumanResponseServlet.SESSION_KEY_RESPONSE);
 * if (actualChallenge == null) {
 *     out.println("You must provide a value for the challenge string");
 * 	   giveTryAgainLink(out);
 *     return;
 * }
 * if (!actualChallenge.equals(expectedChallenge)) {
 *     out.println("Sorry, you didn't pass the anti-Turing test :-)");
 *     giveTryAgainLink(out);
 *     return;
 * }
 * </pre>
 */
public class HumanResponseServlet extends HttpServlet {

	public static final String SESSION_KEY_RESPONSE = "c.d.s.RESPONSE_STRING";
	public static final String SESSION_KEY_TIMESTAMP = "c.d.s.RESPONSE_TIME";
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

		// And the timestamp
		session.setAttribute(SESSION_KEY_TIMESTAMP, System.currentTimeMillis());

		final File dir = new File(application.getRealPath("/tmp"));
        final File tempFile = File.createTempFile("challenge", ".jpg", dir);

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
