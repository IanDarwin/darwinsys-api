package com.darwinsys.mail;

/** Checked Exception for Mail Failures (so rest of code does not
 * need to import javax.mail).
 */
public class MailException extends RuntimeException {

	private static final long serialVersionUID = 3979271360864661817L;

	public MailException() {
		super();
	}
	public MailException(String msg) {
		super(msg);
	}
}
