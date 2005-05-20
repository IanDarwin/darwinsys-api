package com.darwinsys.mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.naming.NamingException;

import com.darwinsys.net.DNSUtils;

public class SmtpUtils {
	private String myDnsServer;
	public static final int SMTP_PORT = 25;
	private String myHostName;
	
	public SmtpUtils(String myHostName, String myDnsServer) {
		this.myHostName = myHostName;
		this.myDnsServer= myDnsServer;
	}

	public boolean verifySender(String user, String host) throws IOException{
		PrintWriter out = null;
		try {
		String mxHost = new DNSUtils(myDnsServer).findMX(host);

		Socket s = new Socket(mxHost, SMTP_PORT);
		BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);
		String greeting = readLine(in);
		send(out, "HELO " + myHostName);
		String heloResp = readLine(in);
		send(out, "MAIL From:<smtp_verifier@" + myHostName + ">");
		String mailResp = readLine(in);
		user="postmaster";
		send(out, "RCPT To:<" + user + "@" + host + ">");
		String vrfyResp = readLine(in);
		
		if (vrfyResp.startsWith("550")) {
			return false;
		}
		
		send(out, "QUIT");
		String quitResp = readLine(in);

		return true;
		} catch (UnknownHostException e) {
			return false;
		} catch (NoRouteToHostException e) {
			return false;
		} catch (NamingException e) {
			e.printStackTrace();
			return false; // Should be trinary, for "unknown"?
		} finally {
			if (out != null)
			out.close();
		}
	}
	public static void send(PrintWriter out, String mesg) {
		System.out.print(">>> ");
		System.out.print(mesg);
		System.out.println(" <<<");
		out.print(mesg + "\r\n");
		out.flush();
	}
	public static String readLine(BufferedReader is) throws IOException {
		String response = is.readLine();
		System.out.print("<<< ");
		System.out.println(response);
		return response;
	}
}
