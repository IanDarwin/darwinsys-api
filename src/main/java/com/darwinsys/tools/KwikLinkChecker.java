package com.darwinsys.tools;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

/** A very minimal link checker. 
 * Typical usage: java -cp darwinsys-api.jar com.darwinsys.net.KwikLinkChecker
 * @author Ian Darwin
 */
public class KwikLinkChecker {
	
	static boolean verbose;

	public static void main(String[] args) {
		for (String arg : args) {
			if (arg.equals("-v")) {
				verbose = true;
				continue;
			}
			LinkStatus stat = check(arg);
			if (verbose || !stat.ok)
				System.out.println(stat.message);
		}
	}
	
	static LinkStatus check(String urlString) {
		URL url;
		HttpURLConnection conn = null;
		HttpURLConnection.setFollowRedirects(false);
		try {
			url = new URL(urlString);
			conn = (HttpURLConnection) url.openConnection();
			switch (conn.getResponseCode()) {
			case 200:
				return new LinkStatus(true, urlString);
			case 403:
				return new LinkStatus(false,"403: " + urlString );
			case 404:
				return new LinkStatus(false,"404: " + urlString );
			}
			conn.getInputStream();
			return new LinkStatus(true, urlString);
		} catch (IllegalArgumentException | MalformedURLException e) {
			// Oracle JDK throws IAE if host can't be determined from URL string
			return new LinkStatus(false, "Malformed URL: " + urlString);
		} catch (UnknownHostException e) {
			return new LinkStatus(false, "Host invalid/dead: " + urlString);
		} catch (FileNotFoundException e) {
			return new LinkStatus(false,"NOT FOUND (404) " + urlString);
		} catch (ConnectException e) {
			return new LinkStatus(false, "Server not listening: " + urlString);
		} catch (IOException e) {
			return new LinkStatus(false, e.toString()); // usually includes failing URL
		} catch (Exception e) {
			return new LinkStatus(false, "Unexpected exception! " + e);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}
}
