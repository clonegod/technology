# 单机环境安装	


#####1.下载zookeeper的安装包
http://apache.fayea.com/zookeeper/stable/zookeeper-3.4.10.tar.gz

#####2.解压zookeeper 
tar -zxvf zookeeper-3.4.10.tar.gz

#####3.cd 到 ZK_HOME/conf  , copy一份zoo.cfg
cp  zoo_sample.cfg  zoo.cfg

#####4.sh zkServer.sh （启动zookeeper服务）
{start|start-foreground|stop|restart|status|upgrade|print-cmd}

#####5.sh zkCli.sh	（进入zookeeper命令行客户端）



---


# 集群环境搭建
zookeeper的集群组成
zookeeper一般是由 2n+1台服务器组成。

zookeeper集群搭建步骤
####第一步： 修改配置文件 zoo.cfg
	server.id=host:port:port
	id的取值范围： 1~255； 用id来标识该机器在集群中的机器序号。
	2888表示follower节点与leader节点交换信息的端口号
	3888表示如果leader节点挂掉了, 需要一个端口来重新选举。
	
	server.1=192.168.11.129:2888:3888
	server.2=192.168.11.131:2888:3888
	server.3=192.168.11.135:2888:3888

####第二步：创建myid
在每一个服务器的dataDir目录下创建一个myid的文件，文件就一行数据，数据内容是每台机器对应的server ID的数字

####第三步：启动zookeeper
bin/zkServers.sh start			     #默认读取配置文件conf/zoo.cfg
bin/zkServers.sh start conf/myzoo.cfg  #指定配置文件路径


######另外，还可以配置额外的节点作为observer
1、在zoo.cfg里面增加

	peerType=observer

2、在配置为observer的节点信息后面增加后缀 ":observer"

	server.1=192.168.11.129:2888:3888:observer
