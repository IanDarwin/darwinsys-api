package com.darwinsys.security;

import java.util.Base64;

/**
 * Simple class for creating BasicAuth Authorization header value from name and passwd
 */
public class BasicAuth {

	private final static Base64.Encoder encoder = Base64.getEncoder();

	/**
	 * Given a username and password, returns the complete value part
	 * of the basic-auth header, "Basic" + " " + base64(username+":"+passwd)
	 * @param username The username
	 * @param password The password
	 * @return The finished header.
	 */
	public static String makeHeader(String username, String password) {
		return String.format("Basic %s",
			encoder.encodeToString((username + ":" + password).getBytes()));
	}
}
