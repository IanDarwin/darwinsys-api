package com.darwinsys.sql;

import junit.framework.TestCase;

public class ConfigurationTest extends TestCase {

	SimpleSQLConfiguration target;

	protected void setUp() throws Exception {
		super.setUp();
		target = new SimpleSQLConfiguration("name", "url", "drv", "user", "pass");
	}

	public void testGetDbDriverName() {
		assertEquals("drv", target.getDriverName());
	}

	public void testGetDbPassword() {
		assertEquals("pass", target.getPassword());
	}

	public void testHasPassword() {
		assertTrue("pass passwd", target.hasPassword());
		target.setPassword("");
		assertFalse("0-length passwd", target.hasPassword());
		target.setPassword(null);
		assertFalse("null passwd", target.hasPassword());
	}

	public void testGetDbURL() {
		assertEquals("url", target.getDbURL());
	}

	public void testGetDbUserName() {
		assertEquals("user", target.getUserName());
	}

	public void testGetName() {
		assertEquals("name", target.getName());
	}

}
