package com.darwinsys.sql;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfigurationTest {

	SimpleSQLConfiguration target;

	@BeforeEach
	void setUp() throws Exception {
		target = new SimpleSQLConfiguration("name", "url", "drv", "user", "pass");
	}

	@Test
	void getDbDriverName() {
		assertEquals("drv", target.getDriverName());
	}

	@Test
	void getDbPassword() {
		assertEquals("pass", target.getPassword());
	}

	@Test
	void hasPassword() {
		assertTrue(target.hasPassword(), "pass passwd");
		target.setPassword("");
		assertFalse(target.hasPassword(), "0-length passwd");
		target.setPassword(null);
		assertFalse(target.hasPassword(), "null passwd");
	}

	@Test
	void getDbURL() {
		assertEquals("url", target.getDbURL());
	}

	@Test
	void getDbUserName() {
		assertEquals("user", target.getUserName());
	}

	@Test
	void testGetName() {
		assertEquals("name", target.getName());
	}

}
