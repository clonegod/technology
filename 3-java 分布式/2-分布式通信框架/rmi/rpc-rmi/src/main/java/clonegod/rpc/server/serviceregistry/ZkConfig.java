package clonegod.rpc.server.serviceregistry;

public class ZkConfig {

	// zk集群连接地址
	public static final String CONNECTION_STR = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
	
	// 服务注册的NAMESPACE
	public static final String SERVICE_REGISTRY_PATH = "/registrys/user-service";
}	
