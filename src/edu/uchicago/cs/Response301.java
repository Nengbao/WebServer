package edu.uchicago.cs;

import java.net.Socket;

public class Response301 extends Response {
	private String redirectLink;

	public Response301(Request request, Socket clientSocket, String redirectLink) {
		setUp(request, clientSocket);

		this.redirectLink = redirectLink;
		statusCode = 301;
		contentType = "text/html; charset=utf-8";
		doSendBody = false;
	}


	@Override
	protected String extraHeader() {
		return "Location: " + redirectLink + "\r\n";
	}

	@Override
	protected long getContentLength() {
		return 0;
	}

	@Override
	protected void sendBody() {

	}
}
