package com.darwinsys.sql;

public class SyntaxException extends Exception {

	private static final long serialVersionUID = 198771981278971L;
	
	SyntaxException() {
		super();
	}

	SyntaxException(String message) {
		super(message);
	}
}
