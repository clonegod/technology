##[visual-guide-to-nosql-systems](http://blog.nahurst.com/visual-guide-to-nosql-systems)

##[Brewer's CAP Theorem](http://www.julianbrowne.com/article/brewers-cap-theorem)

##[如何正确理解CAP理论？](https://www.jdon.com/bigdata/how-to-understand-cap.html)

---
CAP理论是针对NOSQL分布式数据存储技术中，所涉及到的：

	集群中节点间数据副本一致性、
	集群的高可用性、
	集群遇到网络分区时的分区容错性（网络分区后仍然可以对外提供服务）

问题而言的！！！

# 先比较一下传统数据库与NoSQL数据库的区别
其实就是侧重点不同。
	
	关系性数据库要解决数据间的关联关系，并通过事务的ACID特性提供复杂的各种数据操作。

	NoSQL数据库则以放弃事务为代价，以实现更高的性能，以及更好的水平扩展性。
	如果NoSQL要提供事务功能，则很难实现高性能，也很难提供水平扩展能力。
	注意：一些NOSQL技术也支持事务，但仅仅支持很简单的事务。

##### 关系型数据库 - 侧重事务功能 ACID
传统的关系型数据库在功能支持上很完善，从简单的键值查询，到复杂的多表联合查询，再到事务机制的支持。
传统的SQL数据库的事务通常都是支持ACID的强事务机制。

	A 原子性，即在事务中执行多个操作是原子的，要么事务中的操作全部执行，要么一个都不执行;
	C 一致性，即事务必须始终保持系统处于一致的状态，不管在任何给定的时间并发事务有多少。（比如两个账户相互转账，不管怎么转，转多少次，最终两个账户的总金额应该是不变的）。
	I 隔离性，即两个事务不会相互影响，独立执行;
	D 持久化，即事务一但完成，该事务对数据库所作的更改便持久的保存在数据库中，不会被回滚。

##### NOSAL数据库 - 侧重高性能与可扩展性，弱化事务功能
NoSQL系统通常注重性能和扩展性，而弱化了事务的能力。

NoSQL系统仅提供对行级别的原子性保证，也就是说同时对同一个Key下的数据进行的两个不同的操作，在实际执行的时候是会串行的执行，保证了每一个Key-Value对的操作不会被破坏。

例如MongoDB数据库，它是不支持事务机制的，同时也不提倡多表关联的复杂模式设计，它只保证对单个文档读写的原子性。


## 区分：数据库ACID中的一致性 、 分布式一致性
	数据库事务的ACID特性的C就代表一致性，可以简单的把一致性理解为正确性或者完整性，数据一致性通常指关联数据之间的逻辑关系是否正确和完整。
	一致性通过数据库所提供的“事务”能力来保证。

	分布式系统中的一致性，指的是分布式节点中，每个节点的数据副本是相同的，保持数据的一致。



# CAP 理论 - 针对NoSQL分布式数据存储而言（重点）

CAP指的是Consistency(强一致性)、Availability(可用性)、Partition tolerance(分区容错性)。

#####强一致性 （集群内部所有节点上的存储数据都是相同的）
Consistency means that data is the same across the cluster, so you can read or write from/to any node and get the same data.

系统在执行过某项操作后仍然处于一致的状态。

在分布式系统中，更新操作执行成功后所有的用户都应该读到最新的值，这样的系统被认为是具有强一致性的。 


#####可用性（集群始终对外表现为可用，即使某个节点宕机）
Availability means the ability to access the cluster even if a node in the cluster goes down.

即使集群中有部分节点不可用，但集群仍然可以对外提供服务。

可用性（Availablity）：任何一个没有发生故障的节点，会在合理的时间内返回一个正常的结果（成功或者失败），也就是对于用户的每一个请求总是能够在有限的时间内返回结果；

