package com.darwinsys.database;

/** Checked Exception DataBase Failures, so application code
 * outside of DAOs and the like does not need to know SQLException.
 */
public class DataBaseException extends RuntimeException {

	private static final long serialVersionUID = 3257572810388681269L;

	public DataBaseException() {
		super();
	}
	public DataBaseException(String msg) {
		super(msg);
	}

	public DataBaseException(String msg, Throwable e) {
		super(msg, e);
	}
}
