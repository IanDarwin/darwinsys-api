package com.darwinsys.testing;

import java.util.Calendar;
import java.util.Date;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import static com.darwinsys.testing.TestUtils.assertNoDefaultProperties;

public class TestUtilsTest extends TestCase {

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
			return i | j.hashCode() | d.hashCode();
		}
	}

	public void testEqualsObjectObject() {
		Calendar c = Calendar.getInstance();
		Mock m1 = new Mock();
		m1.i = 42;
		m1.j = "Hello";
		m1.d = c.getTime();
		Mock m2 = new Mock();
		m2.i = m1.i;
		m2.j = m1.j;
		m2.d = m1.d;
		System.out.println(m1);System.out.println(m2);
		assertEquals(m1, m2);	// Really tests Mock's equal!
		assertTrue(TestUtils.equals(m1, m1));
		assertTrue(TestUtils.equals(m1, m2));
		assertTrue(TestUtils.equals(m2, m1));
		assertFalse(TestUtils.equals(m2, null));
		m2.i++;
		assertFalse(TestUtils.equals(m1, m2));
		m2.i--;
		assertTrue(TestUtils.equals(m2, m1));
		c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 1);
		m2.d = c.getTime();
		assertFalse(TestUtils.equals(m1, m2));
	}

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