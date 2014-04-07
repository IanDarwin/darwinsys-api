package com.darwinsys.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.junit.Before;
import org.junit.Test;

/** Test the Variable Map support
 */
public class VariableMapTest {
	
	VariableMap v;

	@Before
	public void start() {
		v = new VariableMap();
	}
	
	@Test public void testSetGet() {
		v.setVar("name", "Robin Smith");
		assertEquals("simple get", "Robin Smith", v.getVar("name"));
		v.setVar("NAME", "Jo Jenkins");
		assertEquals("case-sens get", "Jo Jenkins", v.getVar("NAME"));
		assertFalse("case sens differ", v.getVar("name").equals(v.getVar("NAME")));
	}
	
	@Test public void testInts() {
		v.setIntVar("my int prop", 42);
		assertEquals("get int", 42, v.getIntVar("my int prop"));
	}
	
	@Test(expected=NumberFormatException.class)
	public void testIntsBad() {
		v.setVar("bad int prop", "42 "); // trailing space intentional!
		int i = v.getIntVar("bad int prop");
		fail("get bad int var returned " + i + "; should throw NFE instead");
	}
	
	@Test public void testDefaults() {
		assertNull(v.getVar("USER", null));
		assertEquals("bar", v.getVar("foo", "bar"));
		v.setVar("num", "42");
		assertEquals(100, v.getIntVar("NOTnum", 100));
		assertEquals(42, v.getIntVar("num", 100));
	}
	
	@Test public void testSubst() {
		v.setVar("USER", "ian");
		v.setVar("PASSWD", "top secret");
		String before = "login(${USER}, ${PASSWD});";
		System.out.printf("Subst input:  %s%n", before);
		String expect = "login(ian, top secret);";
		String actual = v.substVars(before);
		System.out.printf("Subst result: %s%n", actual);
		assertEquals("subst", expect, actual);
	}

	@Test public void testBadSubst() {
		String input = "What does ${noSuchVariable} do?";
		assertEquals("itempotent on bad subst", input, v.substVars(input));
	}
	
	@Test public void testIdemPotent() {
		v.setVar("foo", "bar");
		final String string = "foo bar bleah";
		assertEquals("idempotent", string, v.substVars(string));
	}
	
	// Tests with BackSlashes
	@Test public void testBackslashInSubstInputString() {
		v.setVar("abc", "123");
		String str = "anaylyse this: ${abc}\\123";	// \1 is special to regex!
		assertEquals("testBackslashInSubstInputString", 
			"anaylyse this: 123\\123", v.substVars(str));
	}
	
	@Test public void testBackslashInVarUsedInSubst() {
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
	
	private boolean changed = false;
	private PropertyChangeEvent event;
	
	@Test public void testPropertyChangeListenerSupport() {
		PropertyChangeListener liszt = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				changed = true;
				event = evt;
			}			
		};
		v.addPropertyChangeListener(liszt);
		v.put("foo", "123");
		assertEquals(123, v.getIntVar("foo"));
		assertTrue(changed);
		assertNotNull(event);
		assertTrue(v == event.getSource());
		assertEquals("foo", event.getPropertyName());
		assertNull(event.getOldValue());
		assertEquals("123", event.getNewValue());
		
		changed = false;
		v.removePropertyChangeListener(liszt);
		v.setIntVar("bar", 55);
		assertFalse(changed);
	}
}
