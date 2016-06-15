package edu.uchicago.cs;

import java.io.*;
import java.net.Socket;
import java.net.URLConnection;
import java.util.logging.Logger;

public class RequestHandler implements Runnable {
	private static final Logger logger = Logger.getLogger(RequestHandler.class.getCanonicalName());

	private Socket clientSocket;

	public RequestHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	/**
	 * When an object implementing interface <code>Runnable</code> is used
	 * to create a thread, starting the thread causes the object's
	 * <code>run</code> method to be called in that separately executing
	 * thread.
	 * <p>
	 * The general contract of the method <code>run</code> is that it may
	 * take any action whatsoever.
	 *
	 * @see Thread#run()
	 */
	@Override
	public void run() {
		logger.info("start processing clientSocket");
		Request request = null;
		Response response = null;
		try {
			// get socket stream
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), Response.CHAR_SET_DEFAULT));

			while (!clientSocket.isClosed()) {
				// read request header
				StringBuilder stringBuilder = new StringBuilder();
				String line;
				while (!(line = in.readLine()).equals("")) { // consume other lines
					stringBuilder.append(line).append(System.lineSeparator());
				}   // @TODO check blocking + already has line separator
				request = new Request(stringBuilder.toString());

				// GET and HEAD
				if (request.isSupportedHttpGet() || request.isSupportedHttpHead()) {
					// parse file name
					String fileName = request.getPath();   // remove the first char '/'
					File file = new File(Server.ROOT_DIR, fileName);
					if (file.isDirectory()) {   // if it's a dir, get its index file
						file = new File(Server.ROOT_DIR, fileName + Server.INDEX_FILE_NAME);
					}
					String contentType = URLConnection.getFileNameMap().getContentTypeFor(fileName);

					// process request
					boolean isRedirect = RedirectMap.isRedirect("/" + fileName);
					if (isRedirect) {   // redirect
						String redirectLink = RedirectMap.getRedirectLink("/" + fileName);
						response = new Response301(request, clientSocket, redirectLink);
					} else {    // regular file
						try {
							boolean requestRedirectFile = file.getCanonicalPath().equals(new File(Server.ROOT_DIR, Server.REDIRECT_FILE_NAME).getCanonicalPath());
							boolean canRequestFile = file.canRead() && file.getCanonicalPath().startsWith(new File(Server.ROOT_DIR).getCanonicalPath());
							if (requestRedirectFile) {  // request redirect file, 404
								response = new ResponseError(request, clientSocket, 404);
							} else if (canRequestFile) {    // request normal file, 200
								response = new Response200(request, clientSocket, file);
							} else {  // can not find it, 404
								response = new ResponseError(request, clientSocket, 404);
							}
						} catch (IOException e) {
							e.printStackTrace();
							logger.warning("IOException for request file " + file.getName());
							response = new ResponseError(request, clientSocket, 404);
						}
					}
				} else {  // not GET and HEAD
					response = new ResponseError(request, clientSocket, 403);
				}

				// send reponse and close socket if necessary
				response.send();
				if (!request.isPersistentConnection()) {
					clientSocket.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.warning("IOException for client socket connection.");
		} finally {
			try {
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
