import java.util.*;

/* Simple demo of CSV parser class.
 */
public class CSVSimple {	
	public static void main(String[] args) {
		CSV parser = new CSV();
		Iterator it = parser.parse(
			"\"LU\",86.25,\"11/4/1998\",\"2:19PM\",+4.0625");
		while (it.hasNext()) {
			System.out.println(it.next());
		}

		// Now test with a non-default separator
		parser = new CSV('|');
		it = parser.parse(
			"\"LU\"|86.25|\"11/4/1998\"|\"2:19PM\"|+4.0625");
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
}
