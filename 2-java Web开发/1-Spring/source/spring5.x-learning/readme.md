https://docs.spring.io/spring/docs/5.0.6.RELEASE/spring-framework-reference/

https://github.com/spring-projects/spring-framework


JAVA 
    - 面向对象，一切都围绕着对象进行展开：
        Spring的核心BeanFactory管理所有的对象；
        AOP切面定义拦截的切点，使用代理技术对对象进行功能的扩展，比如透明的RPC调用访问远程服务；
        JVM中堆内存中对象的自动回收；
        
# Spring源码学习方法
    
    1、分模块学习，分而治之
        bean实例化 -> web层接收请求 -> db层操作数据库
        Core ---> IOC容器：Bean的实例化
        Web  ---> WebMvc：前端控制器FrontController，接收http请求，路由匹配，Controller中的handler被调用，参数绑定，调用业务层执行业务操作，返回结果
        Data ---> 操作数据库：JDBC/ORM + Transaction（AOP、代理模式）
        
    2、抓主线，针对该模块的核心功能，理清主干流程
        IOC容器：定位需要加载的配置文件，加载并解析配置文件，注册Bean实例到IOC容器
        WebMvc：http请求的URI如何映射到对应的Controller的method上，@RequestBody，@ResponseBody的实现原理，内容协商机制，视图解析器
        
    3、AOP+代理模式在spring中的应用
        事务管理的实现原理
        
# Spring 底层核心机制
    1、准备Bean ---> IOC容器，集中存放实例化的Bean（读取xml或扫描annotation，通过反射技术对Bean进行实例化，维护到BeanFactory中-Map容器）
    2、注入Bean ---> DI，自动维护对象之间的关系 （依赖注入，通过@Autowired注入需要IOC容器中已经实例化好的Bean）
    3、增强Bean ---> AOP（使用代理技术对Bean进行增强，透明加入附加功能，比如JDBC事务管理）
    

# Spring 功能分类
    Core	
        IoC container, Events, Resources, i18n, Validation, Data Binding, Type Conversion, SpEL, AOP.

    Data Access	
        Transactions, DAO support, JDBC, ORM, Marshalling XML.

    Web Servlet	
        Spring MVC, WebSocket, SockJS, STOMP messaging.

    Web Reactive	
        Spring WebFlux, WebClient, WebSocket.

    Integration	
        Remoting, JMS, JCA, JMX, Email, Tasks, Scheduling, Cache.

    Languages	
        Kotlin, Groovy, Dynamic languages.
    
    Testing	
        Mock objects, TestContext framework, Spring MVC Test, WebTestClient.
    
    
# Spring 模块分类
    Core
        spring-core 核心模块，实现控制反转(Bean容器-BeanFactory)和依赖注入（@Autowired）
        spring-beans 核心模块，实现控制反转(Bean容器-BeanFactory)和依赖注入（@Autowired）
        spring-context  提供Bean生命周期的管理，框架事件体系，资源的透明加载，顶层接口ApplicaitonContext
        spring-context-support
        spring-context-indexer
        spring-aop  面向切面
        spring-aspects  动态代理，底层原来ASM框架，CGLIB库
        spring-expression EL表达式语言
        
    Data Access
        spring-jdbc JDBC抽象与封装，提供JDBCTemplate模板进行CRUD
        spring-orm  对ORM框架的封装，Hibernate，JPA
        spring-tx   提供事务管理的功能
        spring-oxm  处理Bean与XML的转换
        
    Web
        spring-webmvc 前端控制器Front Controller， MVC模式的实现，基于Servlet规范，运行于Tomcat容器，通过Servlet或Listener触发IOC容器的初始化
        spring-webflux  非阻塞函数式Reactive Web框架，异步、非阻塞、事件驱动，底层使用Netty作为服务器
        spring-websocket 与WEB前端的全双工通信，服务器可以向客户端主动推送数据
      
    Message
        spring-jms  消息通信
        spring-messaging 消息通信
        
    Test
        spring-test 在spring环境下进行单元测试、集成测试
        
---
## Core - IOC容器
    BeanFactory 作为顶层接口，封装了操作Bean的相关接口，比如getBean(xxx)
    
    IOC容器初始化的流程：定位、加载、注册
    
    定位， 查找相关的资源文件的位置 --- 资源配置 import, classpath:*, url
    加载， 解析XML配置文件，把bean包装为BeanDefinition对象，BeanDefinition将配置文件中对bean的定义和依赖的属性信息以对象的形式保存在内存中
    注册， 根据BeanDefinition中的信息，对对象进行实例化，放入IOC容器中。
    
    BeanDefinition对象 与 BeanWrapper：
        类对象的实例化---从BeanDefinition中获取类的相关信息，通过反射机制调用构造函数，完成对象的实例化。
        依赖对象的注入---从BeanDefinition中获取需要注入的对象，通过反射机制对依赖的对象进行注入操作。
        BeanDefinition保存了原始的Bean定义相关的信息，加载之后不会修改。
        Bean实例化都是根据原始BeanDefinition来创建对象的，新创建的对象将放入到一个缓存Map中，不会污染原始的BeanDefinition。
        BeanDefinitionResolver实现对BeanDefinition中保存的信息进行解析。
        
        BeanWrapper对原始对象进行了包装，可以理解为装饰器模式的应用，最后放入到factoryBeanCache中的是BeanWrapper对象。
        这样做的好处：不修改原始对象，且在原始对象的基础上实现功能扩展，比如：加入监听器、回调函数等
        
      
## 问答
    Spring中的Bean在什么时候触发自动初始化和依赖注入的？
        1、调用getBean(beanName)的时候，才进行创建和依赖注入。
        2、容器在完成BeanDefinition初始化之后，会在finishBeanInitialization()中判断Bean是否为init-eager，如果是，则调用getBean()，将Bean注入到IOC容器中，并完成依赖注入。
    
    BeanFactory 和 FactoryBean的区别？
        BeanFactory，重点在于Factory，是一个顶层接口，对外暴露的方法主要是getBean的重载方法。底层使用策略模式来提供各种BeanFactory的具体实现。
        FactoryBean，重点在于Bean，所有Factory生产出来的Bean都实现了FactoryBean接口。即，BeanFactory生产的所有对象都可以称为FactoryBean。
        Spring所创建出来的一切对象都是FactoryBean
        
        
--- SpringMVC
    从服务器接收到一个http请求，到返回结果的整个流程，spring MVC将流程拆分为不同的步骤进行处理
        1、路由匹配 HandlerMapping - @RequestMapping
        2、参数提取与绑定 HandlerAdapter - @RequestParam @PathVariable  @RequestBody 
        3、参数校验 @Validation  JSR330
        4、调用service层进行处理，将返回的结果封装到Model中
        5、视图解析 ViewResolver - 根据返回结果的内容类型，选择最佳的视图解析器
        6、将Model中的value填充到视图中
        7、返回View给客户端
        8、整个流程中出现异常时的全局处理


--- Aop + 代理

ioc, di, mvc, aop, transaction


    

    
    