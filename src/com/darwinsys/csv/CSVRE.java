import java.io.*;
import java.util.*;

/* Simple demo of CSV using Regular Expressions.
 */
public class CSVSimple {	
	public static void main(String[] argv) throws IOException
	{
		String line;
	
		// Construct a new Regular Expression parser.
		RE csv = new RE("([^\"\\]*(?);

		BufferedReader is = new BufferedReader(
			new InputStreamReader(System.in));

		while ((line = is.readLine()) != null) {
			System.out.println("line = `" + line + "'");
			Iterator e = csv.match(line);
			int i = 0;
			while (e.hasNext()) 
				System.out.println("fields[" + i++ + "] = `" +
					e.next() + "'");
		}
	}
}
