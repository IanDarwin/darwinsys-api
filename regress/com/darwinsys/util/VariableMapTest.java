package util;


import com.darwinsys.util.VariableMap;

import junit.framework.TestCase;

/** Test the Variables code formerly in TestRunner */
public class VariableMapTest extends TestCase {
	
	VariableMap v = new VariableMap();
	
	public void testSetGet() {
		v = new VariableMap();
		v.setVar("name", "Robin Smith");
		assertEquals("simple get", "Robin Smith", v.getVar("name"));
		v.setVar("NAME", "Jo Jenkins");
		assertEquals("case-sens get", "Jo Jenkins", v.getVar("NAME"));
		assertFalse("case sens differ", v.getVar("name").equals(v.getVar("NAME")));
	}
	
	public void testInts() {
		v.setIntVar("my int prop", 42);
		assertEquals("get int", 42, v.getIntVar("my int prop"));
		try {
			v.setVar("bad int prop", "42 ");
			int i = v.getIntVar("bad int prop");
			fail("get bad int var returned " + i + "; should throw NFE instead");
		} catch (NumberFormatException e) {
			System.out.println("caught expected NFE");
		}
	}
	
	public void testDefaults() {
		assertNull(v.getVar("USER", null));
		assertEquals("bar", v.getVar("foo", "bar"));
		v.setVar("num", "42");
		assertEquals(100, v.getIntVar("NOTnum", 100));
		assertEquals(42, v.getIntVar("num", 100));
	}
	
	public void testSubst() {
		v.setVar("USER", "ian");
		v.setVar("PASSWD", "top secret");
		String before = "login(${USER}, ${PASSWD});";
		System.out.printf("Subst input:  %s%n", before);
		String expect = "login(ian, top secret);";
		String actual = v.substVars(before);
		System.out.printf("Subst result: %s%n", actual);
		assertEquals("subst", expect, actual);
	}

	public void testBadSubst() {
		String input = "What does ${noSuchVariable} do?";
		assertEquals("itempotent on bad subst", input, v.substVars(input));
	}
	
	public void testIdemPotent() {
		v.setVar("foo", "bar");
		final String string = "foo bar bleah";
		assertEquals("idempotent", string, v.substVars(string));
	}
	
	// Tests with BackSlashes
	public void testBackslashInSubstInputString() {
		v.setVar("abc", "123");
		String str = "anaylyse this: ${abc}\\123";	// \1 is special to regex!
		assertEquals("testBackslashInSubstInputString", 
			"anaylyse this: 123\\123", v.substVars(str));
	}
	
	public void testBackslashInVarUsedInSubst() {
		String abcInput = "a\\123";
		v.setVar("abc", abcInput);
		String abcOutput = v.getVar("abc");
		assertEquals(abcInput, abcOutput);
		// This will not yield 'a\\123\\123' because the first \
		// is absorbed by regex code as '\1', which yields the first
		// '1' in the expect string
		assertEquals("testBackslashInVarUsedInSubst",
			"a123\\123", v.substVars("${abc}\\123"));
	}
}
