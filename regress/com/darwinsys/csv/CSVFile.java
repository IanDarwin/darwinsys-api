import java.io.*;
import java.util.*;

/** CSV in action: lines from a file and print. */
public class CSVFile {

	public static void main(String[] argv) throws IOException
	{
		String line;
	
		// Construct a new CSV parser.
		CSV csv = new CSV();

		BufferedReader is = new BufferedReader(
			new InputStreamReader(System.in));
		while ((line = is.readLine()) != null) {
			System.out.println("line = `" + line + "'");
			Iterator e = csv.parse(line);
			int i = 0;
			while (e.hasNext()) 
				System.out.println("field[" + i++ + "] = `" +
					e.next() + "'");
		}
	}
}
