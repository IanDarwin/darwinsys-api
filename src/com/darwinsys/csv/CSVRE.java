import java.io.*;
import org.apache.regexp.*;

/* Simple demo of CSV matching using Regular Expressions.
 * Does NOT use the "CSV" class defined in the book.
 * Pattern from Chapter 7, Mastering Regular Expressions (p. 205, first edn.)
 */
public class CSVRE {	
	/** The rather involved pattern used to match CSV's */
	public static final String CSV_PATTERN =
		"(\"([^\"\\\\]*(\\\\.[^\"\\\\]*)*)\",?|([^,]+),?|,)+";

	public static void main(String[] argv)
		throws IOException, RESyntaxException
	{
		String line;
	
		// Construct a new Regular Expression parser.
		System.out.println("PATTERN = " + CSV_PATTERN); // debug
		RE csv = new RE(CSV_PATTERN);

		// Construct an input reader
		BufferedReader is = new BufferedReader(
			new InputStreamReader(System.in));

		while ((line = is.readLine()) != null) {
			System.out.println("line = `" + line + "'");
			while (csv.match(line)) {
				int n = csv.getParenCount();
				System.out.println("Count = " + n);

				for (int i=0; i<n; i++) {
					System.out.println("fields[" + i + "] = `" +
						csv.getParen(i) + "'");
				}
				break;	// until we increment line
			}
		}
	}
}
