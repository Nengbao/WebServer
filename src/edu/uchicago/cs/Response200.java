package edu.uchicago.cs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URLConnection;

public class Response200 extends Response {
	private File file;

	public Response200(Request request, Socket clientSocket, File file) {
		setUp(request, clientSocket);

		this.file = file;
		this.doSendBody = request.isSupportedHttpGet();
		statusCode = 200;
		contentType = URLConnection.getFileNameMap().getContentTypeFor(file.getName());
	}

	@Override
	protected String extraHeader() {
		return "";
	}

	@Override
	protected long getContentLength() {
		return file.length();
	}

	@Override
	protected void sendBody() {
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		int len = 0;

		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
			while ((len = fileInputStream.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
