package com.darwinsys.sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import com.darwinsys.database.DataBaseException;
import com.darwinsys.util.Verbosity;

/** Encapsulate the Connection-related operations that every
 * JDBC program seems to use.
 */
public class ConnectionUtil {
	/** The default config filename, relative to CLASSPATH and/or ${user.home} */
	public static final String DEFAULT_NAME = ".db.properties";
	/** The current config filename */
	private static String userHomeConfigFileName =
		System.getProperty("user.home") + File.separator + DEFAULT_NAME;
	/** The actual config file used */
	private static String configFilePath = null;
	private static Verbosity verbosity = Verbosity.QUIET;
	/** The contents of the current Configuration file */
	private static final Properties properties = new Properties();
	
	/** Sets the full path of the config file to read.
	 * @param configFileName The FileName of the configuration file to use.
	 */
	public static void setConfigFileName(String configFileName) {
		File file = new File(configFileName);
		if (!file.canRead()) {
			throw new IllegalArgumentException("Unreadable: " + configFileName);
		}
		try (InputStream is = new FileInputStream(file.getCanonicalPath())) {
			properties.load(new FileInputStream(file.getCanonicalPath()));
			// set saved filename to canonical path, if it loads OK
			configFilePath = file.getCanonicalPath();
		} catch (IOException ex) {
			throw new IllegalArgumentException("IO error reading config file: " + configFileName);
		}
	}

	/** Get a SimpleSQLConfiguration for the given config using the default or set property file name
	 * @param config the name of the db.properties configuration entry to use
	 * @return The corresponding Configuration object
	 */
	public static Configuration getConfiguration(String config) throws DataBaseException {
		ensurePropertiesLoaded();
		return getConfiguration(properties, config);
	}

	/**
	 * Get a configuration by name
	 * @param p The Properties file
	 * @param config The name of the wanted configuration
	 * @return The matching configuration
	 */
	static SimpleSQLConfiguration getConfiguration(final Properties p, final String config) {
		final String db_url = p.getProperty(config  + "." + "DBURL");
		final String db_driver = p.getProperty(config  + "." + "DBDriver");
		final String db_user = p.getProperty(config  + "." + "DBUser");
		final String db_password = p.getProperty(config  + "." + "DBPassword");
		if (db_driver == null || db_url == null) {
			throw new DataBaseException("Driver or URL null: " + config);
		}
		return new SimpleSQLConfiguration(config, db_url, db_driver, db_user, db_password);
	}

	/**
	 * Get a Connection for the given config using the default or set property file name
	 * @param config The name of the wanted configuration
	 * @return The matching configuration
	 */
	public static Connection getConnection(final String config) throws DataBaseException {
		ensurePropertiesLoaded();
		return getConnection(properties, config);
	}

	/**
	 * Get a Connection for the given config name from a provided Properties 
	 * @param p The Properties for teh configuration
	 * @param configName The name of the wanted configuration
	 * @return The matching configuration
	 */
	public static Connection getConnection(Properties p,  String configName) throws DataBaseException {
		try {
			String db_url = p.getProperty(configName  + "." + "DBURL");
			if (db_url == null) {
				throw new DataBaseException(configName + ".DBURL is null in " + configFilePath);
			}
			String db_driver = p.getProperty(configName  + "." + "DBDriver");
			if (db_driver == null) {
				throw new DataBaseException(configName + ".DBDriver is null in " + configFilePath);
			}
			// User and pass may be optional
			String db_user = p.getProperty(configName  + "." + "DBUser");
			String db_password = p.getProperty(configName  + "." + "DBPassword");
			return getConnection(db_url, db_driver, db_user, db_password);
		} catch (ClassNotFoundException ex) {
			throw new DataBaseException(ex.toString());
		} catch (SQLException ex) {
			throw new DataBaseException(ex.toString());
		}
	}

