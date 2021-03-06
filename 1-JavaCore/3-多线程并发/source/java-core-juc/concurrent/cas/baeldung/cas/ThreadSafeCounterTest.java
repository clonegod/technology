package baeldung.cas;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.junit.Test;

public class ThreadSafeCounterTest {

	/**
	 * 线程安全的Counter---加锁 synchronized
	 */
    @Test
    public void givenMultiThread_whenSafeCounterWithLockIncrement() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(3);
        SafeCounterWithLock safeCounter = new SafeCounterWithLock();

        IntStream.range(0, 1000)
          .forEach(count -> service.submit(safeCounter::increment));
        service.awaitTermination(100, TimeUnit.MILLISECONDS);

        assertEquals(1000, safeCounter.getValue());
    }
    
    /**
     * 线程安全的Counter---AtomicInteger 原子操作CAS提供保障
     */
    @Test
    public void givenMultiThread_whenSafeCounterWithoutLockIncrement() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(3);
        SafeCounterWithoutLock safeCounter = new SafeCounterWithoutLock();

        IntStream.range(0, 1000)
          .forEach(count -> service.submit(safeCounter::increment));
        service.awaitTermination(100, TimeUnit.MILLISECONDS);

        assertEquals(1000, safeCounter.getValue());
    }
    
}
