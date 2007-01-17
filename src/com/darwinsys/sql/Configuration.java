package com.darwinsys.sql;

public interface Configuration {

	/**
	 * @return Returns the dbDriverName.
	 */
	public abstract String getDriverName();

	/**
	 * @param dbDriverName The dbDriverName to set.
	 */
	public abstract void setDriverName(String dbDriverName);

	/**
	 * @return Returns the dbPassword.
	 */
	public abstract String getPassword();

	/** Convenience: return true if there is a non-null, non-empty password
	 *
	 */
	public abstract boolean hasPassword();

	/**
	 * @param dbPassword The dbPassword to set.
	 */
	public abstract void setPassword(String dbPassword);

	/**
	 * @return Returns the dbURL.
	 */
	public abstract String getDbURL();

	/**
	 * @param dbURL The dbURL to set.
	 */
	public abstract void setDbURL(String dbURL);

	/**
	 * @return Returns the dbUserName.
	 */
	public abstract String getUserName();

	/**
	 * @param dbUserName The dbUserName to set.
	 */
	public abstract void setUserName(String dbUserName);

	/**
	 * @return Returns the name of this SimpleSQLConfiguration
	 */
	public abstract String getName();

	/**
	 * Sets the name of this SimpleSQLConfiguration
	 * @param name The new name.
	 */
	public abstract void setName(String name);

}