package com.darwinsys.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

/**
 * MultipartResponse lets a servlet generate "server push" responses,
 * such as simple SHORT animations (if too long you run the server
 * out of open files/sockets!).
 * <p>Here is one usage example; another is in
 * www.darwinsys.com/demo/MultipartResponseDemoServlet
 * <pre>
 * MultipartResponse mpr = new MultipartResponse(response);
 * PrintWriter out = mpr.getWriter();

 * mpr.startResponse("text/html");
 * out.println("Searching...");
 * mpr.endResponse();

 * ... time passes

 * mpr.startResponse("text/html");
 * out.println("None found :-(");
 * mpr.endResponse();
 * </pre>
 * <p>
 * Can be used freely under the O'Reilly Copyright terms, see
 * http://www.oreilly.com/pub/a/oreilly/ask_tim/2001/codepolicy.html.
 * @author Jason Hunter, http://www.oreilly.com/catalog/jservlet2/
 */
public class MultipartResponse {

	private static final String BOUNDARY_TEXT = "End";
	HttpServletResponse res;
	PrintWriter out;
	OutputStream outputStream;

	boolean endedLastResponse = true;

	public MultipartResponse(HttpServletResponse response) throws IOException {
		// Save the response object and output stream
		res = response;
		outputStream = response.getOutputStream();
		out = new PrintWriter(outputStream);

		// Set things up
		res.setContentType("multipart/x-mixed-replace;boundary=" +
				BOUNDARY_TEXT);
		out.println();
		out.println("--" + BOUNDARY_TEXT);
	}

	public void startResponse(String contentType) throws IOException {
		if (out == null) {
			throw new IllegalStateException("already called finish()");
		}
		// End the last response if necessary
		if (!endedLastResponse) {
			endResponse();
		}
		// Start the next one
		out.println("Content-Type: " + contentType);
		out.println();
		endedLastResponse = false;
	}

	public void endResponse() throws IOException {
		if (out == null) {
			throw new IllegalStateException("already called finish()");
		}
		// End the last response, and flush so the client sees the content
		out.println();
		out.println("--" + BOUNDARY_TEXT);
		out.flush();
		endedLastResponse = true;
	}

	public void finish() throws IOException {
		if (out == null) {
			throw new IllegalStateException("already called finish()");
		}
		out.println("--" + BOUNDARY_TEXT + "--");
		out.flush();
		// Not safe to re-use these after ending tag.
		out = null;
		outputStream = null;
	}

	public PrintWriter getWriter() {
		return out;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}
}