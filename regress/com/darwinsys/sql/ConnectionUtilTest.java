package com.darwinsys;

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
		Set configs = ConnectionUtil.getConfigurations();
		Iterator iter = configs.iterator();
		boolean hasConfigNames = false;
		while (iter.hasNext()) {
			String element = (String) iter.next();
			System.out.println(element);
			hasConfigNames = true;
		}
		assertTrue(hasConfigNames);
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
