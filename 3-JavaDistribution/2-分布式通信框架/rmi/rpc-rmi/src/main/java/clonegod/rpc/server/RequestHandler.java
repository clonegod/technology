package clonegod.rpc.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

import clonegod.rpc.api.RPCRequest;

public class RequestHandler implements Runnable {

	private Map<String, Object> serviceMapping;
	private Socket socket;
	
	public RequestHandler(Socket socket, Map<String, Object> serviceMapping) {
		this.socket = socket;
		this.serviceMapping = serviceMapping;
	}

	@Override
	public void run() {
		try {
			// 接收客户端请求
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			RPCRequest request = (RPCRequest) ois.readObject();
			System.out.println("接收到新的请求：" + request.toString());
			
			// 反射调用本地service
			Object result = invoke(request);
			
			// 响应结果
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(result);
			oos.flush();
			
			ois.close();
			oos.close();
		} catch(Exception e) {
			throw new RuntimeException("处理客户端请求发生异常", e);
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * 反射调用本地方法
	 * 
	 */
	private Object invoke(RPCRequest request) throws Exception {
		String serviceName = request.getClassName();
		
		String version = request.getVersion();
		if(version !=null && ! "".equals(version)) {
			serviceName = serviceName + "-" + version;
		}
		
		// 根据请求的服务名称获取对应的服务实例对象
		Object service = serviceMapping.get(serviceName);
		
		if(service == null) {
			throw new RuntimeException("服务名称错误：" + serviceName);
		}
		
		String methodName = request.getMethod();
		Object[] args = request.getParams();
		Class<?>[] parameterTypes = new Class[args.length];
		for(int i = 0; i < args.length; i++) {
			parameterTypes[i] = args[i].getClass();
		}
		
		// 通过反射调用本地服务的方法
		Method method = service.getClass().getDeclaredMethod(methodName, parameterTypes);
		Object result = method.invoke(service, args);
		return result;
	}

}
