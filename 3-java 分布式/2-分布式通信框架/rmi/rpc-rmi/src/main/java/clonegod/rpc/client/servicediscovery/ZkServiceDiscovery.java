package clonegod.rpc.client.servicediscovery;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.common.IOUtils;

import clonegod.rpc.client.laodbalance.LoadBalance;
import clonegod.rpc.client.laodbalance.RandomLoadBalance;
import clonegod.rpc.server.serviceregistry.ZkConfig;

public class ZkServiceDiscovery implements IServiceDiscovery {

	private String zkAddress;
	
	private CuratorFramework curatorFramework;
	
	private List<String> serviceAddressList = new ArrayList<>();
	
	private LoadBalance loadBalance = new RandomLoadBalance();
	
	public ZkServiceDiscovery(String zkAddress) {
		this.zkAddress = zkAddress;
		curatorFramework = CuratorFrameworkFactory.builder()
							.connectString(this.zkAddress)
							.connectionTimeoutMs(4000)
							.retryPolicy(new ExponentialBackoffRetry(1000, 10))
							.build();
		curatorFramework.start();
	}
	
	@Override
	public String discovery(String serviceName) {
		String path = ZkConfig.SERVICE_REGISTRY_PATH + "/" +serviceName;
		
		// 获取服务地址列表
		try {
			serviceAddressList = curatorFramework.getChildren().forPath(path);
		} catch (Exception e) {
			throw new RuntimeException("从注册中心获取服务地址失败：", e);
		} 
		
		// 监听服务地址的变更
		registerWatcher(path);
		
		// 负载均衡
		return loadBalance.select(serviceAddressList);
	}
	
	private void registerWatcher(String path) {
		PathChildrenCache childrenCache = new PathChildrenCache(curatorFramework, path, true);
		
		PathChildrenCacheListener pathChildrenCacheListener = new PathChildrenCacheListener() {
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				System.err.println(Thread.currentThread().getName() +" 更新服务地址......" + event.getType().name());
				serviceAddressList = curatorFramework.getChildren().forPath(path);
			}
		};
		
		childrenCache.getListenable().addListener(pathChildrenCacheListener);
		
		try {
			childrenCache.start();
		} catch (Exception e) {
			IOUtils.closeStream(childrenCache);
			throw new RuntimeException("注册服务Watcher异常：", e);
		}
	}

}
