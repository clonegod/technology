# 一切以对象为中心进行设计

---

## 以对象为中心进行学习

	从Java语言层面看：
		Java 是一门面向对象的语言，以对象为中心进行编程
	
	从设计模式层面看：	
		封装、继承、多态最终也是应用到具体对象的行为上
	
	从JVM底层内存管理层面看：
		JVM提供了自动内存管理功能，程序中实例化的对象存放JVM的堆中，由GC在某个时机自动清理
		堆内存受物理内存的限制，需要及时回收内存，GC最主要的工作就是回收堆内存中的对象

## Spring 如何以Bean为中心的呢？
	我们知道Java开发，面向对象，关键在于对象
	一切Java系统的功能，都是由不同对象相互协作完成的

	关键来了：对象的创建、管理，谁来做呢？？？
	---> Spring通过IOC容器来实现了这部分功能
	
	开发人员只需要向spring声明，哪些类需要创建对象，spring就会自动去创建这些对象，并放到IOC容器中。
	当开发人员需要使用的时候，同样是声明，比如@Autowired，告诉spring，把对象给我注入进来。

	同时，IOC容器也是spring的核心，很多功能都在此基础上进行扩展实现的！

![](img/spring-overview.png)
	
## spring 主要模块的功能
	【核心容器】
		spring-beans & spring-core 
			spring的核心模块，包含控制反转和依赖注入。
			BeanFactory是spring框架的核心接口，是工厂模式的实现。
			BeanFactory实例化之后，不会立即实例化Bean，只有当Bean需要被使用时，BeanFactory才会对Bean进行实例化。
		
		spring-context
			构建于核心模块之上，扩展了BeanFactory
			添加了Bean生命周期的管理、框架事件体系、资源加载等功能
			ApplicationContext实例化之后，会初始化所有单例的Bean，使之处于待用状态。
		
		spring-expression
			提供EL表达式的支持
			运行时动态执行表达式，具有一定的动态性
	
	【AOP切面编程】
		spring-aop
			spring的另一个核心模块，提供AOP编程的支持。
			AOP可以看作是面向对象的一个补充，以动态代理技术为基础，设计一系列的AOP横切实现
		
		spring-aspect
			该模块集成了AspectJ框架，主要为springAOP提供各种AOP实现方案。
		
		spring-instrument	
			该模块基于java.lang.instrument进行扩展
			也可以理解为对AOP实现的一种支持。
			在JVM启动时，生成一个代理类，通过代理类在运行时修改类的字节码，从而改变类的功能，实现AOP的功能。
	
	【数据访问支持】
		spring-jdbc
			对JDBC访问进行了同一个抽象与封装，简化JDBC编程。

		spring-tx
			spring对JDBC事务控制的实现模块。
			通过AOP技术，以配置的方式对事务进行控制，比如在serive层使用@Transactional
			事务控制一定要放在业务层：
				一个完整的业务应该对应到业务层中的一个方法！
				如果业务操作成功，则全部提交；否则，整个事务必须回滚！

		spring-hibernate
			主要集成了hibernate框架，JPA等数据访问的操作			

		spring-jms
			提供Java消息服务，比如对ActiveMQ发送和接收消息。			

		spring-oxm
			提供XML数据处理的功能： 对象转XML， XML转对象
		
	【WEB模块】
		spring-web
			在核心容器的基础上，提供web相关基础功能的支持。
			通过Servlet或Listener与Tomcat容器进行绑定
			在容器启动时，初始化IOC容器

		spring-webMVC
			MVC模式在spring中的应用
		
		spring-websocket
			提供与WEB前端的全双工通信协议支持。

## spring核心点1 - IOC容器
	由spring自动创建Bean，存储到IOC容器中进行管理

## spring核心点2 - DI
	从IOC容器找到匹配的Bean，自动将其注入到需要的地方，即依赖反转

## spring核心点3- AOP
	通过代理技术对Bean的功能进行增强
	AOP是一种编程思想，具体通过代理Proxy来实现相关功能
	基于AOP技术，可以将某类统一的逻辑动态应用到系统的相关“切面”上

## spring 数据访问层
	对数据层的各种技术进行了统一风格的封装
	JDBCTemplate
	RedisTemplate
	MongoTemplate
	

## spring Web MVC 
	以DispatchServlet为入口点，处理客户端发起的HTTP请求
	
	Interceptor： 在调用Controller中的Handler之前、之后，提供拦截控制的功能
	Controller：封装Handler，在Handler上绑定路由，接收HTTP请求
	ViewResovler: 视图解析器，视图可以是HTML，JSP，Thymeleaf等模板，或者JSON数据等
	