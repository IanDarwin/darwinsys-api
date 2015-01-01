package com.darwinsys.preso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class QRFormatter {

	private static int DEFAULT_SIZE = 128;
	private static int size = DEFAULT_SIZE;

	public static String format(String url) {
		String encodedURL = url;
		try {
			encodedURL = URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("GOOFY! " + e);	// CANTHAPPEN
		}
		return String.format(
			"http://chart.apis.google.com/chart?cht=qr&chs=%d&chl=%s",
			size, encodedURL);
	}

	public int getWidth() {
		return size;
	}

	public int getHeight() {
		return size;
	}
}
