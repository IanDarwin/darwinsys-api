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
		v.setVar("my int prop", "42");
		assertEquals("get int", 42, v.getIntVar("my int prop"));
		try {
			v.setVar("bad int prop", "42 ");
			int i = v.getIntVar("bad int prop");
			fail("get bad int var returned " + i + "; should throw NFE instead");
		} catch (NumberFormatException e) {
			System.out.println("caught expected NFE");
		}
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
	
	public void testIdemPotent() {
		v.setVar("foo", "bar");
		final String string = "foo bar bleah";
		assertEquals("idempotent", string, v.substVars(string));
	}
}
