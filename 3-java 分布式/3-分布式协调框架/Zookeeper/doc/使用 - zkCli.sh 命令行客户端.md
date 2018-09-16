##数据模型
ZNODE 节点树，包含两部分内容：

	path 节点路径/名称空间
	data 节点的数据(仅支持存储少量数据，不超过1M)

##节点类型
	持久节点 	- 可用来存储配置数据，一般存储的数据量很小，比如开关变量
	持久有序节点 	- 可用来做有序消息队列
	临时节点 	- 可用来服务上下线的动态感知，特点是客户端session失效后，节点会自动被删除
	临时有序节点 	- 可用来实现分布式锁


##stat信息的解释
	cZxid = 0x500000015							节点被创建时的事务ID
	ctime = Sat Aug 05 20:48:26 CST 2017		节点的创建时间
	mZxid = 0x500000016							节点最后一次被更新的事务ID
	mtime = Sat Aug 05 20:48:50 CST 2017		节点最后一次被修改的时间
	pZxid = 0x500000015						当前节点下的子节点最后一次被修改时的事务ID
	cversion = 0								子节点的版本号
	dataVersion = 1								表示的是当前节点数据的版本号
	aclVersion = 0								表示acl的版本号，修改节点权限
	ephemeralOwner = 0x0   						存放临时节点的sessionid
	dataLength = 3    							数据值的长度
	numChildren = 0  							子节点的个数



##基本增删改查操作
#####1. create [-s] [-e] path data acl
	-s 表示节点是否有序
	-e 表示是否为临时节点。默认情况下，是持久化节点。
	注意：临时节点下面不能创建子节点。

#####2. get path [watch]
获得指定 path的信息，watch=true表示监控这个节点的状态变化。
 
#####3.set path data [version]
修改节点 path对应的data

>version选项的作用---提供乐观锁的支持：
多个客户端同时修改某个path的值，可通过version来实现乐观锁，防止更新丢失。
比如，数据库里面有一个 version 字段去控制数据行的版本号。

注意：version不代表zookeeper会存储多个版本的数据，不能通过修改version版本回退到历史数据，version仅仅用来描述节点数据的最新版本号。

#####4.delete path [version]
删除节点

递归删除：rmr path


---
# zkCli的使用

进入zookeeper命令行客户端

	/usr/local/zookeeper/bin/zkCli.sh

操作命令

	stat path [watch]				查看path的状态，可监控
    set path data [version]			设置数据到path
    ls path [watch]					列出子目录，可监控
    delquota [-n|-b] path
    ls2 path [watch]				查看状态并列出子目录，可监控
    setAcl path acl
    setquota -n|-b val path
    history 						列出历史命令
    redo cmdno						重新执行某个历史命令，指定命令编号执行
    printwatches on|off
    delete path [version] 			删除子目录
    sync path
    listquota path
    rmr path 						递归删除
    get path [watch]				获取存储在path上的数据
    create [-s] [-e] path data acl  创建目录
    addauth scheme auth
    quit 							退出客户端
    getAcl path
    close 
    connect host:port

查看zookeeper上存储了哪些数据

	[zk: localhost:2181(CONNECTED) 16] ls /
	[zookeeper]

创建节点 /test

	 [zk: localhost:2181(CONNECTED) 19] create /test "my test"
	 Created /test

查询节点数据 /test

	[zk: localhost:2181(CONNECTED) 20] get /test
	my test
	cZxid = 0x300000008
	ctime = Mon Jan 01 12:41:21 CST 2018
	mZxid = 0x300000008
	mtime = Mon Jan 01 12:41:21 CST 2018
	pZxid = 0x300000008
	cversion = 0
	dataVersion = 0
	aclVersion = 0
	ephemeralOwner = 0x0
	dataLength = 7
	numChildren = 0

创建子节点 /test/app1

	[zk: localhost:2181(CONNECTED) 21] create /test/app1 "this is app1"   
	Created /test/app1

获取子目录的数据 /test/app1
	
	[zk: localhost:2181(CONNECTED) 22] get /test/app1
	this is app1
	cZxid = 0x300000009
	ctime = Mon Jan 01 12:42:56 CST 2018
	mZxid = 0x300000009
	mtime = Mon Jan 01 12:42:56 CST 2018
	pZxid = 0x300000009
	cversion = 0
	dataVersion = 0
	aclVersion = 0
	ephemeralOwner = 0x0
	dataLength = 12
	numChildren = 0


递归删除整个目录 /test/app1, /test

	[zk: localhost:2181(CONNECTED) 25] rmr /test
	[zk: localhost:2181(CONNECTED) 26] ls /
	[zookeeper]	

退出客户端 quit

	[zk: localhost:2181(CONNECTED) 37] quit
	Quitting...