public class Request {

	private String[] headers;
	private String method;
	private String path;
	private boolean isSupportedHttp = false;
	private String protocolVersion;

	public Request(String HeaderSection) {
		headers = HeaderSection.split(System.lineSeparator());

		// parse first line
		String firstLine = headers[0];
		String[] tokens = firstLine.split("\\s+");
		if (tokens.length == 3) {   // check the first line
			method = tokens[0];
			path = tokens[1].substring(1);	// remove the first char '/'
			String protocol = tokens[2].split("/")[0];
			protocolVersion = tokens[2].split("/")[1];
			isSupportedHttp = protocol.equals("HTTP") && (protocolVersion.equals("1.0") || protocolVersion.equals("1.1"));
		}
	}

	public boolean isSupportedHttpGet(){
		return method.equals("GET") && isSupportedHttp;
	}

	public boolean isSupportedHttpHead(){
		return method.equals("HEAD") && isSupportedHttp;
	}

	public String getPath() {
		return path;
	}

	public String getProtocolVersion() {
		return protocolVersion;
	}

	public boolean isPersistentConnection(){
		boolean isPersistentConnection = false;

		for (String rawHeader : headers) {
			String cleanHeader = rawHeader.trim().toLowerCase();	// deal with case and leading/tailing white space
			if (cleanHeader.startsWith("connection")) {
				String[] tokens = cleanHeader.split(":");
				if (tokens.length == 2) {
					isPersistentConnection = !tokens[1].trim().equals("close");
				}
			}
		}

		return isPersistentConnection;
	}
}
