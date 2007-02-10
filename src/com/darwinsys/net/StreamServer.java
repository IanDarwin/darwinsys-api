package com.darwinsys.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * StreamServer packages up all the network handling goo for a stream socket-based server;
 * clients need only provide a StreamServerHandlerFactory and a port number.
 */
public class StreamServer implements GenericServer {

	static final int DEFAULT_THREAD_POOL_SIZE = 5;

	final int threadPoolSize;

	StreamServerHandlerFactory streamHandlerFactory;

	ServerSocket serverSock;

	int portNumber;

	private final Executor myThreadPool;

	public StreamServer(int portNumber,
			StreamServerHandlerFactory streamHandlerFactory) throws IOException {
		this.portNumber = portNumber;
		this.streamHandlerFactory = streamHandlerFactory;
		threadPoolSize = DEFAULT_THREAD_POOL_SIZE;
		myThreadPool = Executors.newFixedThreadPool(threadPoolSize);
		serverSock = new ServerSocket(portNumber);
	}

	public void runServer() {
		while (true) {
			try {
			final Socket clientSocket = serverSock.accept();
			myThreadPool.execute(new Runnable() {
				public void run() {
					StreamServerHandler handler = streamHandlerFactory.getHandler(serverSock, clientSocket);
						try {
							handler.handle(clientSocket);
						} catch (IOException e) {
							handleError(e);
						}
				}
			});
			} catch (IOException e) {
				handleError(e);
			}
		}
	}

	public void handleError(Exception e) {
		e.printStackTrace();
	}

	public int getPort() {
		return portNumber;
	}
}
