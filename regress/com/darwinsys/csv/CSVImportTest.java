import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;


/**
 * @author ian
 */
public class CSVTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(CSVTest.class);
	}
	
	CSV csv = new CSV();

	String[] data = { 
		"abc",
		"hello, world",
		"a,b,c",
		"a\"bc,d,e",
		"\"a,a\",b,\"c:\\foo\\bar\"",
		"\"he\"llo",
		"123,456",
		"\"LU\",86.25,\"11/4/1998\",\"2:19PM\",+4.0625",
		"bad \"input\",123e01",
		//"XYZZY,\"\"|\"OReilly & Associates| Inc."|"Darwin| Ian"|"a \"glug\" bit|"|5|"Memory fault| core NOT dumped"

	};
	int[] listLength = {
					1,
					2,
					3,
					3,
					3,
					1,
					2,
					5,
					2
	};
	
	/** test all the Strings in "data" */
	public void testCSV() {
		for (int i = 0; i < data.length; i++){
			List l = csv.parse(data[i]);
			assertEquals(l.size() , listLength[i]);
			for (int k = 0; k < l.size(); k++){
				System.out.print("[" + l.get(k) + "],");
			}
			System.out.println();
		}
	}
	
	/** Test one String with a non-default delimiter */
	public void testBarDelim() {
		// Now test slightly-different string with a non-default separator
		CSV parser = new CSV('|'); 
		List l = parser.parse(
			"\"LU\"|86.25|\"11/4/1998\"|\"2:19PM\"|+4.0625");
		assertEquals(l.size(), 5);
		Iterator it = l.iterator();
		while (it.hasNext()) {
			System.out.print("[" + it.next() + "],");
		}
		System.out.println();
	}
}
