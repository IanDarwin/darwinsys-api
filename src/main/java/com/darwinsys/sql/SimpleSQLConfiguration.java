package com.darwinsys.sql;

/**
 * A SimpleSQLConfiguration is a POJO that represents the
 * standard four JDBC paramaters as Strings,
 * and has an optional name.
 */
public class SimpleSQLConfiguration implements Configuration {
	protected String name;
	protected String dbURL;
	protected String dbDriverName;
	protected String dbUserName;
	protected String dbPassword;

	/**
	 * Construct a SimpleSQLConfiguration object with a name and parameters
	 * @param name Name for display
	 * @param dbURL JDBC URL
	 * @param dbDriverName Driver class name
	 * @param dbUserName DB User Login
	 * @param dbPassword DB User password
	 */
	public SimpleSQLConfiguration(String name, String dbURL, String dbDriverName, String dbUserName,
			String dbPassword) {
		super();
		this.name = name;
		this.dbURL = dbURL;
		this.dbDriverName = dbDriverName;
		this.dbUserName = dbUserName;
		this.dbPassword = dbPassword;
		if (dbURL == null || dbDriverName == null || dbUserName == null || dbPassword == null) {
			throw new NullPointerException(String.format(
				"Config %s, dbURL %s, driver %s, login %s - none shall pass as null",
				name, dbURL, dbDriverName, dbUserName));
		}
	}

	/**
	 * Construct a SimpleSQLConfiguration object with no name
	 * @param dbURL JDBC URL
	 * @param dbDriverName Driver class name
	 * @param dbUserName DB User Login
	 * @param dbPassword DB User password
	 */
	public SimpleSQLConfiguration(String dbURL, String dbDriverName, String dbUserName,
			String dbPassword) {
		this(null, dbURL, dbDriverName, dbUserName, dbPassword);
	}

	@Override
	public String toString() {
		return name != null ? name : super.toString();
	}

	/* (non-Javadoc)
	 * @see com.darwinsys.sql.Configuration#getDbDriverName()
	 */
	public String getDriverName() {
		return dbDriverName;
	}

	/* (non-Javadoc)
	 * @see com.darwinsys.sql.Configuration#setDbDriverName(java.lang.String)
	 */
	public void setDriverName(String dbDriverName) {
		this.dbDriverName = dbDriverName;
	}

	/* (non-Javadoc)
	 * @see com.darwinsys.sql.Configuration#getDbPassword()
	 */
	public String getPassword() {
		return dbPassword;
	}

	/* (non-Javadoc)
	 * @see com.darwinsys.sql.Configuration#hasPassword()
	 */
	public boolean hasPassword() {
		return dbPassword != null && dbPassword.length() > 0;
	}

	/* (non-Javadoc)
	 * @see com.darwinsys.sql.Configuration#setDbPassword(java.lang.String)
	 */
	public void setPassword(String dbPassword) {
		if (dbPassword == null) {
			this.dbPassword = null;
		} else {
			this.dbPassword = dbPassword.trim();
		}
	}

	/* (non-Javadoc)
	 * @see com.darwinsys.sql.Configuration#getDbURL()
	 */
	public String getDbURL() {
		return dbURL;
	}

	/* (non-Javadoc)
	 * @see com.darwinsys.sql.Configuration#setDbURL(java.lang.String)
	 */
	public void setDbURL(String dbURL) {
		this.dbURL = dbURL;
	}

	/* (non-Javadoc)
	 * @see com.darwinsys.sql.Configuration#getDbUserName()
	 */
	public String getUserName() {
		return dbUserName;
	}

	/* (non-Javadoc)
	 * @see com.darwinsys.sql.Configuration#setDbUserName(java.lang.String)
	 */
	public void setUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
