package regress;

import com.darwinsys.database.DataBaseException;
import com.darwinsys.sql.ConnectionUtil;

import java.sql.Connection;
import java.util.*;

import junit.framework.TestCase;

/**
 * name - purpose
 * @version $Id$
 */
public class ConnectionUtilTest extends TestCase {
	public void testList() throws Exception {
		System.out.println("ConnectionUtilTest.testList()");
		Set configs = ConnectionUtil.list();
		Iterator iter = configs.iterator();
		boolean hasConfigNames = false;
		while (iter.hasNext()) {
			String element = (String) iter.next();
			System.out.println(element);
			hasConfigNames = true;
		}
		assertTrue(hasConfigNames);
	}
	
	public void testGetConnection() throws Exception {
		try {
			Connection c = ConnectionUtil.getConnection("url", "mydriver", 
					"operator", "secret");
			fail("getConnection w/ bad params Did not throw exception");
		} catch (ClassNotFoundException nfe) {
			String m = nfe.getMessage();
			assertEquals("failing driver class name", "mydriver", m);
			System.out.println("Caught expected ClassNotFoundException");
		} catch (DataBaseException dbe) {		
			fail("Caught wrong exception " + dbe + "; check order of params");
			
		}
	}
}
