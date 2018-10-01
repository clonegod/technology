package clonegod.rpc.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import clonegod.rpc.api.RPCRequest;

/**
 * 负责底层socket通信
 *
 */
public class TCPTransport {
	private String host;
	private int port;
	
	private Socket socket;
	
	public TCPTransport(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public Socket connet() {
		try {
			System.out.println("建立1个新的socket连接");
			socket = new Socket(host, port);
		} catch (IOException e) {
			throw new RuntimeException("socket连接建立失败");
		} 
		return socket;
	}

	/**
	 * 通过socket将请求对象发送到服务端
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Object send(RPCRequest request) {
		Object result = null;
		try {
			Socket socket = connet();
			
			// 发送
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(request);
			oos.flush();
			
			// 接收
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			result = ois.readObject();
			
			oos.close();
			ois.close();
		} catch (Exception e) {
			throw new RuntimeException("远程调用发生异常", e);
		} finally {
			close();
		}
		return result;
	}
	
	public void close() {
		if(socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
