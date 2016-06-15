import java.util.HashMap;
import java.util.Map;

public class StatusCodeMap {
	private static Map<Integer, String> statusCodeMap;

	static {
		statusCodeMap = new HashMap<>();
		statusCodeMap.put(200, "OK");
		statusCodeMap.put(301, "Move Permanently");
		statusCodeMap.put(404, "File Not Found");
		statusCodeMap.put(403, "Forbidden");
	}

	public static String getDescription(int statusCode){
		return statusCodeMap.get(statusCode);
	}
}
