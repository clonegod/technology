# zoo.cfg 配置文件参数含义说明

### tickTime
心跳时间间隔，以毫秒为单位。

这个时间是作为 Zookeeper 服务器之间或客户端与服务器之间维持心跳的时间间隔，也就是每隔tickTime 时间就会发送一个心跳。

initLimit 和 syncLimit 以此为时间单位，用来配置最大超时时间。

### dataDir
zk保存数据的目录。

存储内存中数据库快照的位置，顾名思义就是 Zookeeper 保存数据的目录。
默认情况下，Zookeeper 将写数据的日志文件也保存在这个目录里。
建议单独设置dataLogDir配置来存储日志数据。

### dataLogdir
zk事务日志的保存目录。

zk日志存储路径。如果没指定，则默认保存到dataDir目录中，与数据库快照放在一起。

### clientPort
zk对客户端开发的服务端口号。默认2181

这个端口就是客户端连接 Zookeeper 服务器的端口，Zookeeper 会监听这个端口，接受客户端的访问请求。

### maxClientCnxns
允许的客户端的最大连接数，默认60

### initLimit  集群初始化的超时时间
整个zk集群达成一致状态的上限时间（leader选举完成）。

这个配置项是用来配置 Zookeeper 接受客户端（这里所说的客户端不是用户连接 Zookeeper 服务器的客户端，而是 Zookeeper 服务器集群中连接到 Leader 的 Follower 服务器）初始化连接时（长连接）最长能忍受多少个心跳时间间隔数。当已经超过 10 个心跳的时间（也就是 tickTime）长度后 Zookeeper 服务器还没有收到客户端的返回信息，那么表明这个客户端连接失败。总的时间长度就是 10*2000=20 秒

### syncLimit  数据同步的超时时间
leader发送消息给follower，且follower回复leader的一个消息同步上限时间。

这个配置项标识 Leader 与 Follower 之间发送消息，请求和应答时间长度，最长不能超过多少个 tickTime 的时间长度，总的时间长度就是 5*2000=10 秒

### server.A = B:C:D
	A 表示服务器的id（必须与myid中的序号一致）；
	B 是这个服务器的 ip 地址或主机名；
	C 表示的是这个节点与zk集群中的其它节点进行通信的端口；
	D 表示选举Leader用的端口。