package com.darwinsys.sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

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

	/** Get a Connection for the given config using the default or set property file name */
	public static Connection getConnection(String config) throws DataBaseException {
		try {
			Properties p = new Properties();
			p.load(new FileInputStream(configFileName));
			return getConnection(p, config);
		} catch (IOException ex) {
			throw new DataBaseException(ex.toString());
		}
	}
	
	/** Get a Connection for the given config name from a provided Properties */
	public static Connection getConnection(Properties p,  String config) throws DataBaseException {
		try {
			String db_driver = p.getProperty(config  + "." + "DBDriver");
			String db_url = p.getProperty(config  + "." + "DBURL");
			String db_user = p.getProperty(config  + "." + "DBUser");
			String db_password = p.getProperty(config  + "." + "DBPassword");
			if (db_driver == null || db_url == null) {
				throw new IllegalStateException("Driver or URL null: " + config);
			}
			return createConnection(db_driver, db_url, db_user, db_password);
		} catch (ClassNotFoundException ex) {
			throw new DataBaseException(ex.toString());
	
		} catch (SQLException ex) {
			throw new DataBaseException(ex.toString());
		}
	}

	public static Connection createConnection(String db_driver, String db_url, 
					String db_user, String db_password)
			throws ClassNotFoundException, SQLException {


		// Load the database driver
		System.out.println("Loading driver " + db_driver);
		Class.forName(db_driver);

		System.out.println("Connecting to DB " + db_url);
		return DriverManager.getConnection(
			db_url, db_user, db_password);
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
}
