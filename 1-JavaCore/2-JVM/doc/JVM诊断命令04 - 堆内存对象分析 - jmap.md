## jmap - 输出堆内存信息

Memory Map for Java - Prints shared object memory maps or heap memory details of a given process or core file or a remote debug server.

[参考文档](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jmap.html)

---
注意：执行jmap命令可能会导致正在运行的JVM产生停顿？！

![](img/cmd-jmap1.png)


## -heap 选项,打印堆内存的分配情况
![](img/cmd-jmap2.png)

新生代(Eden+From+To)、老年代（CMS）、永久区、字符串常量池
![](img/cmd-jmap3.png)


## -histo 选项，导出堆内存的直方图
![](img/cmd-jmap4.png)
![](img/cmd-jmap5.png)

## -dump 选项，指定导出格式、保存路径。
![](img/cmd-jmap6.png)
![](img/cmd-jmap7.png)

## -permstat 选项，打印永久区信息。
![](img/cmd-jmap8.png)
