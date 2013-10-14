package com.darwinsys.net;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * This interface is used by the StreamServer to get a protocol handler;
 */
public interface StreamServerHandlerFactory {

	/** The server will call this when each new request arrives.
	 * This will normally instantiate and return a protocol-specific handler,
	 * but is allowed to share instance(s) if they are thread-safe.
	 * @param serverSock The server socket, so the factory can tell which port number
	 *  is in use and infer the protocol if necessary
	 * @param clientSocket The connected client socket, so the factory can make any
	 *  necessary inferences about this client.
	 * @return The handler for this client connection.
	 */
	public StreamServerHandler getHandler(ServerSocket serverSock, Socket clientSocket);
}