	public static Connection getConnection(String dbUrl, String dbDriver,
					String dbUserName, String dbPassword)
			throws ClassNotFoundException, SQLException {
		ensurePropertiesLoaded();
		// Load the database driver
		if (verbosity != Verbosity.QUIET) {
			System.out.println("Loading driver " + dbDriver);
		}
		Class.forName(dbDriver);

		if (verbosity != Verbosity.QUIET) {
			System.out.println("Connecting to DB " + dbUrl);
		}
		return DriverManager.getConnection(
			dbUrl, dbUserName, dbPassword);
	}

	public static Connection getConnection(Configuration c) throws ClassNotFoundException, SQLException {
		return getConnection(c.getDbURL(), c.getDriverName(), c.getUserName(), c.getPassword());
	}

	/** Generate a Set&lt;String&gt; of the config names available
	 * from the current configuration file.
	 * @return Set&lt;String&gt; of the configurations
	 */
	public static Set<String> getConfigurationNames() {
		ensurePropertiesLoaded();
		Set<String> configNames = new TreeSet<String>();
		for (String element : properties.stringPropertyNames()) {
			int offset;
			if ((offset = element.indexOf('.')) == -1)
				continue;
			String configName = element.substring(0, offset);
			configNames.add(configName);
		}
		return configNames;
	}

	/**
	 * Load the Properties file. Prefer setConfigFilePath argument, then classpath, then ~/.db.properties.
	 */
	private static void ensurePropertiesLoaded() {
		if (properties.size() == 0) {
			// if loaded, assume done here or in setConfigFilePath().
			InputStream inputStream = null;
			try {
				URL url = ConnectionUtil.class.getResource("/" + DEFAULT_NAME);
				if (url != null) {
					configFilePath = url.toString();
					inputStream = ConnectionUtil.class.getResourceAsStream("/" + DEFAULT_NAME);
				}
				if (inputStream == null) {
					inputStream = new FileInputStream(configFilePath = userHomeConfigFileName);
				}
				properties.load(inputStream);
			} catch (IOException ex) {
				throw new DataBaseException(ex.toString());
			} finally {
				// Cannot use try-with-finally due to multiple sources of "inputStream" above
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException ex) {
						// you lose
					}
				}
			}
		}
	}

	/**
	 * @return all the configurations as SimpleSQLConfiguration objects
	 */
	public static List<Configuration> getConfigurations() {
		ensurePropertiesLoaded();
		List<Configuration> configs = new ArrayList<Configuration>();
		for (String name : getConfigurationNames()) {
			configs.add(getConfiguration(name));
		}
		return configs;
	}
	
	/** Convert a TransactionIsolation int (defined in java.sql.Connection)
	 * to the corresponding printable string.
	 * @param txisolation The input level
	 * @return the printable version of the name
	 */
	public static String transactionIsolationToString(int txisolation) {
		switch(txisolation) {
			case Connection.TRANSACTION_NONE: 
				// transactions not supported.
				return "TRANSACTION_NONE";
			case Connection.TRANSACTION_READ_UNCOMMITTED: 
				// All three phenomena can occur
				return "TRANSACTION_NONE";
			case Connection.TRANSACTION_READ_COMMITTED: 
			// Dirty reads are prevented; non-repeatable reads and 
			// phantom reads can occur.
				return "TRANSACTION_READ_COMMITTED";
			case Connection.TRANSACTION_REPEATABLE_READ: 
				// Dirty reads and non-repeatable reads are prevented;
				// phantom reads can occur.
				return "TRANSACTION_REPEATABLE_READ";
			case Connection.TRANSACTION_SERIALIZABLE:
				// All three phenomena prvented; slowest!
				return "TRANSACTION_SERIALIZABLE";
			default:
				throw new IllegalArgumentException(
					txisolation + " not a valid TX_ISOLATION");
		}
	}

	public static Verbosity getVerbosity() {
		return verbosity;
	}

	public static void setVerbosity(Verbosity verbosity) {
		ConnectionUtil.verbosity = verbosity;
	}
}
