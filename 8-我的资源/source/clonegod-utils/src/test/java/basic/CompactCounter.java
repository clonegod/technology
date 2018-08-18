package basic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.stream.IntStream;

/**
 * 利用原始数据类型，实现一个线程安全的计数器。
 * 使用原子数据类型，而不是包装类型，可以进一步提高性能。（包装类型属于对象，对象占用的内存空间比原始类型大很多，比如对象头）
 */
public class CompactCounter {
    private volatile long counter; // 原始数据类型
    
    private static final AtomicLongFieldUpdater<CompactCounter> updater = 
    			AtomicLongFieldUpdater.newUpdater(CompactCounter.class, "counter");
    
    public void increase() {
        updater.incrementAndGet(this);
    }
    
    public static void main(String[] args) throws Exception {
		final CompactCounter counter = new CompactCounter();
		
		ExecutorService executor = Executors.newFixedThreadPool(100);
		IntStream.rangeClosed(1, 1000)
				.forEach(n -> {
					executor.execute(() -> {
						counter.increase();
					});
				});
		
		executor.shutdown();
		executor.awaitTermination(3, TimeUnit.SECONDS);
		
		System.out.println(counter.counter);
		
	}
}

/**
 * 
 * 一个常见的线程安全计数器实现。
 */
class Counter {
    private final AtomicLong counter = new AtomicLong(); // 利用CAS保证线程安全的原子操作
    public void increase() {
        counter.incrementAndGet();
    }
}


