package com.darwinsys.sql;

/**
 * The interface used by all data source configurations here */
public interface Configuration {

	/**
	 * @return Returns the dbDriverName.
	 */
	public abstract String getDriverName();

	/**
	 * Set thedriver name.
	 * @param dbDriverName The dbDriverName to set.
	 */
	public abstract void setDriverName(String dbDriverName);

	/**
	 * Get the database password.
	 * @return Returns the dbPassword.
	 */
	public abstract String getPassword();

	/**
	 * Convenience routine.
	 * @return true if there is a non-null, non-empty password
	 */
	public abstract boolean hasPassword();

	/**
	 * Set the database password.
	 * @param dbPassword The dbPassword to set.
	 */
	public abstract void setPassword(String dbPassword);

	/**
	 * Get the database URL.
	 * @return Returns the dbURL.
	 */
	public abstract String getDbURL();

	/**
	 * Set the URL to contact the database.
	 * @param dbURL The dbURL to set.
	 */
	public abstract void setDbURL(String dbURL);

	/**
	 * Retrieve the database username.
	 * @return Returns the dbUserName.
	 */
	public abstract String getUserName();

	/**
	 * Set the database login name.
	 * @param dbUserName The dbUserName to set.
	 */
	public abstract void setUserName(String dbUserName);

	/**
	 * Retrieve the name of this config.
	 * @return Returns the name of this SimpleSQLConfiguration
	 */
	public abstract String getName();

	/**
	 * Sets the name of this SimpleSQLConfiguration
	 * @param name The new name.
	 */
	public abstract void setName(String name);

}
