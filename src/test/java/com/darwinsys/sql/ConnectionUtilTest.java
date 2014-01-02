package com.darwinsys.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

/**
 * Test for ConnectionUtils
 */
public class ConnectionUtilTest {

	final static String MOCK_JBDC_DRIVER = "com.darwinsys.sql.MockJDBCDriver";

	@Test
	public void testGetConfigurationNames() throws Exception {
		System.out.println("ConnectionUtilTest.testList()");
		Set<String> configs = ConnectionUtil.getConfigurationNames();
		boolean hasConfigNames = false;
		for (String element : configs) {
			System.out.println(element);
			hasConfigNames = true;
		}
		assertTrue("config names", hasConfigNames);
	}

	@Test
	public void testGetConnections() {
		System.out.println("ConnectionUtilTest.testList()");
		final List<Configuration> configs = ConnectionUtil.getConfigurations();
		assertTrue("connection list", configs.size() > 0);
	}

	@Test
	public void testHasPassword() throws Exception {
		final Configuration c = ConnectionUtil.getConfigurations().get(0);
		c.setPassword(null);
		assertFalse(c.hasPassword());
	}

	@Test
	public void testGetConnectionManualDriver() throws Exception {
		final Connection c = ConnectionUtil.getConnection(
				"jdbc:mock:some_url", MOCK_JBDC_DRIVER,
				"operator", "secret");
		assertNotNull("Get Conn from 4 params", c);
	}
	
	@Test(expected=ClassNotFoundException.class)
	public void testGetConnectionBadDriver() throws Exception {
		final Connection c = ConnectionUtil.getConnection("url", "mydriver",
				"operator", "secret");
		fail("getConnection w/ bad params Did not throw exception");
		System.out.println(c);
	}

	@Test
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
}
