import java.io.*;

import java.util.*;
import java.util.regex.*;
import com.darwinsys.util.Debug;

/* Simple demo of CSV matching using Regular Expressions.
 * Does NOT use the "CSV" class defined in the Java CookBook.
 * RE Pattern from Chapter 7, Mastering Regular Expressions (p. 205, first edn.)
 */
public class CSVRE {	
	/** The rather involved pattern used to match CSV's consists of three
	 * alternations: the first matches quoted fields, the second unquoted,
	 * the third null fields
	 */
	public static final String CSV_PATTERN =
		"\"([^\"\\\\]*(\\\\.[^\"\\\\]*)*)\",?|([^,]+),?|,";
	private static Pattern csvRE;

	public static void main(String[] argv) throws IOException {
		new CSVRE().process();
	}
	
	public void process() throws IOException {
		String line;

		// Construct a new Regular Expression parser.
		Debug.println("regexp", "PATTERN = " + CSV_PATTERN); // debug
		csvRE = Pattern.compile(CSV_PATTERN);
		BufferedReader is =
			new BufferedReader(new InputStreamReader(System.in));

		// For each line...
		while ((line = is.readLine()) != null) {
			System.out.println("line = `" + line + "'");
			List l = parse(line);
			for (int i = 0; i < l.size(); i++) {
				System.out.println(l.get(i) + ",");
			}
			System.out.println();
		}
	}
	
	/** Parse one line. Overkill for this program, but factored out
	 * to be similar to original CSV class.
	 */
	public List parse(String line) {
		List list = new ArrayList();
		Matcher m = csvRE.matcher(line);
		m.matches();
		// For each field
		for (int fieldNum = 0; fieldNum < m.groupCount(); fieldNum++) {
			System.out.println(
					"field[" + fieldNum + "] = `" + m.group(fieldNum) + "'");
		}
		return list;
	}
	
}
