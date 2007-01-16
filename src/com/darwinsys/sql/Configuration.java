package com.darwinsys.sql;

/**
 * A Configuration is a POJO that represents the standard four JDBC paramaters as Strings,
 * and has an optional name.
 */
public class Configuration {
	protected String name;
	protected String dbURL;
	protected String dbDriverName;
	protected String dbUserName;
	protected String dbPassword;

	/**
	 * Construct a Configuration object with a name and parameters
	 * @param dbURL
	 * @param dbDriverName
	 * @param dbUserName
	 * @param dbPassword
	 */
	public Configuration(String name, String dbURL, String dbDriverName, String dbUserName,
			String dbPassword) {
		super();
		this.name = name;
		this.dbURL = dbURL;
		this.dbDriverName = dbDriverName;
		this.dbUserName = dbUserName;
		this.dbPassword = dbPassword;
	}

	/**
	 * Construct a Configuration object with no name
	 * @param dbURL
	 * @param dbDriverName
	 * @param dbUserName
	 * @param dbPassword
	 */
	public Configuration(String dbURL, String dbDriverName, String dbUserName,
			String dbPassword) {
		this(null, dbURL, dbDriverName, dbUserName, dbPassword);
	}

	@Override
	public String toString() {
		return name != null ? name : super.toString();
	}

	/**
	 * @return Returns the dbDriverName.
	 */
	public String getDbDriverName() {
		return dbDriverName;
	}
	/**
	 * @param dbDriverName The dbDriverName to set.
	 */
	public void setDbDriverName(String dbDriverName) {
		this.dbDriverName = dbDriverName;
	}
	/**
	 * @return Returns the dbPassword.
	 */
	public String getDbPassword() {
		return dbPassword;
	}

	/** Convenience: return true if there is a non-null, non-empty password
	 *
	 */
	public boolean hasPassword() {
		return dbPassword != null && dbPassword.length() > 0;
	}
	/**
	 * @param dbPassword The dbPassword to set.
	 */
	public void setDbPassword(String dbPassword) {
		if (dbPassword == null) {
			this.dbPassword = null;
		} else {
			this.dbPassword = dbPassword.trim();
		}
	}

	/**
	 * @return Returns the dbURL.
	 */
	public String getDbURL() {
		return dbURL;
	}
	/**
	 * @param dbURL The dbURL to set.
	 */
	public void setDbURL(String dbURL) {
		this.dbURL = dbURL;
	}
	/**
	 * @return Returns the dbUserName.
	 */
	public String getDbUserName() {
		return dbUserName;
	}
	/**
	 * @param dbUserName The dbUserName to set.
	 */
	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
