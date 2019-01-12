package clonegod.zookeeper.watcher;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class WatcherDemo {

	/**
	 * 如果指定Watcher，则使用指定的Watcher来处理事件
	 * 
	 * 如果没有指定Watcher，则使用ZooKeeper构造函数中的Watcher来处理事件（默认的事件处理器）
	 */
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        final CountDownLatch countDownLatch=new CountDownLatch(1);
        final ZooKeeper zooKeeper=
                new ZooKeeper("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183",
                        4000, new Watcher() {
                    @Override
                    public void process(WatchedEvent event) {
                        System.out.println("全局事件处理器： "+event.getType());
                        if(Event.KeeperState.SyncConnected==event.getState()){
                            //如果收到了服务端的响应事件，连接成功
                            countDownLatch.countDown();
                        }
                    }
                });
        countDownLatch.await();

        if(zooKeeper.exists("/zk-persis-mic", false) == null) {
        	zooKeeper.create("/zk-persis-mic","1".getBytes(),
        			ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        }


        //exists  getdata getchildren
        //通过exists绑定事件
        Stat stat=zooKeeper.exists("/zk-persis-mic", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println(event.getType()+"->"+event.getPath());
                try {
                    //再一次去绑定事件
                    zooKeeper.exists(event.getPath(),true);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        //通过修改的事务类型操作来触发监听事件
        stat=zooKeeper.setData("/zk-persis-mic","2".getBytes(),stat.getVersion());

        Thread.sleep(1000);

        zooKeeper.delete("/zk-persis-mic",stat.getVersion());

        System.in.read();
    }
}
