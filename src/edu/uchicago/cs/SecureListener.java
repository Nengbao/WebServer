package edu.uchicago.cs;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.security.*;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;

public class SecureListener extends AbstractListener {
	public final static String algorithm = "SSL";

	public SecureListener(ExecutorService pool, int port) {
		super(pool, port);
	}

	@Override
	protected ServerSocket getServerSocket() throws Exception {
		SSLContext context = SSLContext.getInstance(algorithm);
		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
		KeyStore ks = KeyStore.getInstance("JKS");
		char[] password = "networks".toCharArray();
		ks.load(new FileInputStream("edu/uchicago/cs/server.jks"), password);
		kmf.init(ks, password);
		context.init(kmf.getKeyManagers(), null, null);
		Arrays.fill(password, '0');
		SSLServerSocketFactory factory = context.getServerSocketFactory();
		SSLServerSocket sslServerSocket = (SSLServerSocket) factory.createServerSocket(port);
		sslServerSocket.setWantClientAuth(false);
		sslServerSocket.setNeedClientAuth(false);    //@TODO verify, no client auth

//		// @TODO check, add anonymous (non-authenticated) cipher suites
//		String[] supported = sslServerSocket.getSupportedCipherSuites();
//		String[] anonCipherSuitesSupported = new String[supported.length];
//		int numAnonCipherSuitesSupported = 0;
//		for (int i = 0; i < supported.length; i++) {
//			if (supported[i].indexOf("_anon_") > 0) {
//				anonCipherSuitesSupported[numAnonCipherSuitesSupported++] = supported[i];
//			}
//		}
//		String[] oldEnabled = sslServerSocket.getEnabledCipherSuites();
//		String[] newEnabled = new String[oldEnabled.length + numAnonCipherSuitesSupported];
//		System.arraycopy(oldEnabled, 0, newEnabled, 0, oldEnabled.length);
//		System.arraycopy(anonCipherSuitesSupported, 0, newEnabled, oldEnabled.length, numAnonCipherSuitesSupported);
//		sslServerSocket.setEnabledCipherSuites(newEnabled);

		return sslServerSocket;
	}

	@Override
	protected String getListenerType() {
		return "secure";
	}
}
