# SIGAR (System Information Gatherer and Reporter) 
	- 收集本地cpu,内存，磁盘，网络等相关数据
	使用sigar可以在客户端收集相关系统信息，然后定时汇报给指定服务器，以便于统一监测节点的系统参数。

# 使用方式（windows为例）：
	1、下载： https://sourceforge.net/projects/sigar/
	2、windows环境下，解压，进入hyperic-sigar-1.6.4\sigar-bin\lib
		复制 sigar-amd64-winnt.dll 到 jdk/bin目录下
	3、依赖包：
		sigar.jar (1.6.4版本) 
		log4j.jar