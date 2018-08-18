# Spring中的事务配置

---
## Spring 事务配置的注意点

#### 事务的回滚策略
	1、 默认，发生RuntimeException或者Error异常，会导致事务回滚。
		不过可以自定义哪些异常类型才让事务回滚， roolbackFor=xxxException

		# TransactionAspectSupport.completeTransactionAfterThrowing
		public boolean rollbackOn(Throwable ex) {
			// 默认RuntimeException、Error异常，会导致事务发生回滚
			return (ex instanceof RuntimeException || ex instanceof Error);
		}
	
	>>> 关键点: 
	当业务方法中发生异常时，一定要将异常抛出去，让spring可以检测到发生了异常，
	否则spring无法捕获到异常，导致异常时的回滚机制失效！

####  事务隔离级别
	2、事务隔离级别的配置 （java.sql.Connection 中定义的事务隔离级别）
	
		int TRANSACTION_NONE             = 0;
		int TRANSACTION_READ_UNCOMMITTED = 1; // 可能读到其它事务尚未提交的数据，但这个数据之后又被回滚了
		int TRANSACTION_READ_COMMITTED   = 2; // 禁止了脏读，允许不可重复读和幻读
		int TRANSACTION_REPEATABLE_READ  = 4; // 禁止了脏读、不可重复读，允许幻读
		int TRANSACTION_SERIALIZABLE     = 8; // 禁止了脏读、不可重复读、幻想读，事务串行执行，慎用！
		

		不可重复读：针对行的修改
			事务1发起第1次查询
			事务2修改了这个行的数据
			事务1以相同条件再次查询，查询的结果不应该查询到修改后的行。

		幻想读：针对行的新增
			事务1发起第1次查询
			事务2新增了一行数据，该行数据满足事务1所发起的查询条件
			事务1以相同条件再次查询，查询的结果不应该查询到新增的行。

		

####  事务传播特性
	3、事务传播特性的配置 （org.springframework.transaction.TransactionDefinition 中定义）
		》》》 指定被执行的方法，需要在何种事务环境下运行。

		int PROPAGATION_REQUIRED = 0; // 支持当前事务，或者没有的话就新建一个事务
		int PROPAGATION_SUPPORTS = 1; // 支持当前事务，没有的话就在无事务环境下运行
		int PROPAGATION_MANDATORY = 2; // 支持当前事务，否则抛出异常
		int PROPAGATION_REQUIRES_NEW = 3; // 创建新的事务，如果已存在事务，则挂起已存在的事务
		int PROPAGATION_NOT_SUPPORTED = 4; // 不支持事务，总是以非事务方式运行
		int PROPAGATION_NEVER = 5;	 // 不支持事务，如果已存在事务，则抛异常
		int PROPAGATION_NESTED = 6; // 如果当前存在事务，在该事务内部以嵌套事务运行
		int ISOLATION_DEFAULT = -1; // 依赖底层数据库配置

---
# 相关源码分析

## java.sql.Connection
	Connection表示：底层数据库驱动与数据库建立的连接。
	一个Connection就是Driver与DB之间的一个TCP连接。
	
	Java制定了Connection接口，由不同的数据库厂商提供相关的驱动包。
	MySQL提供

## javax.sql.DataSource
	DataSource 是对Connection的包装，提供了获取Connection的相关接口。

## org.springframework.jdbc.datasource.DataSourceTransactionManager
	DataSourceTransactionManager 内部包含了DataSource的引用，因此可以获取到底层的Connection
	
	DataSourceTransactionManager中的commit()，rollback() 调用都是委托给真正的connection来完成的。

#
	@Override
	protected void doCommit(DefaultTransactionStatus status) {
		DataSourceTransactionObject txObject = (DataSourceTransactionObject) status.getTransaction();
		Connection con = txObject.getConnectionHolder().getConnection();
		if (status.isDebug()) {
			logger.debug("Committing JDBC transaction on Connection [" + con + "]");
		}
		try {
			con.commit();
		}
		catch (SQLException ex) {
			throw new TransactionSystemException("Could not commit JDBC transaction", ex);
		}
	}

	@Override
	protected void doRollback(DefaultTransactionStatus status) {
		DataSourceTransactionObject txObject = (DataSourceTransactionObject) status.getTransaction();
		Connection con = txObject.getConnectionHolder().getConnection();
		if (status.isDebug()) {
			logger.debug("Rolling back JDBC transaction on Connection [" + con + "]");
		}
		try {
			con.rollback();
		}
		catch (SQLException ex) {
			throw new TransactionSystemException("Could not roll back JDBC transaction", ex);
		}
	}


## org.springframework.transaction.interceptor.TransactionInterceptor 
	implements org.aopalliance.intercept.MethodInterceptor from cglib.jar
	
	// AOP 拦截到需要被事务管理的方法的执行
	@Override
	public Object invoke(final MethodInvocation invocation) throws Throwable {
		// Work out the target class: may be {@code null}.
		// The TransactionAttributeSource should be passed the target class
		// as well as the method, which may be from an interface.
		// AopUtils 从代理对象中找到真正的target对象
		Class<?> targetClass = (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);
		
		// Adapt to TransactionAspectSupport's invokeWithinTransaction...
		return invokeWithinTransaction(invocation.getMethod(), targetClass, new InvocationCallback() {
			@Override
			public Object proceedWithInvocation() throws Throwable {
				return invocation.proceed();
			}
		});
	}

	// 在事务环境下，执行数据库操作
	protected Object invokeWithinTransaction(Method method, Class<?> targetClass, final InvocationCallback invocation)
			throws Throwable {

		// If the transaction attribute is null, the method is non-transactional.
		final TransactionAttribute txAttr = getTransactionAttributeSource().getTransactionAttribute(method, targetClass);
		final PlatformTransactionManager tm = determineTransactionManager(txAttr);
		final String joinpointIdentification = methodIdentification(method, targetClass);

		if (txAttr == null || !(tm instanceof CallbackPreferringPlatformTransactionManager)) {
			// Standard transaction demarcation with getTransaction and commit/rollback calls.
			TransactionInfo txInfo = createTransactionIfNecessary(tm, txAttr, joinpointIdentification);
			Object retVal = null;
			try {
				// This is an around advice: Invoke the next interceptor in the chain.
				// This will normally result in a target object being invoked.
				retVal = invocation.proceedWithInvocation();
			}
			catch (Throwable ex) {
				// target invocation exception
				// 判断异常类型，决定是否回滚事务
				completeTransactionAfterThrowing(txInfo, ex);
				throw ex;
			}
			finally {
				cleanupTransactionInfo(txInfo);
			}
			// 没有回滚事务，则提交事务
			commitTransactionAfterReturning(txInfo);
			return retVal;
		}

		else {
			// It's a CallbackPreferringPlatformTransactionManager: pass a 
			}
			catch (ThrowableHolderException ex) {
				throw ex.getCause();
			}
		}
	}

