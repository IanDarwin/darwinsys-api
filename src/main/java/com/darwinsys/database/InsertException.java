package com.darwinsys.database;

/**
 * Unchecked Exception for failed insertions
 */
public class InsertException extends DataBaseException {

	private static final long serialVersionUID = 3835151761840287795L;
	
	public InsertException() {
		super();
	}

	public InsertException(String msg) {
		super(msg);
	}

	public InsertException(String msg, Throwable e) {
		super(msg, e);
	}
}
