## 非关系型数据库的特点
1、数据模型比较简单；

2、对数据库的性能要求非常高；

3、不需要高度的数据一致性；

--------------------------------------------------------------------
## 高可用
系统需要非常高的可用性，任一节点的宕机不会造成系统服务的中断。

一般，采用集群来提供系统的高可用（主从结构 + 多节点 构成的集群）
	
## 支持水平扩展
随着系统负荷的不断增加，可随时增加新的节点来分摊负载，新增节点后可减轻其它节点的处理压力。

水平扩展：
通过增加额外的机器来减轻当前系统的压力
不是单靠提升某台服务器的处理能力来解决问题，而是采用一组节点来分摊系统的工作负载。

比如，redis集群从6个节点扩展到12个节点。

垂直扩展：
通过对单一机器性能的不断加强来提升系统性能
比如从4核扩展到8核，内存从8G增加到16G，机械硬盘换位固态硬板，百兆网卡升级为千兆网卡等

--------------------------------------------------------------------
## Redis简介
以key-value形式存储，是非关系型、分布式、开源、水平可扩展的。

#### 优点
对数据的高并发读写（单线程）
可海量数据的高效率存储与访问（基于内存存储，并提供可选的数据持久化功能）
对数据的可扩展性（水平扩展）和高可用性（集群）

#### 缺点
redis相对于关系型数据库，对ACID的支持非常有限，无法做太复杂的关系数据模型
#### 主要应用场景
1、作为缓存服务器，为mysql等数据库降压，比如将非关键热点数据放入redis缓存起来，提供快速查询；
这种场景下，必须保证redis缓存的数据与mysql存放的数据是一致的
即；更新mysql需要一起更新redis，将两个操作放在一个事物中进行操作！

2、为业务系统集群提供共享缓存，比如session共享；

3、提供一些高并发场景下的特殊解决方案，比如做秒杀，排行榜等；

--------------------------------------------------------------------
### Redis如何保证数据安全？
reids是完全基于内存提供数据服务的，当系统宕机后，内存数据会完全丢失，redis怎么应对这样的情况？

1、RDB -> 定时快照持久化（数据刷盘不实时，数据丢失的概率更大）

2、AOF -> 将所有引起数据发送变化的命令都记录到日志文件中保存（速度快，推荐生产上使用该方案）
	
	
--------------------------------------------------------------------
### Redis高可用的方案：
##### 1、一主多从（主节点负责写，从节点负责读），---> 较少使用，存在单点问题

缺点1：一旦主节点宕机，系统将完全不可用。

缺点2：主节点、从节点存储的都是相同的数据，无法提供分布式存储的功能。
	
##### 2、哨兵  + 一主多从。 ---> redis2.x中常用的方案
监控主节点的状态，一旦主节点不可用，将在N个从节点中进行选举出一个新的主节点（将某个从节点提升为主节点）。

宕机的主节点恢复后，以新的从节点身份再加入到主从环境，

缺点1：主节点、从节点存储的都是相同的数据，无法提供分布式存储的功能。
	
##### 3、集群（redis3.0提供）。 ---> 功能比较完善，推荐使用。

高可用：多个主从结构构成一个集群，以集群方式提供系统的高可用。

数据分布式存储：数据均摊到不同的主节点上存储。

数据迁移：支持数据在节点间移动，比如将节点A的数据移动一部分到节点B上。

水平可扩展：可增加新节点到集群环境，提高更大的存储能力；可减少节点，释放过剩的节点。

--------------------------------------------------------------------
### redis3.x与redis2.x的比较：
从高可用方面看：
3.x以集群的方式替代了2.x中的哨兵机制来提供高可用。

从数据存储方面看：
3.x在集群环境中引入hash槽位slot来对数据进行存储，数据完全分布式存储，提供了水平扩展的能力。

### redis和memcached简单比较
	从内存方面看：
		redis：key全部在内存，数据可全部在内存，也可大部分在内存，小部分在磁盘
		memcached：所有数据（key和value）全部在内存
	从工作线程上看： 
		redis: 单线程串行执行（单线程可以不考虑线程并发引起的问题，简化任务的执行）
		memcached：多线程并行执行，可有效利用多核cpu
	从数据存储方面看：
		reids: 支持数据持久化（rdb或aof）
		memcached：不支持，数据全部在内存

### LUA脚本
	reids使用lua脚本实现命令执行的原子性

--------------------------------------------------------------------	
### redis突然变慢，可能的原因？
高并发写的时候，由于为了保障数据安全，开启了aof日志功能，而redis是单线程工作模式，所以多线程并发写导致写速度慢。

解决方案：

1、增加主节点，分摊高并发写的压力；

2、使用ssdb替代redis，ssdb支持高并发写，相关链接 http://ssdb.io/zh_cn/


		


