package mock;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

/** Very simple mock object that can be loaded as a JDBC Driver.
 * @author ian
 */
public class MockJdbcDriver implements Driver {

	public Connection connect(String arg0, Properties arg1) throws SQLException {
		return null;
	}

	public boolean acceptsURL(String arg0) throws SQLException {
		return false;
	}

	public DriverPropertyInfo[] getPropertyInfo(String arg0, Properties arg1)
			throws SQLException {
		return null;
	}

	public int getMajorVersion() {
		return 57;
	}

	public int getMinorVersion() {
		return 42;
	}

	public boolean jdbcCompliant() {
		return false;
	}

}
