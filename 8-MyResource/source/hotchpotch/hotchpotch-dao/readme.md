## model与entity（实体类）的区别
model的字段 > entity的字段，并且model的字段属性可以与entity不一致，model是用于前端页面数据展示的，而entity则是与数据库进行交互做存储用途。

	举个例子：
		比如在存储时间的类型时，数据库中存的是datetime类型，entity获取时的类型是Date（）类型，date型的数据在前端展示的时候必须进行类型转换（转为String类型），在前端的进行类型转换则十分的麻烦，转换成功了代码也显得十分的臃肿，所以将entity类型转换后，存储到对应的model中，在后台做类型转换，然后将model传到前端显示时，前端就十分的干净。同时也可以添加字段，作为数据中转。
		
		entity -> 数据库表
		model 	 -> 数据封装，用于返回给客户端使用；model的数据来自entity，但是可以扩展出更多的字段让前端使用更方便！
		
		
## JBBC连接串中时区的问题
	serverTimezone=UTC 错误（会导致程序查询出来的时间与数据库存储的时间相差几个消息）
	serverTimezone=Asia/Shanghai	正确（东八区时间）
	

