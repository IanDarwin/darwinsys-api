package com.darwinsys.sql;

import java.sql.Connection;
import java.util.List;

/**
 * The contract that an application/environment has to provide to SQLRunner
 * to obtain JDBC Connections.
 */
public interface ConfigurationManager {

    /**
     * Gets a list of valid Configurations.
     * @return A List of Configuration objects that are suitable for display in a JComboBox
     * (that is, has a user-friendly toString() method).
     */
    List<Configuration> getConfigurations();

    /**
     * Obtain a Connection (presumably from a Connection Pool); the Connection
     * will be closed when SQLRunner is done with it.
     * @param conn An object previously provided by getConnectionsList().
     * @return A JDBC Connection for this Configuration.
     */
    Connection getConnection(Configuration conn);
}
