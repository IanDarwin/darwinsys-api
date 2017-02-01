package com.darwinsys.io;

import java.io.IOException;
import java.io.Reader;

/**
 * Read an Array of Strings as a Reader.
 * @author Ian Darwin
 * @author Adapted from a GIST by Steven Taschuk (stebel on github)
 */
public class StringArrayReader extends Reader {

	private String[] lines;
	private int lineNumber = 0, posInLine = 0;
	
	public StringArrayReader(String[] lines) {
		this.lines = lines;
	}
	
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		if (lines == null) {
			throw new IOException("Reader is closed");
		}
		if (lineNumber >= lines.length)
	        return -1;
	    while (posInLine >= lines[lineNumber].length()) {
	        lineNumber++;
	        posInLine = 0;
	        if (lineNumber >= lines.length)
	            return -1;
	    }
	    // We only want to read at most one "line" per read call.
	    len = Math.min(len, lines[lineNumber].length() - posInLine);
	    for (int i = 0; i < len; i++)
	        cbuf[off+i] = lines[lineNumber].charAt(posInLine+i);
	    cbuf[off+len] = '\n';
	    posInLine += len;
	    return len + 1;

	}

	@Override
	public void close() throws IOException {
		if (lines == null) {
			throw new IOException("Reader is already closed");
		}
		lines = null;
	}

}
