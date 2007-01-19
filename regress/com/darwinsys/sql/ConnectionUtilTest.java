package com.darwinsys.sql;

import com.darwinsys.database.DataBaseException;
import com.darwinsys.sql.ConnectionUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import junit.framework.TestCase;

/**
 * Test for ConnectionUtils
 * @version $Id$
 */
public class ConnectionUtilTest extends TestCase {

	final static String MOCK_JBDB_DRIVER = "mock.MockJdbcDriver";

	public void testGetConfigurationNames() throws Exception {
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
		final List<Configuration> configs = ConnectionUtil.getConfigurations();
		assertTrue(configs.size() > 1);
	}

	public void testHasPassword() throws Exception {
		final Configuration c = ConnectionUtil.getConfigurations().get(0);
		c.setPassword(null);
		assertFalse(c.hasPassword());
	}

	public void testGetConnectionBadDriver() throws Exception {
		try {
			final Connection c = ConnectionUtil.getConnection("url", "mydriver",
					"operator", "secret");
			fail("getConnection w/ bad params Did not throw exception");
			System.out.println(c);
		} catch (ClassNotFoundException nfe) {
			final String m = nfe.getMessage();
			assertEquals("failing driver class name", "mydriver", m);
			System.out.println("Caught expected ClassNotFoundException");
		} catch (DataBaseException e) {
			fail("Caught wrong exception " + e + "; check order of params");
		}
	}

	public void testPackageGetConfiguration() {
		Properties p = new Properties();
		final String DRIVERNAME = "someDriverName";
		p.setProperty("foo.DBDriver", DRIVERNAME);
		final String DBURL = "a nice long dburl";
		p.setProperty("foo.DBURL", DBURL);
		final String DBUSERNAME = "db2inst1";
		p.setProperty("foo.DBUser", DBUSERNAME);

		SimpleSQLConfiguration conf = ConnectionUtil.getConfiguration(p, "foo");

		assertEquals(DRIVERNAME, conf.getDriverName());
		assertEquals(DBURL, conf.getDbURL());
		assertEquals(DBUSERNAME, conf.getUserName());
	}

	public void testGetConnectionBadURL() throws Exception {
		try {
			final Connection c = ConnectionUtil.getConnection("url",
					MOCK_JBDB_DRIVER,
					"operator", "secret");
			fail("getConnection w/ bad params did not throw exception");
			System.out.println(c);
		} catch (SQLException e) {
			System.out.println("Caught expected Exception " + e);
		}
	}
}
