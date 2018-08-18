## Mysql InnoDB 排他锁


用法： select … for update;

例如：select * from goods where id = 1 for update;

排他锁的申请前提：没有线程对该结果集中的任何行数据使用排他锁或共享锁，否则申请会阻塞。

for update仅适用于InnoDB，且必须在事务块(BEGIN/COMMIT)中才能生效。

在进行事务操作时，通过“for update”语句，MySQL会对查询结果集中每行数据都添加排他锁，其他线程对该记录的更新与删除操作都会阻塞。


## 排他锁：行锁、表锁
	InnoDB行锁是通过给索引上的索引项加锁来实现的，只有通过索引条件检索数据，InnoDB才使用行级锁，否则，InnoDB将使用表锁。



## 场景分析
假设有一张商品表 goods，它包含 id，商品名称，库存量三个字段，表结构如下：

	CREATE TABLE `goods` (
	  `id` int(11) NOT NULL AUTO_INCREMENT,
	  `name` varchar(100) DEFAULT NULL,
	  `stock` int(11) DEFAULT NULL,
	  PRIMARY KEY (`id`),
	  UNIQUE KEY `idx_name` (`name`) USING HASH
	) ENGINE=InnoDB 

插入如下数据：
	
	INSERT INTO `goods` VALUES ('1', 'prod11', '1000');
	INSERT INTO `goods` VALUES ('2', 'prod12', '1000');
	INSERT INTO `goods` VALUES ('3', 'prod13', '1000');


## 并发更新时的数据一致性
假设有A、B两个用户同时各购买一件 id=1 的商品，用户A获取到的库存量为 1000，用户B获取到的库存量也为 1000，用户A完成购买后修改该商品的库存量为 999，用户B完成购买后修改该商品的库存量为 999，此时库存量数据产生了不一致。


》》》两种解决方案：

	悲观锁方案：

	每次获取商品时，对该商品加排他锁。
	也就是在用户A获取获取 id=1 的商品信息时对该行记录加锁，期间其他用户阻塞等待访问该记录。
	悲观锁适合写入频繁的场景。
	
	begin;
	select * from goods where id = 1 for update;
	update goods set stock = stock - 1 where id = 1;
	commit;

#
	乐观锁方案：

	每次获取商品时，不对该商品加锁。
	在更新数据的时候需要比较程序中的库存量与数据库中的库存量是否相等，如果相等则进行更新，
	反之程序重新获取库存量，再次进行比较，直到两个库存量的数值相等才进行数据更新。
	乐观锁适合读取频繁的场景。

	#不加锁获取 id=1 的商品对象
	select * from goods where id = 1
	
	begin;
	#更新 stock 值，这里需要注意 where 条件 “stock = cur_stock”，只有程序中获取到的库存量与数据库中的库存量相等才执行更新
	update goods set stock = stock - 1 where id = 1 and stock = cur_stock;
	commit;


如果我们需要设计一个商城系统，该选择以上的哪种方案呢？

查询商品的频率比下单支付的频次高，基于以上可能会优先考虑第二种方案。



#### select ... for update
	根据查询条件锁定相关记录。
	一定要在事务环境下执行，否则会在查询结束后马上释放锁。
	锁定记录后应该尽快执行update操作并提交事务，否则长时间运行事务将影响数据库性能。

	SELECT ... FOR UPDATE will lock the record with a write (exclusive) lock until the transaction is completed (committed or rolled back).

	To select a record and ensure that it's not modified until you update it, you can start a transaction, select the record using SELECT ... FOR UPDATE, do some quick processing, update the record, then commit (or roll back) the transaction.
	
	If you use SELECT ... FOR UPDATE outside of a transaction (autocommit ON), then the lock will still be immediately released, so be sure to use a transaction to retain the lock.
	
	For performance, do not keep transactions open for very long, so the update should be done immediately.

	悲观锁
		begin;
		select * from goods where id = 1 for update;
		update goods set stock = stock - 1 where id = 1;
		commit;
	