package com.darwinsys.sql;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Test for ConnectionUtils
 */
class ConnectionUtilTest {

	final static String MOCK_JBDC_DRIVER = "com.darwinsys.sql.MockJDBCDriver";

	@BeforeAll
	static void setupConfigs() {
		// This works on Maven and Eclipse. Other build tools: yer on yer own.
		// ConnectionUtil.setConfigFileName("target/classes/db.properties");
		// Just use the provided one, on Maven test classpath.
	}

	/** Check that we have at least one configuration */
	@Test
	void getConfigurationNames() throws Exception {
		System.out.println("ConnectionUtilTest.testList()");
		Set<String> configs = ConnectionUtil.getConfigurationNames();
		boolean hasConfigNames = false;
		for (String element : configs) {
			System.out.println(element);
			hasConfigNames = true;
		}
		assertTrue(hasConfigNames, "config names");
	}

	@Test
	void getConnections() {
		System.out.println("ConnectionUtilTest.testList()");
		final List<Configuration> configs = ConnectionUtil.getConfigurations();
		assertTrue(configs.size() > 0, "connection list");
	}

	@Test
	void hasPassword() throws Exception {
		final Configuration c = ConnectionUtil.getConfigurations().get(0);
		c.setPassword(null);
		assertFalse(c.hasPassword());
	}

	@Test
	void getConnectionManualDriver() throws Exception {
		final Connection c = ConnectionUtil.getConnection(
				"jdbc:mock:some_url", MOCK_JBDC_DRIVER,
				"operator", "secret");
		assertNotNull(c, "Get Conn from 4 params");
	}

	@Test
	void getConnectionBadDriver() {
		assertThrows(ClassNotFoundException.class, () ->
			ConnectionUtil.getConnection(
				"jdbc:foo:bar", "NoSuchDriver",
				"operator", "secret"));
	}

	@Test
	void packageGetConfiguration() {
		Properties p = new Properties();
		final String DRIVERNAME = "someDriverName";
		p.setProperty("foo.DBDriver", DRIVERNAME);
		final String DBURL = "a nice long dburl";
		p.setProperty("foo.DBURL", DBURL);
		final String DBUSERNAME = "db2inst1";
		p.setProperty("foo.DBUser", DBUSERNAME);
		p.setProperty("foo.DBPassword", "pocketful of mumbles");

		SimpleSQLConfiguration conf = ConnectionUtil.getConfiguration(p, "foo");

		assertEquals(DRIVERNAME, conf.getDriverName());
		assertEquals(DBURL, conf.getDbURL());
		assertEquals(DBUSERNAME, conf.getUserName());
	}
}
