# spring使用了哪些设计模式？
	【单例模式、原型模型】
		单例模式：
			系统中仅存在唯一的对象实例，是一个共享的对象。
			适用于无状态的Bean，比如Controller,Service,Repositoy等都是单例的
			spring创建的Bean默认就是单例。

		原型模式：
			通过一个原型工厂，基于原型进行“克隆”，返回新的对象给调用者使用。
			使用 @Prototype 可以将Bean声明为多例的，这样每次从IOC容器获取都是新的对象。
	
	【工厂模式】
		基于最顶层BeanFactory接口，实现了工厂模式。
		通过类全名进行对象的创建。

	【代理模式】
		事务代理 TransactionManager

	【模板方法模式】
		JDBCTemplate

		DispatcherServlet中的doService()
			DispatcherServlet extends FrameworkServlet extends HttpServlet
			HttpServlet中的doGet,doPost等方法被FrameworkServlet重写
			重写为调用processRequest(),而processRequest()内部则调用抽象的doService()
			doService()在DispatcherServlet中进行了具体实现
			DispatcherServlet内部通过doDispatch()对HTTP请求进行转发处理



