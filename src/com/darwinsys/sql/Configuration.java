package com.darwinsys.sql;

/**
 * A Configuration is a POJO that represents the standard four JDBC paramaters as Strings
 */
public class Configuration {
	protected String dbURL;
	protected String dbDriverName;
	protected String dbUserName;
	protected String dbPassword;
	
	/**
	 * @param dbURL
	 * @param dbDriverName
	 * @param dbUserName
	 * @param dbPassword
	 */
	public Configuration(String dbURL, String dbDriverName, String dbUserName,
			String dbPassword) {
		super();
		this.dbURL = dbURL;
		this.dbDriverName = dbDriverName;
		this.dbUserName = dbUserName;
		this.dbPassword = dbPassword;
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
	/**
	 * @param dbPassword The dbPassword to set.
	 */
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
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
}
