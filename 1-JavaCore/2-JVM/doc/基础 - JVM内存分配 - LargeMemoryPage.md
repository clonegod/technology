## TLB - Memory address translations

Large pages enable applications to establish large memory regions.
 
Memory address translations use translation lookaside buffers (TLB) inside the CPU. 

TLB is a cache that memory management hardware uses for speeding up virtual address translations.

TLB 缓存了虚拟地址到物理地址的映射关系，可以提高内存访问速度，适用于内存密集类型的应用系统。

The use of large pages can significantly reduce TLB misses, improving the performance of most workloads, especially those with large active memory working sets.


## How Java Support for Large Memory Pages

#### -XX:+UseLargePages
Beginning with Java SE 5.0 there is a cross-platform flag for requesting large memory pages: -XX:+UseLargePages (on by default for Solaris, off by default for Windows and Linux). 

The goal of large page support is to optimize processor Translation-Lookaside Buffers.

A Translation-Lookaside Buffer (TLB) is a page translation cache（页面翻译缓存） that holds the most-recently used virtual-to-physical address translations. 

TLB is a scarce（稀缺） system resource. 

A TLB miss can be costly as the processor must then read from the hierarchical（分级/分层） page table, which may require multiple memory accesses. 

By using bigger page size, a single TLB entry can represent larger memory range.  使用更大的页面，单个TLB条目可以表示更大的内存范围。

There will be less pressure on TLB and memory-intensive applications may have better performance.  内存密集型应用可能会有更好的性能。

However please note sometimes using large page memory can negatively affect system performance.
 
For example, when a large mount of memory is pinned（固定，锁住） by an application, it may create a shortage（短缺） of regular memory and cause excessive paging in other applications and slow down the entire system. 

Also please note for a system that has been up for a long time, excessive（过多） fragmentation（片段） can make it impossible to reserve enough large page memory.

When it happens, either the OS or JVM will revert to using regular pages.

--------------------------------------------------------------------------------
### Operating system configuration changes to enable large pages:
Linux（Linux系统配置使用LargePage）:  

Large page support is included in 2.6 kernel. Some vendors have backported the code to their 2.4 based releases. To check if your system can support large page memory, try the following:   

	# cat /proc/meminfo | grep Huge 
	HugePages_Total: 0 
	HugePages_Free: 0 
	Hugepagesize: 2048 kB 
	# 

If the output shows the three "Huge" variables then your system can support large page memory, but it needs to be configured. If the command doesn't print out anything, then large page support is not available. 

To configure the system to use large page memory, one must log in as root, then:

  	1. Increase SHMMAX value. It must be larger than the Java heap size. On a system with 4 GB of physical RAM (or less) the following will make all the memory sharable: 
	# echo 4294967295 > /proc/sys/kernel/shmmax 

  	2. Specify the number of large pages. In the following example 3 GB of a 4 GB system are reserved for large pages (assuming a large page size of 2048k, then 3g = 3 x 1024m = 3072m = 3072 * 1024k = 3145728k, and 3145728k / 2048k = 1536): 
	# echo 1536 > /proc/sys/vm/nr_hugepages 

Note the /proc values will reset after reboot so you may want to set them in an init script (e.g. rc.local or sysctl.conf). 