zookeeper从设计模式角度来看，是一个基于观察者模式设计的分布式服务管理框架。

它负责存储和管理分布式系统都关心的数据，然后接受客户端的注册，一旦这些数据状态发送变化，

zookeeper就会通知那些注册了监控的观察者（客户端程序），从而实现类似集群中的Master-Slave管理模式。

### 集群Master选举
zookeeper不仅能够帮助维护当前集群环境中机器的服务状态，

而且能帮助选出一个“Master”，让这个Master来管理集群。

这就是zookeeper另一个重要的功能，领导者选举，并实现集群的容错功能。

使用zookeeper进行Master选举：

	基于znoe节点path的唯一性，创建节点成功的客户端为master。
	kafka、hadoop、hbase都使用zookeeper来实现master选举。
