package juc;

import java.util.concurrent.Semaphore;

/**
 * 通过控制一定数量的允许（permit）的方式，来达到限制通用资源访问的目的。
 * 
 * 你可以想象一下这个场景，
 * 	在车站、机场等出租车时，当很多空出租车就位时，为防止过度拥挤，调度员指挥排队等待坐车的队伍一次进来 5 个人上车，等这 5 个人坐车出发，再放进去下一批，这和 Semaphore 的工作原理有些类似。
 *	
 *	但是，从具体节奏来看，其实并不符合我们前面场景的需求，因为本例中 Semaphore 的用法实际是保证，一直有 5 个人可以试图乘车，如果有 1 个人出发了，立即就有排队的人获得许可，而这并不完全符合我们前面的要求。
 */
public class UsualSemaphoreSample {
	public static void main(String[] args) throws InterruptedException {
		System.out.println("Action...GO!");
		
		// 只有5个可用的信号量
		Semaphore semaphore = new Semaphore(5);
		
		for (int i = 0; i < 10; i++) {
			Thread t = new Thread(new SemaphoreWorker(semaphore));
			t.start();
		}
	}
}

class SemaphoreWorker implements Runnable {
	private String name;
	private Semaphore semaphore;

	public SemaphoreWorker(Semaphore semaphore) {
		this.semaphore = semaphore;
	}

	@Override
	public void run() {
		try {
			log("is waiting for a permit!");
			semaphore.acquire(); // 消费1个permit
			log("acquired a permit!");
			log("executed!");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			log("released a permit!");
			semaphore.release(); // 释放1个permit
		}
	}

	private void log(String msg) {
		if (name == null) {
			name = Thread.currentThread().getName();
		}
		System.out.println(name + " " + msg);
	}
}