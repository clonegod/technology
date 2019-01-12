package clonegod.spider.core.engine;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 统一管理系统中的线程池，按用途对线程池进行分类:<br/>
 * 核心类任务使用一个专用的线程池;<br/>
 * 另外设置一个通用线程池，用来执行其它任务;<br/>
 */
public class SpiderThreadPool {
	
	private static Logger logger = LoggerFactory.getLogger(SpiderThreadPool.class);

	private static final int FETCH_TASK_CORE_POOL_SIZE = 100;
	private static final int FETCH_TASK_MAX_POOL_SIZE = 500;

	private static final int COMMON_TASK_CORE_POOL_SIZE = 100;
	private static final int COMMON_TASK_MAX_POOL_SIZE = 500;

	private static final long IDEL_THREADS_KEEP_ALIVE_TIME = 120; // 空闲线程最多存活120秒即被系统收回

	private static final int BOLCK_QUEUE_SIZE = 300; // 阻塞队列大小

	/** 数据抓取线程池 */
	private static ThreadPoolExecutor FETCH_TASK_POOL = new ThreadPoolExecutor(FETCH_TASK_CORE_POOL_SIZE,
			FETCH_TASK_MAX_POOL_SIZE, IDEL_THREADS_KEEP_ALIVE_TIME, TimeUnit.SECONDS,
			new ArrayBlockingQueue<Runnable>(BOLCK_QUEUE_SIZE));
	
	/** 外部服务线程池 */
	private static ThreadPoolExecutor COMMON_TASK_POOL = new ThreadPoolExecutor(COMMON_TASK_CORE_POOL_SIZE,
			COMMON_TASK_MAX_POOL_SIZE, IDEL_THREADS_KEEP_ALIVE_TIME, TimeUnit.SECONDS,
			new ArrayBlockingQueue<Runnable>(BOLCK_QUEUE_SIZE));

	/**
	 * 专用执行系统核心任务的线程池
	 * 
	 * @param task
	 * @return
	 */
	public static <T> Future<T> submitCoreTask(Callable<T> task) {
		return FETCH_TASK_POOL.submit(task);
	}

	/**
	 * 通用任务执行的线程池
	 * 
	 * @param task
	 * @return
	 */
	public static <T> Future<T> submitCommonTask(Callable<T> task) {
		return COMMON_TASK_POOL.submit(task);
	}
	
	/**
	 * 启动有序关闭，其中先前提交的任务将被执行，但不会接受任何新任务。 如果已经关闭，调用没有额外的作用。
	 * 此方法不等待以前提交的任务完成执行。 使用等待终止来做到这一点。
	 */
	public static void shutdown() {
		FETCH_TASK_POOL.shutdown();
		COMMON_TASK_POOL.shutdown();
	}
	
	/**
	 * 尝试停止所有主动执行的任务，停止等待任务的处理，并返回正在等待执行的任务列表。
	 * 此方法不等待主动执行的任务终止。 使用等待终止来做到这一点。
	 * 除了努力尝试停止处理积极执行任务之外，没有任何保证。 
	 * 例如，典型的实现将通过Thread.interrupt取消，所以任何不能响应中断的任务都可能永远不会终止。
	 */
	public static void shutdownNow() {
		FETCH_TASK_POOL.shutdownNow();
		COMMON_TASK_POOL.shutdownNow();
	}
	
	/**
	 * 1. 阻塞直到所有任务执行完成---关闭线程池
	 * 2. 或者发生等待超时---关闭线程池
	 * 3. 或者当前线程被中断---
	 * 以三者先发生的为准。
	 * 
	 * @param timeout
	 * @param unit
	 * @throws InterruptedException 
	 */
	public static void awaitTermination(long timeout, TimeUnit unit) {
		try {
			FETCH_TASK_POOL.awaitTermination(timeout, unit);
			COMMON_TASK_POOL.awaitTermination(timeout, unit);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static int getUnCompleteTaskCount() {
		logger.info("FETCH_TASK_POOL: activeCount={}, queueSize={}", FETCH_TASK_POOL.getActiveCount(), FETCH_TASK_POOL.getQueue().size());
		logger.info("COMMON_TASK_POOL: activeCount={}, queueSize={}", COMMON_TASK_POOL.getActiveCount(), COMMON_TASK_POOL.getQueue().size());
		
		int total = FETCH_TASK_POOL.getActiveCount() + FETCH_TASK_POOL.getQueue().size() 
				+ COMMON_TASK_POOL.getActiveCount() + COMMON_TASK_POOL.getQueue().size();
		return  total; 
	}
}
