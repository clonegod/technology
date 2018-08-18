# Lesson18 | JUC - 线程池 

线程是不能够重复启动的，创建或销毁线程存在一定的开销，所以利用线程池技术来提高系统资源利用效率，并简化线程管理，已经是非常成熟的选择。

在现实应用中，理解应用与线程池的交互和线程池的内部工作过程，你可以参考下图。
![](img/threadpool.png)	

## Java 并发类库提供的线程池有哪几种？ 分别有什么特点？ 
Executors 创建线程池的静态工厂方法

	Executors 目前提供了 5 种不同的线程池创建配置：
	
	1、newCachedThreadPool()
		它是一种用来处理大量短时间工作任务的线程池，具有几个鲜明特点：
		它会试图缓存线程并重用，当无缓存线程可用时，就会创建新的工作线程；
		如果线程闲置的时间超过 60 秒，则被终止并移出缓存；
		长时间闲置时，这种线程池，不会消耗什么资源。
		其内部使用 SynchronousQueue 作为工作队列。

	2、newFixedThreadPool(int nThreads)
		重用指定数目（nThreads）的线程，其背后使用的是无界的工作队列，
		任何时候最多有 nThreads 个工作线程是活动的。
		这意味着，如果任务数量超过了活动队列数目，将在工作队列中等待空闲线程出现；
		如果有工作线程退出，将会有新的工作线程被创建，以补足指定的数目 nThreads。

	3、newSingleThreadExecutor()
		它的特点在于工作线程数目被限制为 1，操作一个无界的工作队列，
		所以它保证了所有任务的都是被顺序执行，最多会有一个任务处于活动状态，
		并且不允许使用者改动线程池实例，因此可以避免其改变线程数目。

	4、newSingleThreadScheduledExecutor() 、 newScheduledThreadPool(int corePoolSize)
		创建的是个 ScheduledExecutorService，可以进行定时或周期性的工作调度，
		区别在于单一工作线程还是多个工作线程。

	5、newWorkStealingPool(int parallelism)
		这是一个经常被人忽略的线程池，Java 8 才加入这个创建方法，
		其内部会构建ForkJoinPool，利用Work-Stealing算法，并行地处理任务，不保证处理顺序。
		针对现代CPU多核的特点而设计！
	
## [ForkJoinPool](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/ForkJoinPool.html)
## [Work-Stealing](https://en.wikipedia.org/wiki/Work_stealing)

---
## Executor & ExecutorService
Executor 是一个基础的接口，其初衷是将任务提交和任务执行细节解耦，这一点可以体会其定义的唯一方法。
	void execute(Runnable command);

![Executors](img/executors.png)


ExecutorService 除了通常意义上“池”的功能，还提供了更全面的线程管理、任务提交等方法。


ExecutorService 不仅提供 service 的管理功能，比如 shutdown 等方法，

也提供了更加全面的提交任务机制，如返回Future而不是 void 的 submit 方法。

	<T> Future<T> submit(Callable<T> task);
注意，这个例子输入的可是Callable，它解决了 Runnable 无法返回结果的困扰。



## ThreadPoolExecutor构造函数
Java 标准类库提供了几种基础实现，比如ThreadPoolExecutor、ScheduledThreadPoolExecutor、ForkJoinPool。

这些线程池的设计特点在于其高度的可调节性和灵活性，以尽量满足复杂多变的实际应用场景。

Executors 则从简化使用的角度，为我们提供了各种方便的静态工厂方法。

	【工作队列】
	private final BlockingQueue<Runnable> workQueue;

	工作队列负责存储用户提交的各个任务，工作队列可以是容量为 0 的 SynchronousQueue（newCachedThreadPool），
	也可以是像固定大小线程池（newFixedThreadPool）那样使用 LinkedBlockingQueue。

	【工作线程】
	private final HashSet<Worker> workers = new HashSet<>();
	线程池的工作线程被抽象为静态内部类 Worker，基于AQS实现。
	内部的“线程池”，这是指保持工作线程的集合，线程池需要在运行过程中管理线程创建、销毁。
	例如，对于带缓存的线程池，当任务压力较大时，线程池会创建新的工作线程；
	当业务压力退去，线程池会在闲置一段时间（默认 60 秒）后结束线程。	

	【ThreadFactory】
	ThreadFactory 提供上面所需要的创建线程逻辑。

