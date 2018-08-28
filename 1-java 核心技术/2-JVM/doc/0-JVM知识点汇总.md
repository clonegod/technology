# 串联JVM相关知识点

## 编码
	java源代码 -> 编译 -> Class字节码 -> 添加到classpath路径中

## JVM使用class涉及的流程
	classpath路径 -> 类加载器 -> 验证 -> 准备 -> 初始化 -> 使用 -> 类卸载

## 类加载器
	不同的类加载器负责加载不同用途/功能的类
	BootStrapClassLoader ---- 加载JDK中的核心类文件
	ExtClassLoader ---- 加载jre/lib/ext下的扩展类库
	AppClassLoader ---- 加载用户应用程序的类文件
	CustomerClassLoader ---- 用户自定义类加载，完成特定类加载功能（打破双亲委派规则）

## JVM在运行过程中是如何存储数据的
	字节码原始数据结构 ---- 永久区/方法区 Perm区
	字节码的Class类表示 ----- 堆内存
	new xxxClass() ----- 堆内存 Xms Xmx NewRation XmnSize SurviorRatio
	新对象 ---- 新生代（Eden + from + to），新生对象太多导致Eden区内存不够，触发MinorGC 
	老对象 ---- 老年代，老年代对象太多导致Old区内存不够，触发MajorGc
	方法调用 ---- 栈内存 Xss
	本地方法调用 ---- 本地线程栈，本地方法调用的栈
	程序计数器 ---- 程序计数器，保存线程所执行到的位置（多线程不断切换执行，需要保存执行点）

## JVM在运行过程中会出现哪些问题
	堆内存的空闲空间不足，需要进行垃圾清理-----GC
	垃圾回收器有哪些？使用的什么垃圾算法？
	什么场景下使用哪种回收器比较合适？
	如果出现内存溢出怎么办？怎样排查问题的原因？如何解决？

