## zookeeper集群环境搭建
>3或5节点 leader选举: leader + follower

![Image of zkservice](img/zkservice.jpg)

#

## 集群规划
	操作系统：Centos7
	Java版本：JDK_1.8.0_144
	zookeeper版本：3.4.10
		3个节点：
		node1 192.168.1.201
		node2 192.168.1.202
		node3 192.168.1.203

## 准备工作（3个节点）
	- 关闭防火墙
		systemctl disable firewalld
	- 安装JDK
		/usr/local/java
	- 配置HOSTNAME
		vi /etc/hosts
			192.168.1.201           node1.clonegod.com      node1
			192.168.1.202           node2.clonegod.com      node2
			192.168.1.203           node3.clonegod.com      node3
		vi /etc/sysconfig/network
			HOSTNAME=node1.clonegod.com
	- 创建用户（可选）
		groupdadd hadoop
		useradd -g hadoop hadoop
		passwd hadoop
	- ssh免密（可选）
		ssh-keygen -t rsa -f ~/.ssh/id_rsa
		ssh-copy-id -i ~/.ssh/id_rsa.pub hadoop@192.168.1.201
		ssh-copy-id -i ~/.ssh/id_rsa.pub hadoop@192.168.1.202
		ssh-copy-id -i ~/.ssh/id_rsa.pub hadoop@192.168.1.203

 
	
## 安装zookeeper-3.4.10
### 下载、解压、创建软连接
	cd /usr/local/software
	wget https://mirrors.tuna.tsinghua.edu.cn/apache/zookeeper/stable/zookeeper-3.4.10.tar.gz
	tar -xzvf zookeeper-3.4.10.tar.gz -C /usr/local
	cd /usr/local
	ln -s zookeeper-3.4.10 zookeeper

### 配置环境变量
	vi /etc/profile.d/zookeeper.sh 
		export ZOOKEEPER_HOME=/usr/local/zookeeper
		export PATH=$ZOOKEEPER_HOME/bin:$PATH
	source /etc/profile
	which zkServer.sh

### 配置zookeeper（node1-192.168.1.201）
首先，在192.168.1.201节点完成配置，然后再将zookeeper复制到其它节点。
##### 1、创建数据存储目录、日志目录
	mkdir -p /usr/local/zookeeper/zkData 
	mkdir -p /usr/local/zookeeper/zkData/logs	

##### 2、配置zoo.cfg
	cd /usr/local/zookeeper/conf
	cp zoo_sample.cfg zoo.cfg
	vi zoo.cfg
		# The number of milliseconds of each tick
		tickTime=2000
		
		# The number of ticks that the initial synchronization phase can take
		initLimit=10
		
		# The number of ticks that can pass between
		# sending a request and getting an acknowledgement
		syncLimit=5
		
		# the directory where the snapshot is stored.
		# do not use /tmp for storage, /tmp here is just example sakes.
		dataDir=/usr/local/zookeeper/zkData
		dataLogDir=/usr/local/zookeeper/zkData/logs
		
		# the port at which the clients will connect
		clientPort=2181
		
		# the maximum number of client connections.
		# increase this if you need to handle more clients
		#maxClientCnxns=60
				
		server.1=node1:2888:3888
		server.2=node2:2888:3888
		server.3=node3:2888:3888

##### 3、配置zk实例在集群中的唯一身份标识
	# 在zkData目录下，创建myid。该文件必须放到数据库快照目录下，也就是zkData目录下。
	cd /usr/local/zookeeper
	echo 1 > zkData/myid

### 分发zookeeper到其它节点
	cd  /usr/local # 先切换到父目录，再使用rsync命令
	rsync -avzR zookeeper-3.4.10/ node2:/usr/local
	rsync -avzR zookeeper-3.4.10/ node3:/usr/local

### 配置zk集群的其它节点（node2 - 192.168.1.202）
	ln -s /usr/local/zookeeper-3.4.10 /usr/local/zookeeper
	cd /usr/local/zookeeper
	echo 2 > zkData/myid 
	

### 配置zk集群的其它节点（node3 - 192.168.1.203）	
	ln -s /usr/local/zookeeper-3.4.10 /usr/local/zookeeper
	cd /usr/local/zookeeper
	echo 3 > zkData/myid

## >>>启动zk集群
	# 分别在3个节点上启动zookeeper
	/usr/local/zookeeper/bin/zkServer.sh start

/usr/local/zookeeper/bin/zkServer.sh 
	start
	start-foreground
	stop
	restart
	status

{start|start-foreground|stop|restart|status|upgrade|print-cmd}

## >>>检查zk集群状态
	# 分别在3个节点上查看zookeeper的状态
	/usr/local/zookeeper/bin/zkServer.sh status
	# 192.168.1.201
		Mode: follower
	# 192.168.1.202
		Mode: follower
	# 192.168.1.203
		Mode: leader

## >>>使用zkCli.sh连接zookeeper
	# 在zk集群的任意节点上登陆都可以
	/usr/local/zookeeper/bin/zkCli.sh
	# 输出
		[zk: localhost:2181(CONNECTED) 0] ls /
		[zookeeper]



