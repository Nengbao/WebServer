import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;

public class RegularListener extends AbstractListener{

	public RegularListener(ExecutorService pool, int port) {
		super(pool, port);
	}

	@Override
	protected ServerSocket getServerSocket() throws Exception {
		ServerSocket serverSocket = new ServerSocket(port);
		return serverSocket;
	}

	@Override
	protected String getListenerType() {
		return "regular";
	}
}
