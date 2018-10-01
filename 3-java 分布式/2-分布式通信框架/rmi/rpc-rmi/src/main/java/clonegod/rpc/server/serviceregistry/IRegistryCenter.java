package clonegod.rpc.server.serviceregistry;

/**
 * 注册中心 
 */
public interface IRegistryCenter {

	/**
	 * 服务端注册
	 * @param serviceName 服务名称
	 * @param address 服务地址
	 */
	void registry(String serviceName, String address);
	
}
