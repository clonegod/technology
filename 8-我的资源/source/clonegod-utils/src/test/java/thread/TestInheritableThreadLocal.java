package thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import clonegod.uitls.ThreadUtils;

public class TestInheritableThreadLocal {
	
	/**
	 * InheritableThreadLocal 与 线程池
	 * 
	 */
	static ThreadLocal<Span> threadLocal = new ThreadLocal<Span>();
	
	static ThreadLocal<Span> inheritaleThreadLocal = new InheritableThreadLocal<Span>();
	
	static class Span {
		public String name;
		public Span(String name) {
			this.name = name;
		}
		public String toString() {
			return name;
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		
		threadLocal.set(new Span("NULL"));
		inheritaleThreadLocal.set(new Span("Alice"));
		
		Runnable r = () -> {
			System.out.println(ThreadUtils.currentThreadName() + ": threadLocal : " + threadLocal.get()); // null - threadLocal中的值不具有传递性
			System.out.println(ThreadUtils.currentThreadName() + ": inheritaleThreadLocal : " + inheritaleThreadLocal.get());
			inheritaleThreadLocal.set(new Span("Bob")); // 更新值
		};
		
		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.execute(r);
		ThreadUtils.sleep(ThreadLocalRandom.current().nextInt(100, 200)); // thread1 执行任务的线程更新了inheritaleThreadLocal，将Alice更新为了Bob
		
		// thread1再次执行时，将打印修改后的值Bob
		// 而另一个线程thread2（新线程）仍然打印inheritaleThreadLocal中初始设置的值
		executor.execute(r);
		executor.execute(r);
		
		executor.awaitTermination(1, TimeUnit.SECONDS);
		System.out.println(ThreadUtils.currentThreadName() + ": threadLocal : " + threadLocal.get()); // null
		System.out.println(ThreadUtils.currentThreadName() + ": inheritaleThreadLocal : " + inheritaleThreadLocal.get());
		executor.shutdownNow();
	}
}
