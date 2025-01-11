package com.darwinsys.mail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SmtpUtilsTest {
	private SmtpUtils smtp;

	@BeforeEach
	void setUp() {
		smtp = new SmtpUtils("localhost", "localhost");
	}

	@Test
	void verifySender() throws Exception {
		System.out.println("Starting Send");
		smtp.verifySender("ian", "smtp3.sympatico.ca");
	}
}
