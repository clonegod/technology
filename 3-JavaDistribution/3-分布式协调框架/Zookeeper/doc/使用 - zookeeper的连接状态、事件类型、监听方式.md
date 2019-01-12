# zookeeper的连接状态、事件类型、监听方式

客户端通过不同的事件类型和状态类型来判断znode上发生了哪种变化。

##连接状态（客户端与zk集群连接相关）
	KeeperStat.SyncConnected  	建立连接成功
	KeeperStat.Expired  		会话超时
	KeeperStat.Disconnected  	断开连接
	KeeperStat.AuthFailed  		客户端认证失败

##事件类型（单个znode节点上可能发生的事件类型）
	None   				客户端与zk集群连接成功事件
	NodeCreated  		节点创建
	NodeDeleted        	节点删除
	NodeDataChanged    	节点数据变更：数据内容发生修改
	NodeChildrenChanged  子节点发生变更：新增、删除。注意：子节点内容发生变化不会触发该事件。

## 事件监听的注册方式-重点
zookeeper有watch事件，是1次性触发的。当watch监视的znode发生变化时，zookeeper就会通知设置了该watch的client。


watcher的特性

	通知是一次性，一旦触发一次通知后，该watcher就失效！


#####怎样注册监听？ - 3种方式，用法有区别

###### 支持watch的API
	zk.exists(path, watch)			判断path是否存在，并监控path对应的znode
	zk.getData(path, watch, stat)	获取path对应znode的value，并监控path对应的znode
	zk.getChildren(path, watch)		获取path对应znode的子节点，并监控path下的子节点变化

客户端调用相关api查询znode信息，zookeeper返回结果给客户端。

如果客户端需要在节点发生任何变化时都得到通知，那就可以设置watch参数为true，

这样，当对应的节点发生变化时，客户端将立即得到通知，然后在处理通知的方法中进行细节处理。

注意：**watch是1次性的**。如果需要一直监控节点，则每次调用api都要设置watch=true.


有三个api可以对节点状态设置监听，但各自用途不同：

#####第1种：可以监听节点的创建、删除、数据变化。

	// exists可以对不存在的节点注册监听，而getData则不行。
	zk.exists(PARENT_PATH, true); 

#####第2种：可以监听节点的删除、数据变化

	// 只能对已存在的节点设置监听。
	zk.getData(PARENT_PATH, true, null); 

#####第3种：可以监听子节点的变更

	// 必须使用这种方式对子节点进行监听才有效
	//只能通知子节点发生了变更，但具体是新增还是删除则是不确定的！
	zk.getChildren(PARENT_PATH, true); 


#####监听实例（重要）
比如，对根节点/p监听，进行操作时所触发的事件，监听path=/p：

#####1、创建根节点：
	监听方式：zk.exists(PARENT_PATH, true);
	动作： zk.create(PARENT_PATH, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	触发事件：type:NodeCreated path:/p


#####2、修改根节点：
	监听方式：zk.getData(PARENT_PATH, true, null);
	动作：zk.setData(PARENT_PATH, "parent".getBytes(), -1);
	触发事件：type:NodeDataChanged


#####3、新增子节点：
	监听方式：zk.getChildren(PARENT_PATH, true);
	动作：zk.create(CHILDREN_PATH, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
	触发事件：type:NodeChildrenChanged


#####4、修改子节点：
	监听方式：zk.getChildren(PARENT_PATH, true);
	动作：zk.setData(CHILDREN_PATH, "child".getBytes(), -1);
	触发事件：无-修改子节点内容不会触发根节点的监听事件

#####5、删除子节点：
	监听方式： zk.getChildren(PARENT_PATH, true);
	动作：zk.delete(CHILDREN_PATH, -1);
	触发事件：type:NodeChildrenChanged 
