import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class ResponseError extends Response {
	private String body;

	public ResponseError(Request request, Socket clientSocket, int statusCode) {
		setUp(request, clientSocket);

		this.statusCode = statusCode;
		contentType = "text/html; charset=utf-8";
		doSendBody = true;
		body = getBody();
	}

	@Override
	protected String extraHeader() {
		return "";
	}

	@Override
	protected long getContentLength() {
		int length = 0;
		try {
			length = body.getBytes(CHAR_SET_DEFAULT).length;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return length;
	}

	@Override
	protected void sendBody(){
		try {
			out.write(body.getBytes(CHAR_SET_DEFAULT));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getBody(){
		String description = StatusCodeMap.getDescription(statusCode);
		String body = "<HTML>\r\n"
				+ "<HEAD><TITLE>" + description + "</TITLE>\r\n"
				+ "</HEAD>\r\n"
				+ "<BODY>"
				+ "<H1>HTTP Error " + statusCode + ": " + description + "</H1>\r\n"
				+ "</BODY></HTML>\r\n";
		return body;
	}

}
