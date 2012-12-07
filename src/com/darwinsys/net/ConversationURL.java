package com.darwinsys.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.codec.binary.Base64;

/**
 * Read a URL's data from a URLConnection
 */
public class ConversationURL {
	
	/** Run a conversation and return the result, with no
	 * postBody and no credentials.
	 * @param host The web service site address
	 * @param port the port number (usually 80)
	 * @param path The servlet or component path
	 * @return The result of the conversation.
	 * @throws IOException 
	 */
	 public static String converse(String host, int port, String path) throws IOException {
		 return converse(host, port, path, null, null, null);
	 }
	
	 /** Run a conversation and return the result, with
	  * the given postBody and no credentials.
	  * @param host The web service site address
	  * @param port the port number (usually 80)
	  * @param path The servlet or component path
	  * @param postBody The body to send (null == use GET)
	  * @param userName Credentials
	  * @param password Credentials
	  * @return The result of the conversation.
	  * @throws IOException 
	  */
	 public static String converse(String host, int port, String path, String postBody, String userName, String password) throws IOException {
		 URL url = new URL("http", host, 80, path);
		 return converse(url, postBody, userName, password);
	 }
	
	 /** Run a conversation and return the result as a (long) String, with inputs
	  * the given postBody and credentials.
	  * @param url The URL for the restful web service
	  * @param postBody The body to send (null == use GET)
	  * @param userName Credentials
	  * @param password Credentials
	  * @return The result of the conversation.
	  * @throws IOException 
	  */
	 public static String converse(URL url, String postBody, String userName, String password) throws IOException {
		 InputStream converseStream = converseStream(url, postBody, userName, password);

		StringBuilder sb = new StringBuilder();
		BufferedReader in = new BufferedReader(
				new InputStreamReader(converseStream));
		String line;

		while ((line = in.readLine()) != null) {
			sb.append(line);
		}

		in.close();
		return sb.toString();
	}
	
	 /** Run a conversation and return the resulting InputStream, with inputs
	  * the given postBody and credentials.
	  * @param url The URL for the restful web service
	  * @param postBody The body to send (null == use GET)
	  * @param userName Credentials
	  * @param password Credentials
	  * @return The result of the conversation.
	  * @throws IOException 
	  */
	 public static InputStream converseStream(URL url, String postBody, String userName, String password) throws IOException {
		URLConnection conn = url.openConnection();
		if (userName != null || password != null) {
			String cred = userName + ":" + password;
			String auth = new String(Base64.encodeBase64(cred.getBytes()));
			conn.setRequestProperty("Authorization", auth);
		}
		boolean post = postBody != null;
		if (post) {
			conn.setDoInput(true);
		}
		conn.setDoOutput(true);
		conn.setAllowUserInteraction(true);
		
		conn.connect();
		
		if (post) {
			PrintWriter out = new PrintWriter(conn.getOutputStream());
			out.println(postBody);
			out.close();
		}
		return conn.getInputStream();
	 }
		
}
