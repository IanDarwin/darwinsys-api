package com.darwinsys.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestUtils {

	public static String md5(String data) {
		byte[] bytes = data.getBytes();
		try {
			MessageDigest md = 
				(MessageDigest) MessageDigest.getInstance("MD5");
			md.update(bytes, 0, bytes.length);	// digest it

			// Digest the credentials.
			byte[] digest = md.digest();

			// return as hex string.
			return toHex(digest);

		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.toString());
		}
	}

	private static final char byteToHex[] = {
		'0', '1', '2', '3', '4', '5', '6', '7',
		'8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
	};

	/** Convert an array of bytes to a hex string. */
	public static final String toHex(byte bytes[]) {
		assert bytes != null : "invalid input to toHex()";
		StringBuffer sb = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			// System.out.println("input: " + bytes[i] + " ");
			sb.append((char)byteToHex[(bytes[i] >> 4) & 0x0f]);
			sb.append((char)byteToHex[bytes[i] & 0x0f]);
		}
		return (sb.toString());
	}
}
