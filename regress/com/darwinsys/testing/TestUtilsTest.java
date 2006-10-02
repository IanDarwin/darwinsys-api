package com.darwinsys.testing;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

public class TestUtilsTest extends TestCase {

	class Mock {
		int i;
		String j;
		private Date d;
		public Date getDate() {
			return d;
		}
		public void setDate(Date d) {
			this.d = d;
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

}
