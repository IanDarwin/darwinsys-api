package com.darwinsys.mail;

/** Checked Exception for Mail Failures (so rest of code does not
 * need to import javax.mail).
 */
public class MailException extends RuntimeException {
	public MailException() {
		super();
	}
	public MailException(String msg) {
		super(msg);
	}
}