#####分区容错性（节点间遇到网络通信问题）
Partition tolerance means that the cluster continues to function even if there is a "partition" (communication break) between two nodes (both nodes are up, but can't communicate).

当系统中的一部分节点无法和其他节点进行通信，集群仍然能够正常对外提供服务。

分区容忍性（Partition-torlerance）：当节点间出现网络分区（不同节点处于不同的子网络，子网络之间无法联通，也就是被切分成了孤立的集群网络），照样可以提供满足一致性和可用性的服务，除非整个网络环境都发生了故障。

######常见的数据存储方案
	关系型数据库 MYSQL
	键值对数据库 REDIS
	列式数据库   HBASE
	文档数据库   MONGODB
![](img/CAP.png)

不能同时满足3个特性，最多满足其中2个。

	C 一致性 （Consistency）: 
		对外提供数据服务时，所有节点上的数据都是一致的，即各个节点数据需要具有强一致性。
	A 可用性（Availability）：
		每个请求都能够收到一个响应，无论结果是成功或者失败，集群对外表现为可用的。
	P 分区容错（Partition-tolerance）- 网络通信问题总是不可避免的：
		系统应该持续提供服务，即使系统内部（某个/些节点出现分区）有消息丢失。
		比如交换机失败、网址网络被分成几个子网，子网间网络不通，形成脑裂；
		服务器发生网络延迟或死机，导致某些 server 与集群中的其他机器失去联系。

由于网络是不可靠的，因此出现网络分区是无法避免的。
所以，分布式系统的设计需要根据自身的特点进行取舍，选择的侧重点不同：

	CP：强调集群间各个节点数据的一致性，弱化高可用性。当无法保证数据一致性时，集群将拒绝对外提供服务。

	AP：强调集群服务的高可用性，弱化一致性，通过“最终一致性”来保证业务逻辑的正确。


### CP - 比如，Redis/HBase/MongoDB，强调集群中各节点数据的一致性，弱化高可用性
data is consistent between all nodes, and maintains partition tolerance (preventing data desync) by becoming unavailable when a node goes down.

CP 类型的存储当遇到某个节点宕机时，集群将会整体不可用，以保证数据的一致性要求。


### AP - 比如，Cassandra，强调集群的高可用性，弱化数据的一致性
nodes remain online even if they can't communicate with each other and will resync data once the partition is resolved, but you aren't guaranteed that all nodes will have the same data (either during or after the partition)

AP 类型的存储不保证数据一致性，即使集群中的节点遇到通信问题，也会正常对外提供服务。


### 为什么C与A不能同时满足？
必须明确一点：对于分布式系统而言，分区容错性是必须要满足的，因为分区的出现是必然，也是必须要解决的问题。

所以，P必须要保证，那么我们就只能在C和A之间做权衡。

	有两个或以上节点时，当网络分区发生时，集群中两个节点不能相互通信。
	此时如果要保证数据的一致性C，那么必然会有一个节点被标记为不可用的状态，
	这就违反了可用性A的要求，只能保证CP。

	反之，如果保证可用性A，即两个节点可以继续各自处理请求，
	但是由于网络分区导致节点间不能同步数据，必然又会导致数据的不一致，只能保证AP。


---
# BASE 理论
BASE 全称是 Basically available,soft-state,Eventually Consistent

BASE理论是：BASE是指基本可用（Basically Available）、软状态（ Soft State）、最终一致性（ Eventual Consistency）。

BASE是对CAP中一致性和可用性权衡的结果，其来源于对大规模互联网系统分布式实践的总结，是基于CAP定理逐步演化而来的。

其核心思想是即使无法做到强一致性(Strong consistency)，但每个应用都可以根据自身的业务特点，采用适当的方式来使系统达到最终一致性(Eventual consistency)。

### Basically available 系统基本可用
在分布式系统出现不可预知的故障时，允许瞬时部分可用性。

基本可用是指分布式系统在出现不可预知故障的时候，允许损失部分可用性——但请注意，这绝不等价于系统不可用。以下两个就是“基本可用”的典型例子。
	
	1. 响应时间上的损失：正常情况下，一个在线搜索引擎需要在0.5秒之内返回给用户相应的查询结果，但由于出现故障（比如系统部分机房发生断电或断网故障），查询结果的响应时间增加到了1～2秒。
	
	2. 功能上的损失：正常情况下，在一个电子商务网站上进行购物，消费者几乎能够顺利地完成每一笔订单，但是在一些节日大促购物高峰的时候，由于消费者的购物行为激增，为了保护购物系统的稳定性，部分消费者可能会被引导到一个降级页面。

### soft-state  软状态
弱状态也称为软状态，和硬状态相对，是指允许系统中的数据存在中间状态，并认为该中间状态的存在不会影响系统的整体可用性，即允许系统在不同节点的数据副本之间进行数据同步的过程存在延时。

### Eventually consistent 数据最终一致性
所有数据副本在一段时间的同步后最终都能达到一致的状态。

最终一致性强调的是系统中所有的数据副本，在经过一段时间的同步后，最终能够达到一个一致的状态。

因此，最终一致性的本质是需要系统保证最终数据能够达到一致，而不需要实时保证系统数据的强一致性。

#####总的来说，
BASE理论面向的是大型高可用可扩展的分布式系统，和传统事务的ACID特性是相反的，它完全不同于ACID的强一致性模型，而是提出通过牺牲强一致性来获得可用性，并允许数据在一段时间内是不一致的，但最终达到一致状态。但同时，在实际的分布式场景中，不同业务单元和组件对数据一致性的要求是不同的，因此在具体的分布式系统架构设计过程中，ACID特性与BASE理论往往又会结合在一起使用。