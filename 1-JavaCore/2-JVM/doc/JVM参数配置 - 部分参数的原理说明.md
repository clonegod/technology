## -XX:+PrintCommandLineFlags
Enables the printing of ergonomically selected HotSpot VM settings based on the set of command line options explicitly specified. 

打印命令行传入的JVM参数

Useful when wanting to know the ergonomic values set by the HotSpot VM such as JVM heap space sizes and garbage collector selected.
 
同时也会打印JVM自动设置的堆内存大小、选择的垃圾回收器等。

## -XX:+PrintFlagsFinal
Enables the printing of all production HotSpot VM command line option names and their corresponding values as they are set by the HotSpot VM based on the command line options explicitly specified and HotSpot VM defaults for options not specified. Introduced in Java 6 Update 19.

Useful when wanting to know the configuration of HotSpot VM options in use by a Java application. 

In contrast to -XX:+PrintCommandLineFlags, -XX:+PrintFlagsFinal prints all HotSpot VM options and their corresponding values as set by the HotSpot VM, not just those that are ergonomically set.

打印命令行传入的参数、JVM自动设置的参数、JVM其它相关参数在运行时所使用的默认值。


## -XX:+HandlePromotionFailure
关闭新生代收集担保。

The youngest generation collection does not require a guarantee of full promotion of all live objects. 



###### 什么是新生代收集担保？ 
在一次理想化的minor gc中，Eden和First Survivor中的活跃对象会被复制到Second Survivor。 
然而，Second Survivor不一定能容纳下所有从E和F区copy过来的活跃对象。
为了确保minor gc能够顺利完成，GC需要在年老代中额外保留一块足以容纳所有活跃对象的内存空间。 
这个预留操作，就被称之为新生代收集担保（New Generation Guarantee）。如果预留操作无法完成时，仍会触发major gc(full gc)。 

###### 为什么要关闭新生代收集担保？ 
因为在年老代中预留的空间大小，是无法精确计算的。
为了确保极端情况的发生，GC参考了最坏情况下的新生代内存占用，即Eden+First Survivor。
这种策略无疑是在浪费年老代内存，从时序角度看，还会提前触发Full GC。
为了避免如上情况的发生，JVM允许开发者手动关闭新生代收集担保。

在开启本选项后，minor gc将不再提供新生代收集担保，而是在出现survior或年老代不够用时，抛出promotion failed异常。


## -Djava.awt.headless=true

Headless模式是系统的一种配置模式。在该模式下，系统缺少了显示设备、键盘或鼠标。

Headless模式虽然不是我们愿意见到的，但事实上我们却常常需要在该模式下工作，尤其是服务器端程序开发者。

因为服务器（如提供Web服务的主机）往往可能缺少前述设备，但又需要使用他们提供的功能，生成相应的数据，以提供给客户端（如浏览器所在的配有相关的显示设备、键盘和鼠标的主机）。

一般是在程序开始激活headless模式，告诉程序，现在你要工作在Headless mode下，就不要指望硬件帮忙了，你得自力更生，依靠系统的计算能力模拟出这些特性来 。

在Java服务器程序需要进行部分图像处理功能时，建议将程序运行模式设置为headless，这样有助于服务器端有效控制程序运行状态和内存使用（可防止在处理大图片时发生内存溢出） 。

## -XX:-DisableExplicitGC
默认不启用
禁止在运行期显式地调用 System.gc()。
开启该选项后，GC的触发时机将由Garbage Collector全权掌控。 
注意：你熟悉的代码里没调用System.gc()，不代表你依赖的框架工具没在使用。
例如RMI就在多数用户毫不知情的情况下，显示地调用GC来防止自身OOM。

