import java.util.List;

import junit.framework.TestCase;

/**
 * name - purpose
 * @version $Id$
 */
public class CSVRETest extends TestCase {
	CSVRE target = new CSVRE();
	
	public void testCanonical() {
		List list = target.parse("\"a\",\"b\",\"c\"");
		assertEquals(3, list.size());
		assertEquals("a", list.get(0));
		assertEquals("b", list.get(1));
		assertEquals("c", list.get(2));
	}
	public void testNullField() {
		List list = target.parse("\"a\",,\"c\"");
		assertEquals(3, list.size());
		assertEquals("a", list.get(0));
		assertEquals(null, list.get(1));
		assertEquals("c", list.get(2));
	}
	public void testNotAllQuoted() {
		List list = target.parse("\"a\",b,\"c\"");
		assertEquals(3, list.size());
		assertEquals("a", list.get(0));
		assertEquals("b", list.get(1));
		assertEquals("c", list.get(2));
	}
}
