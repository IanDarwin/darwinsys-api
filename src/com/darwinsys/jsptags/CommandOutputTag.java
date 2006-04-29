package com.darwinsys.jsptags;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/** CommandOutputTag - runs an external shell(system) command, and displays the output.
 * The command may include pipes, redirections, etc., at least on "normal" systems.
 */
public class CommandOutputTag extends TagSupport {

	private static final long serialVersionUID = -765529312054151714L;
	private boolean output = true;
	private boolean error  = false;
	private String command = null;

	/** Invoked at the end tag boundary, does the work */
	public int doStartTag() throws JspException  {
		try {
			JspWriter out = pageContext.getOut();
			if (command == null) { // This is a "CANTHAPPEN" since the TLD defines it as required
				throw new JspException("CommandOutputTag: command attribute is required");
			}
			
			Process p = Runtime.getRuntime().exec(command);

			InputStream streamFromProcess = null;
			if (output) {
				// getInputStream gives an Input stream connected to
				// the process p's standard output. Just use it to make
				// a BufferedReader to readLine() what the program writes out,
				// and copy each line to the JSP output.
				streamFromProcess = p.getInputStream();
			} else if (error) {
				streamFromProcess = p.getErrorStream();
			}
			if (streamFromProcess == null) {
				throw new JspException("One of output='true' or error='true' is required");
			}
			
			BufferedReader is = new BufferedReader(new InputStreamReader(streamFromProcess));
			
			String line;
			while ((line = is.readLine()) != null) {	
				out.println(line);
			}
			is.close();
			
		} catch (IOException ex) {
			throw new JspException("TextImageServleg.doEndTag: IO Error", ex);
		}
		return EVAL_BODY_INCLUDE;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public void setOutput(boolean output) {
		this.output = output;
	}

	public void setCommand(String command) {
		this.command = command;
	}
}
