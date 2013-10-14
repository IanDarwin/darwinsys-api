package com.darwinsys.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

/**
 * A utility class to generate multipart/x-mixed-replace responses,
 * the kind of responses that implement server push.
 * such as simple SHORT animations (if too long you run the server
 * out of open files/sockets!).

 * <b>Note that Microsoft Internet Explorer does not understand
 * this sort of response</b>.

 * To use this class, first construct a new MultipartResponse passing
 * to its constructor the servlet's response parameter. MultipartResponse
 * uses the response object to fetch the servlet's output stream and
 * to set the response's content type.

 * Then, for each page of content, begin by calling startResponse()
 * passing in the content type for that page. Send the content for the
 * page by writing to the output stream as usual. A call to endResponse()
 * ends the page and flushes the content so the client can see it. At
 * this point a sleep() or other delay can be added until the next
 * page is ready for sending.

 * The call to endResponse() is optional. The startResponse() method
 * knows whether the last response has been ended, and ends it itself
 * if necessary. However, it's wise to call endResponse() if there's
 * to be a delay between the time one response ends and the next begins.
 * It lets the client display the latest response during the time it
 * waits for the next one.

 * Finally, after each response page has been sent, a call to the
 * finish() method finishes the multipart response and sends a code
 * telling the client there will be no more responses.

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
		out = new PrintWriter(outputStream, true);

		// Set things up
		res.setContentType("multipart/byteranges; boundary=" +
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
		outputStream.flush();
		endedLastResponse = true;
	}

	public void finish() throws IOException {
		if (out == null) {
			throw new IllegalStateException("already called finish()");
		}
		out.println("--" + BOUNDARY_TEXT + "--");
		out.flush();
		outputStream.flush();
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
