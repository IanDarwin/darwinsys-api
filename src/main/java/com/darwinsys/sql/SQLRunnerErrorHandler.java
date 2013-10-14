package com.darwinsys.sql;


public interface SQLRunnerErrorHandler {

	/**
	 * Called by SQLRunner to handle an error encountered while trying to execute
	 * a command.
	 * @param e The Exception thrown by Statement.execute().
	 */
	public void handleError(Exception e);

}
