package baeldung.queue.BlockingQueue;

/**
应用场景：
	DelayQueue是一个无界阻塞队列，只有在延迟期满时才能从中提取元素。该队列的头部是延迟期满后保存时间最长的Delayed 元素。
	DelayQueue阻塞队列在我们系统开发中也常常会用到，例如：
	缓存系统的设计，缓存中的对象，超过了空闲时间，需要从缓存中移出；
	任务调度系统，能够准确的把握任务的执行时间。
	我们可能需要通过线程处理很多时间上要求很严格的数据，如果使用普通的线程，我们就需要遍历所有的对象，一个一个的检查看数据是否过期等，
	首先这样在执行上的效率不会太高，其次就是这种设计的风格也大大的影响了数据的精度。
	一个需要12:00点执行的任务可能12:01才执行,这样对数据要求很高的系统有更大的弊端。由此我们可以使用DelayQueue。
 *
 */
