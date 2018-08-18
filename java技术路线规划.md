# java 技术路线

##【java 核心技术】
    JVM
        JMM Java Memory Model
            Happens-Before
        堆内存结构
            新生代（Eden+Survior0+Survior1）、老年代、永久区
        垃圾回收算法
            引用计数、复制算法、压缩算法
        垃圾回收器
            Serial, ParallelGC, CMS, G1
        内存诊断命令
            jps, jstat, jstack, jmap
        GC日志分析
            OOM, DeadLock
    
    多线程并发库
        java5 - JUC
        java7 - ForkJoin
        java8 - CompletableFuture
        
    I/O
        BIO, NIO, AIO
        
    设计模式
        代理模式
        模板方法

## 【java 开源框架】
    spring
        spring MVC, spring 事务管理, spring-jdbc
        
    mybatis
        generator， 分页插件
        
## 【分布式系统相关】    
    分布式通信 - netty
    分布式协调 - zookeeper
    分布式服务治理 - Dubbo
    分布式消息队列 - Kafka, RocketMQ
    分布式缓存 - Redis
    分布式解决方案：
        分布式全局唯一ID
        分布式事务
        

## 【微服务】
    SpringBoot
    
    SpringCloud
        eureka, zuul, ribbon ...
        
##【数据存储】
    >>> MySQL
        建库、建表、建索引、建约束
        binlog原理、主从机制
        索引机制、使用规则
    
    >>> MongoDB
        分布式数据存储

##【服务器】
    Linux
        常用命令：top, find, grep, tail
        脚本编写
        
    Tomcat  
        配置、参数调优

##【团队开发】
    git
    maven
    jenkins
    docker
    
    
## 【其它框架】
    elk 日志收集、存储，实现快速日志搜索
    
    
    
    
    