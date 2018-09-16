# standalone mode 单节点模式
部署在一台服务器上，单实例。

##### 配置zoo.cfg
复制zoo_sample.cfg文件，重命名为zoo.cfg

如果没有特殊需求，不需要修改配置文件，直接使用默认配置文件即可.

##### 启动zookeeper
	zookeeper-3.4.12\bin> zkServer.cmd

##### 查看是否运行成功：
	[main:NIOServerCnxnFactory@89] - binding to port 0.0.0.0/0.0.0.0:2181


---

# 单IP多节点（伪集群）
>部署在同一IP，但是有多个节点，各有自己的端口。
>
>zk的部署个数最好为基数，ZK集群的机制是只要超过半数的节点OK，集群就能正常提供服务。
>
>只有ZK节点挂得太多，只剩一半或不到一半节点能工作，集群才失效。

###注意：
同一IP上搭建多个节点的集群时，必须要注意端口问题，端口必须不同；

创建多个节点集群时，在dataDir目录下必须创建myid文件，myid文件用于zookeeper验证server序号等，myid文件只有一行，并且为当前server的序号，例如server.1的myid就是1，server2的myid就是2。。。


###搭建步骤
拷贝多份zookeeper程序，例如设置三个server，分别创建目录server1、server2、server3，

每个目录下存放一份zookeeper程序，并修改各自配置文件如下：

#####server1
	#zoo.cfg
	tickTime=2000
	initLimit=10
	syncLimit=5
	
	dataDir=E:/local/zookeeper/zk-cluster/server1/data
	
	clientPort=2181
	
	server.1=127.0.0.1:2881:3881
	server.2=127.0.0.1:2882:3882
	server.3=127.0.0.1:2883:3883

在dataDir目录下(zk-cluster/server1/data)，新建myid文件，内容为：1

#####server2
	#zoo.cfg
	tickTime=2000
	initLimit=10
	syncLimit=5
	
	dataDir=E:/local/zookeeper/zk-cluster/server2/data
	
	clientPort=2182
	
	server.1=127.0.0.1:2881:3881
	server.2=127.0.0.1:2882:3882
	server.3=127.0.0.1:2883:3883

在dataDir目录下(zk-cluster/server2/data)，新建myid文件，内容为：2

#####server3
	#zoo.cfg
	tickTime=2000
	initLimit=10
	syncLimit=5
	
	dataDir=E:/local/zookeeper/zk-cluster/server3/data
	
	clientPort=2183
	
	server.1=127.0.0.1:2881:3881
	server.2=127.0.0.1:2882:3882
	server.3=127.0.0.1:2883:3883

在dataDir目录下(zk-cluster/server3/data)，新建myid文件，内容为：3


### 启动集群
启动过程：启动顺序为server1、server2、server3。

在启动server1，server2时zk会报错，当所有节点全部启动时错误会消失。

	zk-cluster/server1/zookeeper-3.4.12/bin/zkServer.cmd
	zk-cluster/server2/zookeeper-3.4.12/bin/zkServer.cmd
	zk-cluster/server3/zookeeper-3.4.12/bin/zkServer.cmd



