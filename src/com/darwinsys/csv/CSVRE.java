import com.darwinsys.util.Debug;
import java.io.*;
import org.apache.regexp.*;

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

	public static void main(String[] argv) throws IOException, RESyntaxException
	{
		String line;
	
		// Construct a new Regular Expression parser.
		Debug.println("regexp", "PATTERN = " + CSV_PATTERN); // debug
		RE csv = new RE(CSV_PATTERN);

		BufferedReader is = new BufferedReader(new InputStreamReader(System.in));

		// For each line...
		while ((line = is.readLine()) != null) {
			System.out.println("line = `" + line + "'");

			// For each field
			for (int fieldNum = 0, offset = 0; csv.match(line, offset); fieldNum++) {

				// Print the field (0=null, 1=quoted, 3=unquoted).
				int n = csv.getParenCount()-1;
				if (n==0)	// null field
					System.out.println("field[" + fieldNum + "] = `'");
				else
					System.out.println("field[" + fieldNum + "] = `" + csv.getParen(n) + "'");

				// Skip what already matched.
				offset += csv.getParen(0).length();
			}
		}
	}
}
