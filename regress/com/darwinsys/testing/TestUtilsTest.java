package com.darwinsys.testing;

import static com.darwinsys.testing.TestUtils.assertNoDefaultProperties;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import junit.framework.AssertionFailedError;

public class TestUtilsTest {

	Calendar c;
	Mock m1, m2;
	
	/** Simple POJO mock for testing TestUtils */
	static class Mock {
		int i;
		String j;
		private Date d;
		public Date getDate() {
			return d;
		}
		public void setDate(Date d) {
			this.d = d;
		}
		@Override
		public boolean equals(Object o) {
			if (o == null || !(o.getClass() == Mock.class)) {
				return false;
			}
			Mock m = (Mock)o;
			if (this.i != m.i || this.j != m.j ||
					!(this.d.equals(m.d))) {
				return false;
			}
			return true;
		}
		@Override
		public int hashCode() {
			return i*51 | j.hashCode() | d.hashCode();
		}
	}

	@Before
	public void setup() {
		System.out.println("TestUtilsTest.setup()");
		c = Calendar.getInstance();
		m1 = new Mock();
		m1.i = 42;
		m1.j = "Hello";
		m1.d = c.getTime();
		m2 = new Mock();
		m2.i = m1.i;
		m2.j = m1.j;
		m2.d = m1.d;
		System.out.println(m1);System.out.println(m2);
	}
	
	@Test
	public void testEqualsObjectObject() {
		assertEquals("equality1", m1, m2);	// First tests Mock's equal!
		assertTrue("equality2", TestUtils.equals(m1, m1));
		assertTrue("equality3", TestUtils.equals(m1, m2));
		assertTrue("equality4", TestUtils.equals(m2, m1));
		assertFalse("equality5", TestUtils.equals(m2, null));
		m2.i++;
		assertFalse("equality6", TestUtils.equals(m1, m2));
		m2.i--;
		assertTrue("equality7", TestUtils.equals(m2, m1));
	}
		
	@Test
	public void testEqualsCalCal() {
		c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 1);
		m2.d = c.getTime();
		assertFalse("equality1", TestUtils.equals(m1, m2));
	}

	@Test
	public void testAssertNoDefaultProperties() throws Exception {
		Object o = new Object();
		assertNoDefaultProperties(o);

		Mock m = new Mock();
		try {
			assertNoDefaultProperties(m);
			fail("Didn't find null properties in blank Mock");
		} catch (AssertionFailedError e) {
			System.out.println("Caught expected " + e);
		}
		m.d = new Date();
		m.i = 1000;
		m.j = "mockme";
		assertNoDefaultProperties(m);
	}

}