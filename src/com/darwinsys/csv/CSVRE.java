import java.io.*;
import org.apache.regexp.*;

/* Simple demo of CSV using Regular Expressions.
 */
public class CSVRE {	
	/** The rather involved pattern used to match CSV's */
	public static final String CSV_PATTERN = "([^\"\\\\]*(\\\\.[^\"\\\\])*)*\",?)|([^,]+),?|,";

	public static void main(String[] argv) throws IOException
	{
		String line;
	
		// Construct a new Regular Expression parser.
		RE csv = new RE(CSV_PATTERN);

		// Construct an input reader
		BufferedReader is = new BufferedReader(
			new InputStreamReader(System.in));

		while ((line = is.readLine()) != null) {
			System.out.println("line = `" + line + "'");
			int i = 0;
			while (csv.match(line)) {
				int start = csv.getParenStart();
				int end   = csv.getParenEnd();

				System.out.println("fields[" + i++ + "] = `" +
					fields[i] + "'");
		}
	}
}
