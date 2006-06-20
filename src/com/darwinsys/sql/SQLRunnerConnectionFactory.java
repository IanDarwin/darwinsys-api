package com.darwinsys.sql;

import java.sql.Connection;
import java.util.List;

/**
 * The contract that an application/environment has to provide to SQLRunner
 * to obtain JDBC Connections.
 */
public interface SQLRunnerConnectionFactory {
	
    /**
     * Gets a list of valid Connections.
     * @return A List of objects that are suitable for display in a JComboBox
     * (that is, either String, or Component, or has a user-friendly toString() method).
     */
    List<Object> getConnectionsList();
    
    /**
     * Obtain a Connection (presumably from a Connection Pool); the Connection
     * will be closed when SQLRunner is done with it.
     * @param conn An object previously provided by getConnectionsList().
     * @return
     */
    Connection getConnection(Object conn);
}
