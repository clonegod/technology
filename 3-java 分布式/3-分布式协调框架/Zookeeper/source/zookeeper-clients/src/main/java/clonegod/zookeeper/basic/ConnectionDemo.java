package clonegod.zookeeper.basic;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ConnectionDemo {

    public static void main(String[] args) {
        try {
            final CountDownLatch countDownLatch=new CountDownLatch(1);
            ZooKeeper zooKeeper=
                    new ZooKeeper("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183",
                            4000, new Watcher() {
                        @Override
                        public void process(WatchedEvent event) {
                            if(Event.KeeperState.SyncConnected==event.getState()){
                                //如果收到了服务端的响应事件，连接成功
                                countDownLatch.countDown();
                            }
                        }
                    });
            countDownLatch.await();
            System.out.println(zooKeeper.getState());//CONNECTED

            //添加节点
            if(zooKeeper.exists("/zk-persis-mic", false) == null) {
            	zooKeeper.create("/zk-persis-mic","0".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            	Thread.sleep(3000);
            }
            Stat stat=new Stat();

            //得到当前节点的值
            byte[] bytes=zooKeeper.getData("/zk-persis-mic",null,stat);
            System.out.println(new String(bytes));

            //修改节点值
            zooKeeper.setData("/zk-persis-mic","1".getBytes(),stat.getVersion());

            //得到当前节点的值
            byte[] bytes1=zooKeeper.getData("/zk-persis-mic",null,stat);
            System.out.println(new String(bytes1));

            zooKeeper.delete("/zk-persis-mic",stat.getVersion());

            zooKeeper.close();

            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }
}
