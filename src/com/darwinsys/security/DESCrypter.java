package com.darwinsys.security;

import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * DES crypt/decrypt using JSSE.
 * @author Original from Patrick Chan's Programmer's Almanac
 */
public class DESCrypter {

	private Cipher dcipher;
	
	private Cipher ecipher;

	/** 8-byte Salt for DES: this class is set up for general use;
	 * for some applications, could increase security by choosing
	 * a different random salt on every invocation (but then need
	 * to track the chosen salt if decryption must be done later).
	 * Could just export the SecretKey in that case
	 */
	private byte[] salt = { 
		(byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
		(byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03,
	};

	private final int iterationCount = 17;

	private final SecretKey key;

	/**
	 * Construct a DESCrypter for the given password
	 */
	public DESCrypter(String passPhrase) {
		try {
			// Create the SecretKey object
			KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt,
				iterationCount);
			key = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
				.generateSecret(keySpec);

			// And the Cipher objects
			ecipher = Cipher.getInstance(key.getAlgorithm());
			dcipher = Cipher.getInstance(key.getAlgorithm());

			// Prepare the parameter for the ciphers
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt,
				iterationCount);

			// Create the ciphers
			ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

		} catch (Throwable e) {
			throw new RuntimeException("DESCrypter: Constructor failure: " + e);
		}
	}
	
	private final static String PADDING = "   ";

	/**
	 * Encrypt the given string using DES
	 */
	public String encrypt(String str) {
		try {
			// Encode the string into bytes using utf-8
			byte[] utf8 = str.getBytes("UTF8");

			// Encrypt
			byte[] enc = ecipher.doFinal(utf8);

			// Encode bytes to base64 to get a string
			final String encoded = Base64Coder.encodeLines(enc);
			final int n = encoded.length();
			System.out.println("Encoded length = " + n);
			return encoded;


		} catch (Throwable e) {
			throw new RuntimeException("Encryption failure: " + e);
		}
	}

	/**
	 * Decrypt the given string
	 */
	public String decrypt(String str) {
		try {
			// Decode base64 to get bytes
			byte[] dec = Base64Coder.decode(str);

			// Decrypt
			byte[] utf8 = dcipher.doFinal(dec);

			// Decode using utf-8
			return new String(utf8, "UTF8");
		} catch (Throwable e) {
			throw new RuntimeException("Decryption failure: " + e, e);
		}

	}
}
