package com.darwinsys.tools;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URI;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpResponse.BodyHandlers;

/**
 * A very minimal link checker; checks one or more links from command line, carefully but not recursively.
 * Typical usage: java -cp darwinsys-api.jar com.darwinsys.net.KwikLinkChecker [-v] url [...]
 * @author Ian Darwin
 */
public class KwikLinkChecker {
	
	static boolean verbose;
	HttpClient client;

	public static void main(String[] args) {
		KwikLinkChecker checker = new KwikLinkChecker();
		for (String arg : args) {
			if (arg.equals("-v")) {
				verbose = true;
				continue;
			}
			if (arg.startsWith("-")) {
				System.out.println("Invalid argument: " + arg);
				continue;
			}
			LinkStatus stat = checker.check(arg);
			if (verbose || !stat.ok)
				System.out.println(stat.message);
		}
	}

	KwikLinkChecker() {
		client = HttpClient.newBuilder()
			.followRedirects(Redirect.NORMAL)
			.version(Version.HTTP_1_1)
			.build();
	}
	
	// tag::main[]
	/**
	 * Check one HTTP link; not recursive. Returns a LinkStatus with
	 * boolean success, and the filename or an error message in the
	 * message part of the LinkStatus.  The end of this method is one of
	 * the few places where a whole raft of different "catch" clauses is
	 * actually needed for the intent of the program.
	 * @param urlString the link to check
	 * @return the link's status
	 */
	@SuppressWarnings("exports")
	public LinkStatus check(String urlString) {
		try {
			HttpResponse<String> resp = client.send(
				HttpRequest.newBuilder(URI.create(urlString))
				.header("User-Agent", getClass().getName())
				.GET()
				.build(), 
				BodyHandlers.ofString());

			// Collect the results
			if (resp.statusCode() == 200) {
				System.out.println(resp.body());
			} else {
				System.out.printf("ERROR: Status %d on request %s\n",
					resp.statusCode(), urlString);
			}

			switch (resp.statusCode()) {
			case 200:
				return new LinkStatus(true, urlString);
			case 403:
				return new LinkStatus(false,"403: " + urlString );
			case 404:
				return new LinkStatus(false,"404: " + urlString );
			}
			return new LinkStatus(true, urlString);
		} catch (IllegalArgumentException | MalformedURLException e) {
			// JDK throws IAE if host can't be determined from URL string
			return new LinkStatus(false, "Malformed URL: " + urlString);
		} catch (UnknownHostException e) {
			return new LinkStatus(false, "Host invalid/dead: " + urlString);
		} catch (FileNotFoundException e) {
			return new LinkStatus(false,"NOT FOUND (404) " + urlString);
		} catch (ConnectException e) {
			return new LinkStatus(false, "Server not listening: " + urlString);
		} catch (SocketException e) {
			return new LinkStatus(false, e + ": " + urlString);
		} catch (IOException e) {
			return new LinkStatus(false, e.toString()); // includes failing URL
		} catch (Exception e) {
			return new LinkStatus(false, urlString + ": " + e);
		}
	}
	// end::main[]
}
