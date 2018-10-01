package clonegod.rpc.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RPCServer {
	
	private final int port;
	
	public RPCServer(int port) {
		this.port = port;
	}
	
	private static final ExecutorService threadPool = Executors.newCachedThreadPool();

	public void publish(final Object service) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("服务发布成功，等待客户端请求");
			while(true) {
				Socket socket = serverSocket.accept();
				threadPool.execute(new RequestHandler(service, socket));
			}
		} catch (IOException e) {
			throw new RuntimeException("服务发布异常", e);
		} finally {
			if(serverSocket != null)
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
	}
	
}
