package com.darwinsys.sql;

import junit.framework.TestCase;

public class ConfigurationTest extends TestCase {

	Configuration target;

	protected void setUp() throws Exception {
		super.setUp();
		target = new Configuration("name", "url", "drv", "user", "pass");
	}

	public void testGetDbDriverName() {
		assertEquals("drv", target.getDbDriverName());
	}

	public void testGetDbPassword() {
		assertEquals("pass", target.getDbPassword());
	}

	public void testHasPassword() {
		assertTrue("pass passwd", target.hasPassword());
		target.setDbPassword("");
		assertFalse("0-length passwd", target.hasPassword());
		target.setDbPassword(null);
		assertFalse("null passwd", target.hasPassword());
	}

	public void testGetDbURL() {
		assertEquals("url", target.getDbURL());
	}

	public void testGetDbUserName() {
		assertEquals("user", target.getDbUserName());
	}

	public void testGetName() {
		assertEquals("name", target.getName());
	}

}
