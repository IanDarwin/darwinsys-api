package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * Read a URL's data from a URLConnection
 */
public class ConversationURL {
	    
	    public static String converse(String host, int port, String path, String postBody) throws IOException {
			URL url = new URL("http", host, 80, path);
	        URLConnection conn = url.openConnection();
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
