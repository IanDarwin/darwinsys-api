package com.darwinsys.database;

/** Checked Exception for failed insertions */
public class InsertException extends DataBaseException {
	public InsertException() {
		super();
	}
	public InsertException(String msg) {
		super(msg);
	}
}
