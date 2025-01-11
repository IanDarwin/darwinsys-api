package com.darwinsys.util;

import static org.junit.jupiter.api.Assertions.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Test the Variable Map support
 */
class VariableMapTest {
	
	VariableMap v;

	@BeforeEach
	void start() {
		v = new VariableMap();
	}

	@Test
	void setGet() {
		v.setVar("name", "Robin Smith");
		assertEquals("Robin Smith", v.getVar("name"), "simple get");
		v.setVar("NAME", "Jo Jenkins");
		assertEquals("Jo Jenkins", v.getVar("NAME"), "case-sens get");
		assertNotEquals(v.getVar("name"), v.getVar("NAME"), "case sens differ");
	}

	@Test
	void ints() {
		v.setIntVar("my int prop", 42);
		assertEquals(42, v.getIntVar("my int prop"), "get int");
	}

	@Test
	void intsBad() {
		v.setVar("bad int prop", "42 ");
		assertThrows(NumberFormatException.class, () -> v.getIntVar("bad int prop"));
	}

	@Test
	void defaults() {
		assertNull(v.getVar("USER", null));
		assertEquals("bar", v.getVar("foo", "bar"));
		v.setVar("num", "42");
		assertEquals(100, v.getIntVar("NOTnum", 100));
		assertEquals(42, v.getIntVar("num", 100));
	}

	@Test
	void subst() {
		v.setVar("USER", "ian");
		v.setVar("PASSWD", "top secret");
		String before = "login(${USER}, ${PASSWD});";
		System.out.printf("Subst input:  %s%n", before);
		String expect = "login(ian, top secret);";
		String actual = v.substVars(before);
		System.out.printf("Subst result: %s%n", actual);
		assertEquals(expect, actual, "subst");
	}

	@Test
	void badSubst() {
		String input = "What does ${noSuchVariable} do?";
		assertEquals(input, v.substVars(input), "itempotent on bad subst");
	}

	@Test
	void idemPotent() {
		v.setVar("foo", "bar");
		final String string = "foo bar bleah";
		assertEquals(string, v.substVars(string), "idempotent");
	}

	// Tests with BackSlashes
	@Test
	void backslashInSubstInputString() {
		v.setVar("abc", "123");
		String str = "anaylyse this: ${abc}\\123";	// \1 is special to regex!
		assertEquals("anaylyse this: 123\\123", v.substVars(str), "testBackslashInSubstInputString");
	}

	@Test
	void backslashInVarUsedInSubst() {
		String abcInput = "a\\123";
		v.setVar("abc", abcInput);
		String abcOutput = v.getVar("abc");
		assertEquals(abcInput, abcOutput);
		// This will not yield 'a\\123\\123' because the first \
		// is absorbed by regex code as '\1', which yields the first
		// '1' in the expect string
		assertEquals("a123\\123", v.substVars("${abc}\\123"), "testBackslashInVarUsedInSubst");
	}
	
	private boolean changed = false;
	private PropertyChangeEvent event;

	@Test
	void propertyChangeListenerSupport() {
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
