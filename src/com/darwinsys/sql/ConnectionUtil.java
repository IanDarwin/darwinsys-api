package com.darwinsys.sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import com.darwinsys.database.DataBaseException;

/** Encapsulate the Connection-related operations that every 
 * JDBC program seems to use.
 */
public class ConnectionUtil {
	/** The default config filename, relative to ${user.home} */
	public static final String DEFAULT_NAME = ".db.properties";
	
	/** The current config filename */
	private static String configFileName =
		System.getProperty("user.home") + File.separator + DEFAULT_NAME;

	/** Get a Configuration for the given config using the default or set property file name */
	public static  Configuration getConfiguration(String config) throws DataBaseException {
		try {
			Properties p = new Properties();
			p.load(new FileInputStream(configFileName));
			return getConfiguration(p, config);
		} catch (IOException ex) {
			throw new DataBaseException(ex.toString());
		}
	}
	
	/**
	 * @param p The Properties file
	 * @param config The name of the wanted configuration
	 * @return The matching configuration
	 */
	private static Configuration getConfiguration(Properties p, String config) {
		String db_driver = p.getProperty(config  + "." + "DBDriver");
		String db_url = p.getProperty(config  + "." + "DBURL");
		String db_user = p.getProperty(config  + "." + "DBUser");
		String db_password = p.getProperty(config  + "." + "DBPassword");
		if (db_driver == null || db_url == null) {
			throw new DataBaseException("Driver or URL null: " + config);
		}
		return new Configuration(db_url, db_driver, db_user, db_password);
	}

	/** Get a Connection for the given config using the default or set property file name */
	public static Connection getConnection(String configName) throws DataBaseException {
		try {
			Properties p = new Properties();
			p.load(new FileInputStream(configFileName));
			return getConnection(p, configName);
		} catch (IOException ex) {
			throw new DataBaseException(ex.toString());
		}
	}
	
	/** Get a Connection for the given config name from a provided Properties */
	public static Connection getConnection(Properties p,  String configName) throws DataBaseException {
		try {
			String db_driver = p.getProperty(configName  + "." + "DBDriver");
			String db_url = p.getProperty(configName  + "." + "DBURL");
			String db_user = p.getProperty(configName  + "." + "DBUser");
			String db_password = p.getProperty(configName  + "." + "DBPassword");
			if (db_driver == null || db_url == null) {
				throw new DataBaseException("Driver or URL null: " + configName);
			}
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

		// Load the database driver
		System.out.println("Loading driver " + dbDriver);
		Class.forName(dbDriver);

		System.out.println("Connecting to DB " + dbUrl);
		return DriverManager.getConnection(
			dbUrl, dbUserName, dbPassword);
	}
	
	public static Connection getConnection(Configuration c) throws ClassNotFoundException, SQLException {
		return getConnection(c.dbDriverName, c.dbURL, c.dbUserName, c.dbPassword);
	}
	
	/** Returns the full path of the configuration file being used.
	 * @return Returns the configFileName.
	 */
	public static String getConfigFileName() {
		return configFileName;
	}
	
	/** Sets the full path of the config file to read.
	 * @param configFileNam The FileName of the configuration file to use.
	 */
	public static void setConfigFileName(String configFileNam) {
		configFileName = configFileNam;
		File file = new File(configFileName);
		if (!file.canRead()) {
			throw new IllegalArgumentException("Unreadable: " + configFileName);
		}
		try {
			ConnectionUtil.configFileName = file.getCanonicalPath();
		} catch (IOException ex) {
			System.err.println("Warning: IO error checking path: " + configFileName);
			ConnectionUtil.configFileName = configFileName;
		}
	}
	
	/** Generate a Set<String> of the config names available
	 * from the current configuration file.
	 * @return Set<String> of the configurations
	 */
	public static Set<String> list() {
		Set<String> configNames = new TreeSet<String>();
		try {
			Properties p = new Properties();
			p.load(new FileInputStream(configFileName));
			Enumeration enumeration = p.keys();
			while (enumeration.hasMoreElements()) {
				String element = (String) enumeration.nextElement();
				int offset;
				if ((offset= element.indexOf('.')) == -1)
					continue;
				String configName = element.substring(0, offset);
				//System.out.println(configName);
				configNames.add(configName);
				
			}
		} catch (IOException ex) {
			throw new DataBaseException(ex.toString());
		}
		return configNames;
	}
}
