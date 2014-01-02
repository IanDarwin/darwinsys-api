package com.darwinsys.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Class to do formatted dump ("hex dump") of data from various sources.
 * For example, the first few lines of running "java Dumper" with this
 * class' class file as its argument might look like this:
 * <pre>
 * 00016: ca fe ba be 00 00 00 2e 00 87 0a 00 29 00 3d 07  ???????.........
 * 00032: 00 3e 08 00 3f 0a 00 02 00 40 09 00 0a 00 41 07  ..............A.
 * 00048: 00 42 0a 00 06 00 3d 09 00 0a 00 43 09 00 0a 00  .B.........C....
 * 00064: 44 07 00 45 0a 00 0a 00 3d 07 00 46 09 00 47 00  D..E.......F..G.
 * 00080: 48 0a 00 0c 00 49 0a 00 0a 00 4a 07 00 4b 0a 00  H....I....J..K..
 * 00096: 10 00 40 07 00 4c 09 00 47 00 4d 0a 00 4e 00 4f  .....L..G.M..N.O
 * 00112: 09 00 47 00 50 09 00 0a 00 51 0a 00 52 00 53 0a  ..G.P....Q..R.S.
 * 00128: 00 4e 00 54 08 00 55 0a 00 4e 00 56 0a 00 4e 00  .N.T..U..N.V..N.
 * 00144: 57 0a 00 4e 00 58 0a 00 06 00 59 0a 00 06 00 5a  W..N.X....Y....Z
 * 00160: 0a 00 5b 00 5c 0a 00 06 00 5d 0a 00 5e 00 5f 0a  ................
 * 00176: 00 0a 00 60 0b 00 61 00 62 07 00 63 08 00 64 0a  ......a.b..c..d.
 * 00192: 00 24 00 65 0a 00 06 00 65 0a 00 4e 00 66 07 00  ...e....e..N.f..
 * 00208: 67 01 00 0f 6f 66 66 73 65 74 46 6f 72 6d 61 74  g...offsetFormat
 * 00224: 74 65 72 01 00 18 4c 6a 61 76 61 2f 74 65 78 74  ter...Ljava.text
 * 00240: 2f 4e 75 6d 62 65 72 46 6f 72 6d 61 74 3b 01 00  .NumberFormat...
 * 00256: 0e 42 59 54 45 53 5f 50 45 52 5f 4c 49 4e 45 01  .BYTES.PER.LINE.
 * 00272: 00 01 49 01 00 0d 43 6f 6e 73 74 61 6e 74 56 61  ..I...ConstantVa
 * 00288: 6c 75 65 03 00 00 00 10 01 00 03 6e 75 6d 01 00  lue........num..
 * 00304: 18 4c 6a 61 76 61 2f 6c 61 6e 67 2f 53 74 72 69  .Ljava.lang.Stri
 * 00320: 6e 67 42 75 66 66 65 72 3b 01 00 03 74 78 74 01  ngBuffer....txt.
 * </pre>
 * @author Ian F. Darwin, http://www.darwinsys.com/contact.jsp
 */
public class Dumper {

	/** A formatter for the offset */
	NumberFormat offsetFormatter = new DecimalFormat("00000");

	public static void main(String[] av) throws IOException {
		Dumper c = new Dumper();
		switch(av.length) {
		case 0: c.dump(new StreamDumpGetter(System.in));
			break;
		default:
			for (int i=0; i<av.length; i++)
				try {
					c.dump(new StreamDumpGetter(new FileInputStream(av[i])));
				} catch (FileNotFoundException e) {
					System.err.println(e);
				}
		}
	}

	/** The number of items per line */
	public final static int BYTES_PER_LINE = 16;

	protected StringBuffer num = new StringBuffer();
	protected StringBuffer txt = new StringBuffer();

	private int offset;

	/** Output the line's bytes and printables, send line end,
	 * and reset the two StringBuffers.
	 */
	protected void endOfLine() {
		System.out.print(offsetFormatter.format(offset += BYTES_PER_LINE));
		System.out.print(": ");
		System.out.print(num);
		System.out.print(' ');
		System.out.print(txt);
		System.out.println();
		num.setLength(0);
		txt.setLength(0);
	}

	/** print one file, given an open InputStream */
	public void dump(DumpSource g) {
		num.setLength(0);
		txt.setLength(0);

		offset = 0;
		
		try {
			int b = 0;
			int column = 0;

			while ((b=g.get()) != -1) {				
				num.append(String.format("%02x", b & 0xff));
				num.append(' ');
				txt.append(Character.isLetterOrDigit((char)b) ? (char)b : '.');

				if (++column % BYTES_PER_LINE == 0) {
					endOfLine();
				}
			}
			for ( ; column % BYTES_PER_LINE != 0 ; column++) {
				num.append("   ");
			}
			// if partial line, output it.
			if (++column % BYTES_PER_LINE != 0) {
				endOfLine();
			}
			System.out.println();
		} catch (IOException ex) {
			System.out.println("Dumper: " + ex.toString());
		}
	}
}
