## What's GCRoots

A garbage collection root is an object that is accessible from outside the heap.

初始存活对象集就是GC Roots。

	1.JAVA虚拟机栈中的本地变量引用对象； 
	2.方法区中静态变量引用的对象； 
	3.方法区中常量引用的对象； 
	4.本地方法栈中JNI引用的对象；

tracing gc的基本思路：
	
以当前存活的对象集为root，遍历出他们（引用）关联的所有对象（Heap中的对象），没有遍历到的对象即为非存活对象，这部分对象可以gc掉。

---

### 根搜索算法

通过一系列的名为“GC Root”的对象作为起点，从这些节点向下搜索，搜索所走过的路径称为引用链(Reference Chain)，当一个对象到GC Root没有任何引用链相连时，则该对象不可达，该对象是不可使用的，垃圾收集器将回收其所占的内存。
主流的商用程序语言C#、java和Lisp都使用根搜素算法进行内存管理。


在java语言中，可作为GC Root的对象包括以下几种对象：

	a.java虚拟机栈(栈帧中的本地变量表)中的引用的对象。
	b.方法区中的类静态属性引用的对象。
	c.方法区中的常量引用的对象。
	d.本地方法栈中JNI本地方法的引用对象。

java方法区在Sun HotSpot虚拟机中被称为永久代，很多人认为该部分的内存是不用回收的，java虚拟机规范也没有对该部分内存的垃圾收集做规定，但是方法区中的废弃常量和无用的类还是需要回收以保证永久代不会发生内存溢出。

判断废弃常量的方法：如果常量池中的某个常量没有被任何引用所引用，则该常量是废弃常量。

判断无用的类：

	(1).该类的所有实例都已经被回收，即java堆中不存在该类的实例对象。
	(2).加载该类的类加载器已经被回收。
	(3).该类所对应的java.lang.Class对象没有任何地方被引用，无法在任何地方通过反射机制访问该类的方法。

---
### GC roots
https://www.yourkit.com/docs/java/help/gc_roots.jsp

The so-called GC (Garbage Collector) roots are objects special for garbage collector. 

Garbage collector collects those objects that are not GC roots and are not accessible by references from GC roots.

There are several kinds of GC roots. 

One object can belong to more than one kind of root. 

The root kinds are:

	  ● Class - class loaded by system class loader. Such classes can never be unloaded. They can hold objects via static fields. Please note that classes loaded by custom class loaders are not roots, unless corresponding instances of java.lang.Class happen to be roots of other kind(s).
	  ● Thread - live thread
	  ● Stack Local - local variable or parameter of Java method
	  ● JNI Local - local variable or parameter of JNI method
	  ● JNI Global - global JNI reference
	  ● Monitor Used - objects used as a monitor for synchronization
	  ● Held by JVM - objects held from garbage collection by JVM for its purposes. Actually the list of such objects depends on JVM implementation. Possible known cases are: the system class loader, a few important exception classes which the JVM knows about, a few pre-allocated objects for exception handling, and custom class loaders when they are in the process of loading classes. Unfortunately, JVM provides absolutely no additional detail for such objects. Thus it is up to the analyst to decide to which case a certain "Held by JVM" belongs.