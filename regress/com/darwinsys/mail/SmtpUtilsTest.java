package com.darwinsys.mail;

import junit.framework.TestCase;

public class SmtpUtilsTest extends TestCase {
	private SmtpUtils smtp;
	
	public void setUp() {
		smtp = new SmtpUtils("localhost", "localhost");
	}
	public void testVerifySender() throws Exception {
		System.out.println("Starting Send");
		smtp.verifySender("ian", "smtp3.sympatico.ca");
	}
}
