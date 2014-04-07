package com.darwinsys.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A basic demo of the generic StreamServer class.
 */
public class StreamServerDemo {

	/**
	 * This method must be thread-safe!
	 * @param sock
	 * @throws IOException
	 */
	private static void doConversation(Socket sock) throws IOException {
		System.out.println("StreamServerDemo.doConversation(): accepted connection");
		BufferedReader is = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		String line;
		while ((line = is.readLine()) != null) {
			System.out.println(line);
		}
		PrintWriter out = new PrintWriter(sock.getOutputStream());
		out.println("OK");
		sock.close();
	}

	/**
	 * A very minimal StreamServerHandlerFactory
	 */
	static class MyStreamHandlerFactory implements StreamServerHandlerFactory, StreamServerHandler {

		public StreamServerHandler getHandler(ServerSocket serverSock, Socket clientSocket) {
			return this;
		}

		public void handle(Socket sock) throws IOException {
			doConversation(sock);
		}

	}
	public static void main(String[] args) throws Throwable {
		StreamServer server = new StreamServer(4567, new MyStreamHandlerFactory());
		System.out.printf("Server listening in %d%n", server.getPort());
		server.runServer();
	}

}
