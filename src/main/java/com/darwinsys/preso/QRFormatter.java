package com.darwinsys.preso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class QRFormatter {

	private static int SIZE = 128;

	public static String format(String url) {
		String encodedURL = url;
		try {
			encodedURL = URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("GOOFY! " + e);	// CANTHAPPEN
		}
		return String.format(
			"http://chart.apis.google.com/chart?cht=qr&chs=%d&chl=%s",
			SIZE, encodedURL);
	}

	public int getWidth() {
		return SIZE;
	}

	public int getHeight() {
		return SIZE;
	}
}
