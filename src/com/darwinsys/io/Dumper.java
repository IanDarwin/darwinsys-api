import java.io.*;

/**
 * Read a file and decode, using DataInputStream and System.out
 */
public class DumpFile {

	public static void main(String[] av) {
		DumpFile c = new DumpFile();
		switch(av.length) {
		case 0: c.process(System.in);
			break;
		default:
			for (int i=0; i<av.length; i++)
				try {
					c.process(new FileInputStream(av[i]));
				} catch (FileNotFoundException e) {
					System.err.println(e);
				}
		}
	}

	/** The numberof items per line */
	public final static int PERLINE = 15;

	protected StringBuffer num = new StringBuffer();
	protected StringBuffer txt = new StringBuffer();

	protected void dump() {
		System.out.print(num);
		System.out.print(' ');
		System.out.print(txt);
		num.setLength(0);
		txt.setLength(0);
	}

	/** print one file, given an open InputStream */
	public void process(InputStream ois) {
		BufferedInputStream is = new BufferedInputStream(ois);
		num.setLength(0);
		txt.setLength(0);
		
		try {
			int b = 0;
			int column = 0;

			while ((b=is.read()) != -1) {
				num.append(b);
				num.append(' ');
				txt.append(Character.isLetterOrDigit((char)b) ? (char)b : '.');

				if (++column%PERLINE == 0) {
					dump();
				}
			}
			System.out.print("\n");
			is.close();
		} catch (IOException e) {
			System.out.println("IOException: " + e);
		}
	}
}
