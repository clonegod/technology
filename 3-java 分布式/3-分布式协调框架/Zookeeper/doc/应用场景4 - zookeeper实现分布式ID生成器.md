## 分布式场景下生成全局唯一ID

在分布式系统中，为了保证数据的一致性，往往需要进行同步控制，比如减库存、唯一流水号生成等。Curator对Zookeeper进行了封装，实现了分布式锁的功能，提供了线程的同步控制。同时，Curator也提供了多种锁机制。


#####利用Curator的分布式锁来实现在同一时刻只会生成一个唯一的流水号。

	import org.apache.curator.RetryPolicy;
	import org.apache.curator.framework.CuratorFramework;
	import org.apache.curator.framework.CuratorFrameworkFactory;
	import org.apache.curator.framework.recipes.locks.InterProcessMutex;
	import org.apache.curator.retry.ExponentialBackoffRetry;
	
	import java.text.SimpleDateFormat;
	import java.util.Date;
	import java.util.concurrent.CountDownLatch;
	
	
	public class CreateOrderNoWithZK {

	    private static final String path = "/lock_path";
	
	    public static void main(String[] args) {
	
	        CuratorFramework client = getClient();
	        final InterProcessMutex lock = new InterProcessMutex(client, path);
	        final CountDownLatch countDownLatch = new CountDownLatch(1);
	
	        final long startTime = new Date().getTime();
	        for (int i = 0; i < 10; i++) {
	            new Thread(new Runnable() {
	                @Override
	                public void run() {
	                    try {
	                        countDownLatch.await();
	                        lock.acquire();
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                    }
	
	                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss|SSS");
	                    System.out.println(sdf.format(new Date()));
	
	                    try {
	                        lock.release();
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                    }
	                    System.out.println("显示此线程大概花费时间（等待+执行）:" + (new Date().getTime() - startTime) + "ms");
	                }
	            }).start();
	        }
	        System.out.println("创建线程花费时间:" + (new Date().getTime() - startTime) + "ms");
	        countDownLatch.countDown();
	    }
	
	    private static CuratorFramework getClient() {
	        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
	        CuratorFramework client = CuratorFrameworkFactory.builder()
	                .connectString("127.0.0.1:2181")
	                .retryPolicy(retryPolicy)
	                .sessionTimeoutMs(6000)
	                .connectionTimeoutMs(3000)
	                .namespace("demo")
	                .build();
	        client.start();
	        return client;
	    }
	}

###注意 - 该方案仍然不完美
在上面的代码中，打印了每步操作的时间，其中访问的zookeeper服务器是远程服务器。从打印的时间我们可以看出，通过这种方式生成唯一流水号并不能支撑很大的并发量。每次操作都需要通过网络访问，zookeeper的节点操作等，会花费大量的时间。另外，由于精确到毫秒，因此一秒钟最多也只能处理999个请求。

同时，在分布式环境中上面的示例还是会出现重复的可能性的，比如两个服务器的时间不一致，即两个服务器相差10ms，恰好第一个执行完，第二个执行的间隙也是10ms，那么第二个生成的订单号还是有可能跟第一个重复的，虽然这种概率及其小。

以上通过示例演示了Curator的分布式锁功能，根据具体的业务需求可选择不同的业务场景来使用。