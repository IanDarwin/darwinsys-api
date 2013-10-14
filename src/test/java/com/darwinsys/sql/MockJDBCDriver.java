package com.darwinsys.sql;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

public class MockJDBCDriver implements Driver {

	static {
		try {
			DriverManager.registerDriver(new MockJDBCDriver());
		} catch (SQLException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	public boolean acceptsURL(String url) throws SQLException {
		return true;
	}

	public Connection connect(String url, Properties info) throws SQLException {
		return new MockJDBCConnection();
	}

	public int getMajorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getMinorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean jdbcCompliant() {
		// TODO Auto-generated method stub
		return false;
	}

}
