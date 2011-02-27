package mock;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

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

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new IllegalStateException(
				"I hadn't planned for this method to be called");
	}

}
