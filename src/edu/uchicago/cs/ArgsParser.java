package edu.uchicago.cs;

public class ArgsParser {
	public static int[] parseTwoPorts(String[] args, String className) {
		int numOfPorts = 2;
		int[] ports = new int[numOfPorts];

		String sslServerPortArg = "--sslServerPort";
		String serverPortArg = "--serverPort";
		String usageInfo = "Expected usage: java " + className + " " + serverPortArg + "=port1 " + sslServerPortArg + "=port2";
		String errorMsg = "Error in command line argument format";

		if (args.length == numOfPorts) {
			int indexOfEqual0 = args[0].indexOf("=");
			int indexOfEqual1 = args[1].indexOf("=");
			if ((indexOfEqual0 != -1) && (indexOfEqual1 != -1)) {
				String firstPart0 = args[0].substring(0, indexOfEqual0);
				String firstPart1 = args[1].substring(0, indexOfEqual1);
				if (!firstPart0.equals(serverPortArg) || !firstPart1.equals(sslServerPortArg)) {
					System.out.println(errorMsg);
					System.out.println(usageInfo);
					throw new IllegalArgumentException(usageInfo);
				}
				ports[0] = Integer.parseInt(args[0].substring(indexOfEqual0 + 1));
				ports[1] = Integer.parseInt(args[1].substring(indexOfEqual1 + 1));
				for (int port : ports) {
					if (port < 0 || port > 65535) {
						System.out.println("Expected port number is between 0 and 65535");
						System.out.println(usageInfo);
						throw new IllegalArgumentException(usageInfo);
					}
				}
			} else {
				System.out.println(errorMsg);
				System.out.println(usageInfo);
				throw new IllegalArgumentException(usageInfo);
			}
		} else {
			System.out.println(errorMsg);
			System.out.println(usageInfo);
			throw new IllegalArgumentException(usageInfo);
		}

		return ports;
	}
}
