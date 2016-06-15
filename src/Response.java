import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

public abstract class Response {
	public static final String CHAR_SET_DEFAULT = "UTF-8";

	protected OutputStream out;
	protected int statusCode;
	protected String contentType;
	protected boolean doSendBody;
	protected String protocolVersion;

	protected void send() {
		sendHeader();
		if (doSendBody) {
			sendBody();
		}
	}

	protected void sendHeader() {
		Date now = new Date();

		String head = "HTTP/" + protocolVersion + " " + statusCode + " " + StatusCodeMap.getDescription(statusCode) + "\r\n"
				+ "Date: " + now + "\r\n"
				+ extraHeader()
				+ "Content-length: " + getContentLength() + "\r\n"
				+ "Content-type: " + contentType + "\r\n\r\n";
		try {
			out.write(head.getBytes(CHAR_SET_DEFAULT));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected abstract String extraHeader();
	protected abstract long getContentLength();
	protected abstract void sendBody();

	protected void setUp(Request request, Socket clientSocket) {
		protocolVersion = request.getProtocolVersion();
		try {
			out = new BufferedOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}