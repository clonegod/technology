package clonegod.rpc.server.serviceregistry;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * 使用zookeeper作为注册中心
 *
 */
public class ZKRegistryCenter implements IRegistryCenter {
	
	private CuratorFramework curatorFramework;
	
	public ZKRegistryCenter() {
		curatorFramework = CuratorFrameworkFactory.builder()
							.connectString(ZkConfig.CONNECTION_STR)
							.connectionTimeoutMs(4000)
							.retryPolicy(new ExponentialBackoffRetry(1000, 10))
							.build();
		curatorFramework.start();
	}
	
	@Override
	public void registry(String serviceName, String address) {
		try {
			// /registrys/user-service/127.0.0.1:8001
			// /registrys/user-service/127.0.0.1:8002
			// /registrys/user-service/127.0.0.1:8003
			String servicePath = ZkConfig.SERVICE_REGISTRY_PATH + "/" + serviceName;
			
			// 1、 创建/registrys/producet-service
			if(curatorFramework.checkExists().forPath(servicePath) == null) {
				curatorFramework.create().creatingParentContainersIfNeeded()
								.withMode(CreateMode.PERSISTENT)
								.forPath(servicePath);
			}
			
			// 2、注册服务地址
			String serviceAddress = servicePath + "/" + address;
			String nodeInfo = curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath(serviceAddress);
			System.out.println("服务注册成功：" + nodeInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
