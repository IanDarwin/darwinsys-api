import java.io.*;

import java.util.*;
import java.util.regex.*;
import com.darwinsys.util.Debug;

/* Simple demo of CSV matching using Regular Expressions.
 * Does NOT use the "CSV" class defined in the Java CookBook. RE pattern
 * adapted from Chapter 7 of <em>Mastering Regular Expressions</em> 
 * (p. 205, first edn.)
 * @version $Id$
 */
public class CSVRE {	
	/** The rather involved pattern used to match CSV's consists of three
	 * alternations: the first matches quoted fields, the second unquoted,
	 * the third null fields
	 */
	public static final String CSV_PATTERN =
		"\"([^\"\\\\]*(\\\\.[^\"\\\\]*)*)\",?|(([^,]+)),?|,";
	private static Pattern csvRE;

	public static void main(String[] argv) throws IOException {
		System.out.println(CSV_PATTERN);
		new CSVRE().process();
	}
	
	public CSVRE() {
		// Construct a new Regular Expression parser.
		Debug.println("regexp", "PATTERN = " + CSV_PATTERN); // debug
		csvRE = Pattern.compile(CSV_PATTERN);
	}
	
	public void process() throws IOException {
		String line;

		BufferedReader is =
			new BufferedReader(new InputStreamReader(System.in));

		// For each line...
		while ((line = is.readLine()) != null) {
			System.out.println("line = `" + line + "'");
			List l = parse(line);
			System.out.println("Found " + l.size() + " items.");
			for (int i = 0; i < l.size(); i++) {
				System.out.print(l.get(i) + ",");
			}
			System.out.println();
		}
	}
	
	/** Parse one line. Factored out
	 * to be similar to original CSV class.
	 */
	public List parse(String line) {
		List list = new ArrayList();
		Matcher m = csvRE.matcher(line);
		// For each field
		while (m.find()) {
			// Get last group to exclude the trailing "," character
			list.add(m.group(1));
		}
		return list;
	}
}
