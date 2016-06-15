package edu.uchicago.cs;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

public abstract class AbstractListener implements Runnable {
	protected static final Logger logger = Logger.getLogger(AbstractListener.class.getCanonicalName());
	protected int port;
	protected ExecutorService pool;

	public AbstractListener(ExecutorService pool, int port) {
		this.pool = pool;
		this.port = port;
	}

	protected abstract ServerSocket getServerSocket() throws Exception;

	@Override
	public void run() {
		try {
			ServerSocket serverSocket = getServerSocket();
			logger.info("Accept " + getListenerType() + " connection on port " + port);
			while (true) {
				try {
					Socket clientSocket = serverSocket.accept();
					logger.info("A " + getListenerType() + "connection is accepted from " + clientSocket.getInetAddress().getHostAddress());
					Runnable runnable = new RequestHandler(clientSocket);
					pool.submit(runnable);
				} catch (Exception e) {
					e.printStackTrace();
					logger.warning("Fail to accept a client connection");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.severe("Fail to get a server socket.");
		}
	}

	protected abstract String getListenerType();
}
