package clonegod.rpc.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import clonegod.rpc.server.anno.RPCAnnotation;
import clonegod.rpc.server.serviceregistry.IRegistryCenter;

public class RPCServer {
	
	private static final ExecutorService threadPool = Executors.newCachedThreadPool();
	

	private IRegistryCenter registryCenter; // 服务注册中心
	
	private String serviceAddress; // 服务地址
	
	private Map<String, Object> serviceMapping = new HashMap<>();
	
	public RPCServer(IRegistryCenter registryCenter, String serviceAddress) {
		super();
		this.registryCenter = registryCenter;
		this.serviceAddress = serviceAddress;
	}


	/**
	 * 本地内存中保存服务名称和服务实例的映射关系
	 */
	public void bind(Object ... services) {
		for(Object service : services) {
			RPCAnnotation anno = service.getClass().getAnnotation(RPCAnnotation.class);
			String serviceName = anno.value().getName();
			String version = anno.version();
			if(version !=null && ! "".equals(version)) {
				serviceName = serviceName + "-" + version;
			}
			serviceMapping.put(serviceName, service);
		}
	}
	

	/**
	 * 发布服务
	 */
	public void publish() {
		ServerSocket serverSocket = null;
		try {
			int port = Integer.parseInt(this.serviceAddress.split(":")[1]);
			serverSocket = new ServerSocket(port);
			
			serviceMapping.forEach((serviceName, instance) -> {
				System.out.println("开始注册服务："+serviceName+"---"+serviceAddress);
				registryCenter.registry(serviceName, serviceAddress);
			});
			
			System.out.println("服务发布成功，等待客户端请求");
			while(true) {
				Socket socket = serverSocket.accept();
				threadPool.execute(new RequestHandler(socket, serviceMapping));
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
