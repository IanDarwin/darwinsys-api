package regress;

import com.darwinsys.sql.ConnectionUtil;
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
}
