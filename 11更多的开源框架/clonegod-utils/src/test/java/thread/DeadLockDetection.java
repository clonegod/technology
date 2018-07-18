package thread;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 如果我们是开发自己的管理工具，需要用更加程序化的方式扫描服务进程、定位死锁，可以考虑使用 Java 提供的标准管理 API，
 * ThreadMXBean，其直接就提供了 findDeadlockedThreads​() 方法用于定位。
 * 
 * 在实际应用中，就可以据此收集进一步的信息，然后进行预警等后续处理。
 * 但是要注意的是，对线程进行快照本身是一个相对重量级的操作，还是要慎重选择频度和时机。
 * 
 */
public class DeadLockDetection {
	
	private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	public static void checkDeadLock() {
		ThreadMXBean mbean = ManagementFactory.getThreadMXBean();
		
		Runnable dlCheck = () -> {
			long[] threadIds = mbean.findDeadlockedThreads();
			if(threadIds != null) {
				ThreadInfo[] threadInfos = mbean.getThreadInfo(threadIds);
				System.out.println("Detected deadlock threads:");
				for(ThreadInfo threadInfo : threadInfos) {
					System.out.println(threadInfo.getThreadName() + ": threadId=" + threadInfo.getThreadId());
				}
			}
		};
		
		// 延迟5秒，然后每10秒进行一次死锁扫描
		scheduler.scheduleWithFixedDelay(dlCheck, 5L, 10L, TimeUnit.SECONDS);
	}
	
	public static void stopCheck() {
		scheduler.shutdownNow();
	}
	
}
