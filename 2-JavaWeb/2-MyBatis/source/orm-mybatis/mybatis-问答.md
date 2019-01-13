## mybatis 的事务管理如何实现？
	JDBC – 这个配置就是直接使用了 JDBC 的提交和回滚设置
	MANAGED – 让容器来管理事务的整个生命周期
	
	注意：若使用 Spring + MyBatis，则没有必要配置事务管理器， 事务由 Spring进行管理。
	
	
## mybatis 的连接池如何实现？
	mybatis提供了3种方式：
		UNPOOLED - 不适用连接池，每次打开新的连接
		POOLED - mybatis内部维护一个数据库连接池
		JNDI - 由容器管理数据库连接池

	注意：mybatis还支持第三方的连接池，配置方式为：
		通过实现接口 org.apache.ibatis.datasource.DataSourceFactory 来使用第三方数据源；
		或者，直接继承org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory；
		
		<dataSource type="org.myproject.C3P0DataSourceFactory">
		  <property name="driver" value="org.postgresql.Driver"/>
		  <property name="url" value="jdbc:postgresql:mydb"/>
		  <property name="username" value="postgres"/>
		  <property name="password" value="root"/>
		</dataSource>

## mybatis 插入时id自增是怎么实现的？
	insert 标签上通过配置 useGeneratedKeys=true, keyProperty=id
	默认useGeneratedKeys=false
	
## mybatis 的 Example 是什么？
	类似Hibernate基于对象编程的方式，实现SQL逻辑
	不推荐使用，因为SQL看起来没有那么直观了！
	
## mybatis 支持 xml 与 接口方法上写注解 编写SQL，两者有什么区别？
	1、xml 方式 与 注解方式，两者是互补的关系
	2、对Mapper的同一个方法而言，要么通过xml写SQL，要么通过注解写SQL
	3、复杂关联查询，动态SQL，使用xml方式编写SQL更方便
	
## resultType 与 resultMap 的区别？
	结果映射方式，使用 resultType 或 resultMap，但不能同时使用。
	resultType适合对单一查询进行结果映射
	resultMap更灵活，可以选择性的对字段进行结果映射
	resultMap功能更强，可以在内部嵌套子标签实现关联查询
	
## mybatis 一级缓存？
	一级缓存默认开启
	一级缓存是session级别的，在一个sqlSession生命周期内有效
	
## mybatis 二级缓存？
	二级缓存默认关闭
	二级缓存基于namespace的，mapper文件中的namespace相当于二级缓存的key
	不建议使用，一般采用第三方缓存框架，比如redis
	
	对于关联查询的情况，关联查询写在哪个namespace下，二级缓存就放在哪个namespace下。
	
## mybatis 的缓存更新策略？
	flushCache	默认值：true（对应插入、更新和删除语句）。
	将其设置为 true，任何时候只要语句被调用，都会导致本地缓存和二级缓存都会被全部清空
	因此，一般不建议使用mybatis的缓存策略，而是用第三方缓存框架来提供缓存功能。
	

		
	

## typehandler ?
	无论是 MyBatis 在预处理语句（PreparedStatement）中设置一个参数时，还是从结果集中取出一个值时， 都会用类型处理器将获取的值以合适的方式转换成 Java 类型。
	
	自定义typehandler: 	extends BaseTypeHandler<E> 
	
## mybatis 分页如何做的？
	使用开源插件PageHelper进行分页；
	PageHelper利用mybatis Plugin的扩展来进行实现。
	拦截Excutor的query方法，增加分页相关逻辑。

	@Intercepts(@Signature(
		type = Executor.class, 
		method = "query", 
		args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}))
	public class PageHelper implements Interceptor {
		...
	}

-------------------------------------------------------------------

## 关联查询-嵌套查询（存在N+1的隐患）
	N+1 查询问题可以是这样引起的:
		执行了一个单独的 SQL 语句来获取结果列表(就是“+1”)，
		接着对返回的每条记录,执行了一个单独的查询语句来加载关联的数据(就是“N”)。
	
	N+1问题如何解决？
	使用懒加载策略，直到真正需要获取关联表的数据时，才发出第二次SQL查询	
	好处：先查A表的数据，如果后面不访问B表的数据，那1次查询就够了（需开启懒加载，否则会连续发出2个SQL查询）。

	懒加载缓解了N+1问题，如何彻底避免N+1呢？
		使用JOIN联合查询，1次SQL查询关联表的数据。
	
## 关联查询-嵌套结果（JOIN查询）	
	只会发出1次SQL，通过JOIN方式1次就完成数据的查询。不存在N+1问题。	
	
	
## 关联关系的表达方式
	1对1	association
	1对多	collection

	