package com.example;

import java.io.BufferedReader;
import java.io.IOException;
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
	 * postBogy and not credentials.
	 * @param host The web service site address
	 * @param port the port number (usually 80)
	 * @param path The servlet or component path
	 * @return
	 */
	 public static String converse(String host, int port, String path) {
		 return converse(host, port, path, null, null, null);
	 }
	
	 public static String converse(String host, int port, String path, String postBody, String userName, String password) throws IOException {
		 URL url = new URL("http", host, 80, path);
		 return converse(url, postBody, userName, password);
	 }
	    
	 public static String converse(URL url, String postBody, String userName, String password) throws IOException {
		 URLConnection conn = url.openConnection();
		 if (userName != null || password != null) {
	        String auth = Base64.encodeBase64(userName + ":" + password);
	        conn.setRequestProperty("Authorization", auth);
		 }
	        boolean post = postBody != null;
	        if (post)
	        	conn.setDoInput(true);
	        conn.setDoOutput(true);
	        conn.setAllowUserInteraction(true);
	        
	        conn.connect();
		
	        if (post) {
	        	PrintWriter out = new PrintWriter(conn.getOutputStream());
	        	out.println(postBody);
	        	out.close();
	        }

	        StringBuilder sb = new StringBuilder();
	        BufferedReader in = new BufferedReader(
	        		new InputStreamReader(conn.getInputStream()));
	        String line;

	        while ((line = in.readLine()) != null) {
	            sb.append(line);
	        }

	        in.close();
	        return sb.toString();
	    }
	

}
