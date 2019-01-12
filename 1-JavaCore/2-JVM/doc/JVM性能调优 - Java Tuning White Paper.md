#[Java Tuning White Paper（with tuning examples）](http://www.oracle.com/technetwork/java/tuning-139912.html#section4.2.5)


## Tuning Ideas	 调优思路

#### 4.1.1   Be Aware of Ergonomics Settings  - JVM默认的参数配置

Before you start to tune the command line arguments for Java be aware that Sun's HotSpot™ Java Virtual Machine has incorporated（融合） technology to begin to tune itself. 

This smart tuning is referred to as Ergonomics. 

Most computers that have at least 2 CPU's and at least 2 GB of physical memory are considered server-class machines which means that by default the settings are:
	
	  ● The -server compiler
 	  ● The -XX:+UseParallelGC parallel (throughput) garbage collector
  	  ● The -Xms initial heap size is 1/64th of the machine's physical memory
 	  ● The -Xmx maximum heap size is 1/4th of the machine's physical memory (up to 1 GB max).	


Please note that 32-bit Windows systems all use the -client compiler by default and 64-bit Windows systems which meet the criteria above will be be treated as server-class machines.



---

#### 4.1.2   Heap Sizing  自定义堆内存大小
Even though Ergonomics significantly improves the "out of the box" experience for many applications, optimal tuning often requires more attention to the sizing of the Java memory regions.

The maximum heap size of a Java application is limited by three factors: 

	the process data model (32-bit or 64-bit) and the associated operating system limitations, 
	the amount of virtual memory available on the system, and
	the amount of physical memory available on the system.  

The size of the Java heap for a particular application can never exceed or even reach the maximum virtual address space of the process data model. 

For a 32-bit process model, the maximum virtual address size of the process is typically 4 GB, though some operating systems limit this to 2 GB or 3 GB. 

The maximum heap size is typically -Xmx3800m (1600m for 2 GB limits), though the actual limitation is application dependent. 
For 64-bit process models, the maximum is essentially unlimited. 

JVM 堆的内存不能设置为物理内存的大小，要为操作系统、系统其它进程，以及JVM的其它操作预留一部分内存空间。

For a single Java application on a dedicated system, `the size of the Java heap should never be set to the amount of physical RAM on the system, as additional RAM is needed for the operating system, other system processes, and even for other JVM operations`. 

Committing too much of a system's physical memory is likely to result in paging of virtual memory to disk, quite likely during garbage collection operations, leading to significant performance issues. 

On systems with multiple Java processes, or multiple processes in general, the sum of the Java heaps for those processes should also not exceed the the size of the physical RAM in the system. 

The next most important Java memory tunable is the size of if the young generation (also known as the NewSize). 

#### Generally speaking the largest recommended value for the young generation is 3/8 of the maximum heap size. 

#### Note that with the throughput and low pause time collectors it may be possible to exceed this ratio.



---
#### 4.1.3   Garbage Collector Policy	垃圾回收器的选择

The Java™ Platform offers a choice of Garbage Collection algorithms.
 
    ● The -XX:+UseParallelGC parallel (throughput) garbage collector, or
    ● The -XX:+UseConcMarkSweepGC concurrent (low pause time) garbage collector (also known as CMS)
    ● The -XX:+UseSerialGC serial garbage collector (for smaller applications and systems)

---
#### 4.1.4   Other Tuning Parameters	其它调优参数 (UseLargePages)

The VM Options page discusses Java support for `Large Memory Pages`. 

By appropriately configuring the operating system and then using the command line options `-XX:+UseLargePages` ( only default for Solaris) and `-XX:LargePageSizeInBytes` you can get the best efficiency out of the memory management system of your server. 

	-XX:LargePageSizeInBytes=128m
	-XX:+UseLargePages

Note that with larger page sizes we can make better use of virtual memory hardware resources (TLBs), but that may cause larger space sizes for the Permanent Generation and the Code Cache, which in turn can force you to reduce the size of your Java heap. 



This is a small concern with 2 MB or 4 MB page sizes but a more interesting concern with 256 MB page sizes. 

> Why Use Large Memory Page ?

Large pages enable applications to establish large memory regions. Memory address translations use translation lookaside buffers (TLB) inside the CPU. TLB is a cache that memory management hardware uses for speeding up virtual address translations.

在操作系统级别上，针对进程的虚拟内存空间，使用大内存页，避免频繁的进行页换入换出，出现系统抖动。 TLB是内存管理硬件用于加速虚拟地址转换的缓存。

---


## 4.2   Tuning Examples 调优示例


Here are some specific tuning examples for your experimentation. 

Please understand that these are only examples and that the optimal heap sizes and tuning parameters for your application on your hardware may differ. 


