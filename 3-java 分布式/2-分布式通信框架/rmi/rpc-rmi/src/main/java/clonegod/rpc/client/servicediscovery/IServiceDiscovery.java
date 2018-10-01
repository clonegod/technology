package clonegod.rpc.client.servicediscovery;

public interface IServiceDiscovery {
	
	/**
	 * 根据请求的服务名称从注册中心获取服务的地址
	 * @param serviceName
	 * @return
	 */
	String discovery(String serviceName);
	
}
