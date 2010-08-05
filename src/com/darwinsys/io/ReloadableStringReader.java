package com.darwinsys.io;

import java.io.IOException;
import java.io.Reader;

/**
 * A simple NON-THREAD-SAFE ReloadableStringReader designed for recycling
 * rather than recreation when you need to read from a bunch of Strings
 * one after another in rapid succession.
 */
public class ReloadableStringReader extends Reader {
	protected String contents;
	protected final static int MARK_INVALID = -1;
	protected int mark = MARK_INVALID;
	protected int pos;

	public ReloadableStringReader(String input) {
		setString(input);
	}
	public ReloadableStringReader() {
		// empty
	}

	public void setString(String s) {
		this.contents = s;
		pos = 0;
		mark = MARK_INVALID;
	}

	public int read() {
		if (contents == null) {
			throw new IllegalStateException("No string provided");
		}
		return (pos < contents.length() ? contents.charAt(pos++) : -1);
	}

	public int read(char[] data) {
		return read(data, 0, data.length);
	}

	public int read(char[] data, int off, int len) {
		int n = 0;
		for (int i = 0; i < len; i++) {
			int ch = read();
			if (ch == -1) {
				return n>0 ? n : -1;
			}
			data[i + off] = (char)ch;
			++n;
		}
		return n;
	}

	public int available() {
		return contents.length() - pos;
	}

	public boolean markSupported() {
		return true;
	}

	public void mark(int readLimit) {
		// XXX implement readLimit
		this.mark = pos;
	}

	public void reset() {
		if (mark == MARK_INVALID) {
			throw new IllegalArgumentException("mark not set");
		}
		pos = mark;
	}

	public long skip(long n) {
		long actual = Math.min(n, available());
		pos = (int)actual;
		return actual;
	}

	public void close() throws IOException {
		// ignored
	}
}
