package edu.uchicago.cs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Server {
    private static final int NUM_THREADS = 10;
    public static final String ROOT_DIR = "edu/uchicago/cs/www";
    public static final String INDEX_FILE_NAME = "index.html";
    public static final String REDIRECT_FILE_NAME = "redirect.defs";

    private static final Logger logger = Logger.getLogger(Server.class.getCanonicalName());
    private int[] port;

    public Server(int[] port) throws FileNotFoundException {
        this.port = port;
    }

    public void start() throws IOException {
        ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);
        Runnable regularListener = new RegularListener(pool, port[0]);
        Runnable secureListener = new SecureListener(pool, port[1]);
        pool.submit(regularListener);
        pool.submit(secureListener);
    }

    public static void main(String[] args) throws IOException {
        // get port
        int[] ports = ArgsParser.parseTwoPorts(args, "Server");

        // start server
        Server server = new Server(ports);
        server.start();
    }
}
