package com.darwinsys.filtergui;

/** Simple marker class 
 * */
public class MyFilterException extends Exception {
	
	private static final long serialVersionUID = -7295013334395074021L;

	public MyFilterException() {
		super();
	}

	public MyFilterException(String message) {
		super(message);
	}

	public MyFilterException(String message, Throwable cause) {
		super(message, cause);
	}

	public MyFilterException(Throwable cause) {
		super(cause);
	}

}
