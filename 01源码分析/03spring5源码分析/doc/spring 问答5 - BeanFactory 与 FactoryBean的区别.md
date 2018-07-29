## BeanFactory 与 FactoryBean 的区别？
	
BeanFactory侧重点在于Factory，它是创建Bean的工厂，定义了Bean工厂统一的行为接口。

	比如，ApplicationContext接口 继承了 BeanFactory 。

	spring中有很多的BeanFactory，使用策略模式来选择具体使用哪个BeanFactory子类。

FactoryBean侧重点在于Bean，用于描述该Bean其实是一个factory，可以用来创建其它的Bean。

	spring内部通过一个特殊的符号来进行区分普通Bean和factoryBean。
	当要获取一个beanfactory的时候，需要在beanName之前拼接一个特殊符号"&"

	/**
	 * Used to dereference a {@link FactoryBean} instance and distinguish it from
	 * beans <i>created</i> by the FactoryBean. 
	 * For example, if the bean named {@code myJndiObject} is a FactoryBean, 
	 * getting {@code &myJndiObject} will return the factory, not the instance returned by the factory.
	 */
	String FACTORY_BEAN_PREFIX = "&";

spring中，有一些BeanFactory自身是通过其它的BeanFactory创建得到的。

因此，当通过getBean()获取Bean实例时，如果要获取的不是普通的Bean，而是BeanFactory时，


	



#### BeanFactory

	public interface BeanFactory {

	String FACTORY_BEAN_PREFIX = "&";

	Object getBean(String name) throws BeansException;
	
	<T> T getBean(String name, @Nullable Class<T> requiredType) throws BeansException;
	
	Object getBean(String name, Object... args) throws BeansException;
	
	<T> T getBean(Class<T> requiredType) throws BeansException;
	
	<T> T getBean(Class<T> requiredType, Object... args) throws BeansException;

	boolean containsBean(String name);

	boolean isSingleton(String name) throws NoSuchBeanDefinitionException;

	boolean isPrototype(String name) throws NoSuchBeanDefinitionException;

	boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException;

	boolean isTypeMatch(String name, @Nullable Class<?> typeToMatch) throws NoSuchBeanDefinitionException;

	@Nullable
	Class<?> getType(String name) throws NoSuchBeanDefinitionException;

	String[] getAliases(String name);

}




#### FactoryBean

	public interface FactoryBean<T> {

		T getObject() throws Exception;
	
		Class<?> getObjectType();
	
		boolean isSingleton();
	
	}
