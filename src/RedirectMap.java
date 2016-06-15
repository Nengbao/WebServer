import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class RedirectMap {
	private static Map<String, String> redirectMap = null;

	// load redirect file into a hashmap
	static  {
		redirectMap = new HashMap<>();
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(Server.ROOT_DIR + "/" + Server.REDIRECT_FILE_NAME));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine().trim();
			if (line.equals("")) {  // empty line
				continue;
			}

			String[] tokens = line.split("\\s+");
			redirectMap.put(tokens[0], tokens[1]);
		}
		scanner.close();
	}

//	// single copy of redirectMap + lazy load redirectMap
//	public static Map<String, String> getRedirectMap() throws FileNotFoundException {
//		if (redirectMap == null) {
//			return buildRedirectMapFromFile();
//		} else {
//			return redirectMap;
//		}
//	}

	public static boolean isRedirect(String path){
		return redirectMap.containsKey(path);
	}

	public static String getRedirectLink(String path) {
		if (isRedirect(path)) {
			return redirectMap.get(path);
		}
		else {
			return null;
		}
	}

}
