package com.darwinsys.database;

/** Checked Exception DataBase Failures (so most of code does not
 * need to import SQLException).
 */
public class DataBaseException extends RuntimeException {
	public DataBaseException() {
		super();
	}
	public DataBaseException(String msg) {
		super(msg);
	}
}
