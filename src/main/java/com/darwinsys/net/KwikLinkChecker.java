package com.darwinsys.net;

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

	public static void main(String[] args) {
		for (String url : args) {
			check(url);
		}
	}
	
	static LinkStatus check(String urlString) {
		URL url;
		try {
			url = new URL(urlString);
			HttpURLConnection.setFollowRedirects(false);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.getInputStream();
			return new LinkStatus(true, urlString);
		} catch (IllegalArgumentException | MalformedURLException e) {
			// Oracle JDK throws IAE if host can't be determined from URL string
			return new LinkStatus(false, "Malformed URL: " + urlString);
		} catch (UnknownHostException e) {
			return new LinkStatus(false, "Host invalid/dead: " + urlString);
		} catch (FileNotFoundException e) {
			return new LinkStatus(false,"NOT FOUND (404) " + urlString );
		} catch (ConnectException e) {
			return new LinkStatus(false, "Server not listening: " + urlString);
		} catch (IOException e) {
			return new LinkStatus(false, e + ": " + urlString);
		}
	}
}