从上面的分析，就可以看出线程池的几个基本组成部分，一起都体现在线程池的构造函数中：

	【corePoolSize】
	核心线程数，可以大致理解为长期驻留的线程数目（除非设置了 allowCoreThreadTimeOut）。
	对于不同的线程池，这个值可能会有很大区别，比如 newFixedThreadPool 会将其设置为 nThreads，而对于 newCachedThreadPool 则是为 0。

	【maximumPoolSize】
	在线程不够时能够创建的最大线程数。
	同样进行对比，对于 newFixedThreadPool，当然就是 nThreads，因为其要求是固定大小，而 newCachedThreadPool 则是 Integer.MAX_VALUE。
	>>> 注意：只有当线程池中的队列满了，才会创建非核心线程！！！
	
	【keepAlive】
	keepAliveTime 和 TimeUnit，这两个参数指定了额外的线程能够闲置多久，显然有些线程池不需要它。

	【workQueue】
	工作队列，必须是 BlockingQueue。

#
	public ThreadPoolExecutor(int corePoolSize,
                      	int maximumPoolSize,
                      	long keepAliveTime,
                      	TimeUnit unit,
                      	BlockingQueue<Runnable> workQueue,
                      	ThreadFactory threadFactory,
                      	RejectedExecutionHandler handler)

## SingleThreadPool 的特殊之处
	SingleThreadPool使用FinalizableDelegatedExecutorService进行了包装。
	FinalizableDelegatedExecutorService 中重写了finalize()，在finalize()中调用了关闭线程池的操作！
	因此，即使在方法内部通过newSingleThreadPool创建线程池，在方法结束时，这个线程池将被合理的关闭。

	而其它类型的线程池，都必须手动调用shutdown/shutdownNow才能关闭。

---

## 线程池的实践经验
	1、任务队列有界，防止大量任务堆积；
	2、选择合适的任务拒绝策略，或者自定义拒绝策略；
	3、自定义线程工厂，给线程取一个有业务含义的名称，便于日志排查；
	4、避免ThreadLocal与ThreadPool一起使用；
	5、计算密集型的线程池中，线程个数建议为CPU个数-1；
	6、IO密集型的线程池中，线程个数可以适当增大，以提高并发处理能力；
	7、提交到线程池中的任务要捕获异常
		如果Runnable/Callable直接抛出异常，每次都会创建线程，也就等于线程池没有发挥作用，
		如果大并发下一直创建线程可能会导致JVM挂掉。

## 线程池使用注意点
	1、避免任务堆积。
	前面我说过 newFixedThreadPool 是创建指定数目的线程，但是其工作队列是无界的，
	如果工作线程数目太少，导致处理跟不上入队的速度，这就很有可能占用大量系统内存，甚至是出现 OOM。
	诊断时，你可以使用 jmap 之类的工具，查看是否有大量的任务对象入队。
	
	2、避免过度设置线程数。
	我们通常在处理大量短时任务时，使用缓存的线程池，可实现线程个数的自动伸缩。
	比如在最新的 HTTP/2 client API 中，目前的默认实现就是如此。
	我们在创建线程池的时候，并不能准确预计任务压力有多大、数据特征是什么样子（大部分请求是 1K 、100K 还是 1M 以上？），所以很难明确设定一个线程数目。

	3、警惕线程泄露。	
	如果线程数目不断增长（可以使用 jstack 等工具检查），也需要警惕另外一种可能性，就是线程泄漏，这种情况往往是因为任务逻辑有问题，导致工作线程迟迟不能被释放。
	建议你排查下线程栈，很有可能多个线程都是卡在近似的代码处。

	4、避免使用 ThreadLocal
	尽量避免在使用线程池时操作 ThreadLocal，因为工作线程的生命周期通常都会超过任务的生命周期。

## 影响性能的因素
	CPU核数
	文件句柄上限
	内存大小
	IO层面：磁盘读写速度、网络IO是否采用异步非阻塞模式
	线程池大小