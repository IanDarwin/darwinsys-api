package com.darwinsys.sql;

import com.darwinsys.database.DataBaseException;
import com.darwinsys.sql.ConnectionUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import junit.framework.TestCase;

/**
 * name - purpose
 * @version $Id$
 */
public class ConnectionUtilTest extends TestCase {

	final static String MOCK_JBDB_DRIVER = "mock.MockJdbcDriver";

	public void testList() throws Exception {
		System.out.println("ConnectionUtilTest.testList()");
		Set<String> configs = ConnectionUtil.getConfigurationNames();
		boolean hasConfigNames = false;
		for (String element : configs) {
			System.out.println(element);
			hasConfigNames = true;
		}
		assertTrue(hasConfigNames);
	}

	public void testGetConnections() {
		System.out.println("ConnectionUtilTest.testList()");
		List<Configuration> configs = ConnectionUtil.getConfigurations();
		boolean hasConfigNames = false;
		for (Configuration element : configs) {
			System.out.println(element);
			hasConfigNames = true;
		}
		assertTrue(hasConfigNames);
	}

	public void testHasPassword() throws Exception {
		Configuration c = ConnectionUtil.getConfigurations().get(0);
		c.setDbPassword(null);
		assertFalse(c.hasPassword());
	}

	public void testGetConnectionBadDriver() throws Exception {
		try {
			Connection c = ConnectionUtil.getConnection("url", "mydriver",
					"operator", "secret");
			fail("getConnection w/ bad params Did not throw exception");
			System.out.println(c);
		} catch (ClassNotFoundException nfe) {
			String m = nfe.getMessage();
			assertEquals("failing driver class name", "mydriver", m);
			System.out.println("Caught expected ClassNotFoundException");
		} catch (DataBaseException e) {
			fail("Caught wrong exception " + e + "; check order of params");
		}
	}

	public void testGetConnectionBadURL() throws Exception {
		try {
			Connection c = ConnectionUtil.getConnection("url",
					MOCK_JBDB_DRIVER,
					"operator", "secret");
			fail("getConnection w/ bad params did not throw exception");
			System.out.println(c);
		} catch (SQLException e) {
			System.out.println("Caught expected Exception " + e);
		}
	}
}
