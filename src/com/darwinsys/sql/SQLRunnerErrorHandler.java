package com.darwinsys.sql;

import java.sql.SQLException;

public interface SQLRunnerErrorHandler {

/**
 * Handles an error encountered by the SQLRunner while trying to execute
 * a query.
 *
 * @param e The SQLException thrown by Statement.execute().
 */
public void handleError(SQLException e);

}
