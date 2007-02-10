package com.darwinsys.net;

import java.io.IOException;
import java.net.Socket;

/**
 * The general contract of a handler for the StreamServer.
 */
public interface StreamServerHandler {

	/** This is called for each request */
	public void handle(Socket sock) throws IOException;
}
