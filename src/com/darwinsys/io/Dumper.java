package com.darwinsys.util;

import java.io.*;

/**
 * Class methods for dumping bytes from memory or input.
 */
public class Dumper {

	public static void main(String[] av) {
		Dumper c = new Dumper();
		switch(av.length) {
		case 0: c.dump(new StreamGetter(System.in));
			break;
		default:
			for (int i=0; i<av.length; i++)
				try {
					c.dump(new StreamGetter(new FileInputStream(av[i])));
				} catch (FileNotFoundException e) {
					System.err.println(e);
				}
		}
	}

	/** The general contract of a class to get bytes. */
	interface Getter {
		public int get();
	}
	/** A Getter that reads from an in-memory array of bytes */
	class ByteArrayGetter implements Getter {
		public ByteArrayGetter(byte[] data) {
			this.data = data;
			offset = 0;
			max = data.length;
		}
		private byte[] data;
		private int offset;
		private int max;
		public int get() {
			if (offset < max)
				return data[offset++];
			return -1;
		}
	}

	class StreamGetter implements Getter {
		private BufferedInputStream is;
		public StreamGetter(ois) throws IOException {
			if (ois instanceof BufferedInputStream)
				is = ois;
			else
				is = new BufferedInputStream(ois);
		}
		public int get() throws IOException {
			return ois.read();
		}
	}

	/** The number of items per line */
	public final static int PERLINE = 16;

	protected StringBuffer num = new StringBuffer();
	protected StringBuffer txt = new StringBuffer();

	private int offset;

	/** Output the line's bytes and printables, send line end,
	 * and reset the two StringBuffers.
	 */
	protected void endOfLine() {
		System.out.print(offset += PERLINE);
		System.out.print(": ");
		System.out.print(num);
		System.out.print(' ');
		System.out.print(txt);
		System.out.println();
		num.setLength(0);
		txt.setLength(0);
	}

	/** print one file, given an open InputStream */
	public void dump(Getter g) {
		BufferedInputStream is = new BufferedInputStream(ois);
		num.setLength(0);
		txt.setLength(0);

		offset = 0;
		
		try {
			int b = 0;
			int column = 0;

			while ((b=g.get()) != -1) {
				// XXX sleazebag formatting
				if (b < 16)
					num.append('0');
				num.append(Integer.toString(b, 16));
				num.append(' ');
				txt.append(Character.isLetterOrDigit((char)b) ? (char)b : '.');

				if (++column%PERLINE == 0) {
					endOfLine();
				}
			}
			// if partial line, output it.
			if (++column%PERLINE != 0) {
				endOfLine();
			}
			System.out.println();
			is.close();
		} catch (IOException e) {
			System.out.println("IOException: " + e);
		}
	}
}