#### 4.2.1   Tuning Example 1: Tuning for Throughput

Here is an example of specific command line tuning for a server application running on system with 4 GB of memory and capable of running 32 threads simultaneously (CPU's and cores or contexts). 

	java -Xmx3800m -Xms3800m -Xmn2g -Xss128k -XX:+UseParallelGC -XX:ParallelGCThreads=20 

Comments:

	  ● -Xmx3800m -Xms3800m 
	Configures a large Java heap to take advantage of the large memory system.
	  ● -Xmn2g 
	Configures a large heap for the young generation (which can be collected in parallel), again taking advantage of the large memory system. It helps prevent short lived objects from being prematurely promoted to the old generation, where garbage collection is more expensive.
	  ● -Xss128k 
	Reduces the default maximum thread stack size, which allows more of the process' virtual memory address space to be used by the Java heap.
	  ● -XX:+UseParallelGC 
	Selects the parallel garbage collector for the new generation of the Java heap (note: this is generally the default on server-class machines)
	  ● -XX:ParallelGCThreads=20 
	Reduces the number of garbage collection threads. The default would be equal to the processor count, which would probably be unnecessarily high on a 32 thread capable system.


#### 4.2.2   Tuning Example 2
Try the parallel old generation collector
Similar to example 1 we here want to test the impact of the parallel old generation collector. 
	
	java -Xmx3550m -Xms3550m -Xmn2g -Xss128k -XX:+UseParallelGC -
	XX:ParallelGCThreads=20 -XX:+UseParallelOldGC 

Comments:

	  ● -Xmx3550m -Xms3550m 
	Sizes have been reduced. The ParallelOldGC collector has additional native, non-Java heap memory requirements and so the Java heap sizes may need to be reduced when running a 32-bit JVM.
	  ● -XX:+UseParallelOldGC 
	Use the parallel old generation collector. Certain phases of an old generation collection can be performed in parallel, speeding up a old generation collection.



#### 4.2.3   Tuning Example 3: Try 256 MB pages

This tuning example is specific to those Solaris-based systems that would support the huge page size of 256 MB. 

	java -Xmx2506m -Xms2506m -Xmn1536m -Xss128k -XX:+UseParallelGC -XX:ParallelGCThreads=20 -XX:+UseParallelOldGC -XX:LargePageSizeInBytes=256m 

Comments:

	  ● -Xmx2506m -Xms2506m 
	Sizes have been reduced because using the large page setting causes the permanent generation and code caches sizes to be 256 MB and this reduces memory available for the Java heap.
	  ● -Xmn1536m 
	The young generation heap is often sized as a fraction of the overall Java heap size. Typically we suggest you start tuning with a young generation size of 1/4th the overall heap size. The young generation was reduced in this case to maintain a similar ratio between young generation and old generation sizing used in the previous example option used.
	  ● -XX:LargePageSizeInBytes=256m 
	Causes the Java heap, including the permanent generation, and the compiled code cache to use as a minimum size one 256 MB page (for those platforms which support it).


#### 4.2.4   Tuning Example 4: Try -XX:+AggressiveOpts

This tuning example is similar to Example 2, but adds the AggressiveOpts option（JIT Compiler）. 

	java -Xmx3550m -Xms3550m -Xmn2g -Xss128k -XX:+UseParallelGC -XX:ParallelGCThreads=20 -XX:+UseParallelOldGC -XX:+AggressiveOpts 

Comments:

	  ● -Xmx3550m -Xms3550m 
	Sizes have been increased back to the level of Example 2 since we no longer using huge pages.
	  ● -Xmn2g 
	Sizes have been increased back to the level of Example 2 since we no longer using huge pages.
	  ● -XX:+AggressiveOpts 
	Turns on point performance optimizations that are expected to be on by default in upcoming releases. The changes grouped by this flag are minor changes to JVM runtime compiled code and not distinct performance features (such as BiasedLocking and ParallelOldGC). This is a good flag to try the JVM engineering team's latest performance tweaks for upcoming releases. 
	Note: this option is experimental! The specific optimizations enabled by this option can change from release to release and even build to build. You should reevaluate the effects of this option with prior to deploying a new release of Java.


#### 4.2.5   Tuning Example 5: Try Biased Locking

This tuning example is builds on Example 4, and adds the Biased Locking option. 

	java -Xmx3550m -Xms3550m -Xmn2g -Xss128k -XX:+UseParallelGC -XX:ParallelGCThreads=20 -XX:+UseParallelOldGC -XX:+AggressiveOpts -XX:+UseBiasedLocking 

Comments:

	  ● -XX:+UseBiasedLocking 
Enables a technique for improving the performance of uncontended synchronization. 


An object is "biased" toward the thread which first acquires its monitor via a monitorenter bytecode or synchronized method invocation; 
subsequent monitor-related operations performed by that thread are relatively much faster on multiprocessor machines. 

Some applications with significant amounts of uncontended synchronization may attain significant speedups with this flag enabled; 
some applications with certain patterns of locking may see slowdowns, though attempts have been made to minimize the negative impact.


#### 4.2.6   Tuning Example 6: Tuning for low pause times and high throughput

This tuning example similar to Example 2, but uses the concurrent garbage collector (instead of the parallel throughput collector). 

	java -Xmx3550m -Xms3550m -Xmn2g -Xss128k -XX:ParallelGCThreads=20 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:SurvivorRatio=8 -XX:TargetSurvivorRatio=90 -XX:MaxTenuringThreshold=31 


Comments:

	  ● -XX:+UseConcMarkSweepGC -XX:+UseParNewGC 
	Selects the Concurrent Mark Sweep collector. This collector may deliver better response time properties for the application (i.e., low application pause time). It is a parallel and mostly-concurrent collector and and can be a good match for the threading ability of an large multi-processor systems.
	  ● -XX:SurvivorRatio=8 
	Sets survivor space ratio to 1:8, resulting in larger survivor spaces (the smaller the ratio, the larger the space). Larger survivor spaces allow short lived objects a longer time period to die in the young generation.
	  ● -XX:TargetSurvivorRatio=90 
	Allows 90% of the survivor spaces to be occupied instead of the default 50%, allowing better utilization of the survivor space memory.
	  ● -XX:MaxTenuringThreshold=31 
	Allows short lived objects a longer time period to die in the young generation (and hence, avoid promotion). 
	A consequence of this setting is that minor GC times can increase due to additional objects to copy. 
	This value and survivor space sizes may need to be adjusted so as to balance overheads of copying between survivor spaces versus tenuring objects that are going to live for a long time. 
	The default settings for CMS are SurvivorRatio=1024 and MaxTenuringThreshold=0 which cause all survivors of a scavenge to be promoted. 
	This can place a lot of pressure on the single concurrent thread collecting the tenured generation. 
	Note: when used with -XX:+UseBiasedLocking, this setting should be 15.


#### 4.2.7   Tuning Example 7: Try AggressiveOpts for low pause times and high throughput

This tuning example is builds on Example 6, and adds the AggressiveOpts option. 

	java -Xmx3550m -Xms3550m -Xmn2g -Xss128k -XX:ParallelGCThreads=20 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:SurvivorRatio=8 -XX:TargetSurvivorRatio=90 -XX:MaxTenuringThreshold=31 -XX:+AggressiveOpts 

Comments:

 	 ● -XX:+AggressiveOpts 


---

## 5   Monitoring and Profiling - 监控和分析

Discussing monitoring (extracting high level statistics from a running application) or profiling (instrumenting an application to provide detailed performance statistics) are subjects which are worthy of White Papers in their own right. For the purpose of this Java Tuning White Paper these subjects will be introduced using tools as examples which can be used on a permanent basis without charge. 

##### 5.1   Monitoring
The Java™ Platform comes with a great deal of monitoring facilities built-in. Please see the document Monitoring and Management for the Java™ Platform for more information. 
The most popular of these "built-in" tools are `JConsole` and the `jvmstat ` technologies. 

##### 5.2   Profiling
The Java™ Platform also includes some profiling facilities. 
The most popular of these "built-in" profiling tools are The `-Xprof Profiler` and the `HPROF profiler` (for use with HPROF see also Heap Analysis Tool). 


---

## 6   Coding for Performance	编写高优化性能

This section will cover coding level changes that you can make which will make an impact on performance. 

For the purpose of this initial draft of the Java Tuning White Paper examples of the kinds of coding level changes that can have an impact on performance are taking advantage of new language features like `NIO` and the `Concurrency utilities`.

#### New I/O API
The New I/O API's (or NIO) offer improved performance for operations like memory mapped files and scalable network operations. 

By using NIO developers may be able to significantly improve performance of memory or network intensive applications. 

One of the success stories of using NIO is in the Grizzly web container which is part of Sun's GlassFish project.

#### Concurrency Utilities
Another example new Java language features that impact performance is the set of Concurrency Utilities. Increasingly server applications are going to be targeting platforms with multiple CPU's and multiple cores per CPU. In order to best take advantage of these systems applications must be designed with multi-threading in mind. Classical multi-threaded programming is very complex and error prone due to subtleties in thread interactions such as race conditions. Now with the Concurrency Utilities developers finally have a solid set of building blocks upon which to build scalable multi-threaded applications while avoiding much of the complexity of writing a multi-threaded framework. 



 