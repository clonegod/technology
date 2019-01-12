package juc;

import java.util.concurrent.Semaphore;

/**
 * 演示个非典型的 Semaphore 用法。
 * 
 * 一次释放N个permit，待N个permit都被使用后，再重新释放一批新的permit。
 *
 */
public class AbnormalSemaphoreSample {
	public static void main(String[] args) throws InterruptedException {
		// 初始permit数量为0
		Semaphore semaphore = new Semaphore(0);
		for (int i = 0; i < 10; i++) {
			Thread t = new Thread(new MyWorker(semaphore));
			t.start();
		}
		System.out.println("Action...GO!");
		// 释放5个permit
		semaphore.release(5);
		
		// 等待所有permit都被使用
		System.out.println("Wait for permits off");
		while (semaphore.availablePermits() != 0) {
			Thread.sleep(100L);
		}
		
		// 重新释放下一批permit
		System.out.println("Action...GO again!");
		semaphore.release(5);
	}
}

class MyWorker implements Runnable {
	private Semaphore semaphore;

	public MyWorker(Semaphore semaphore) {
		this.semaphore = semaphore;
	}

	@Override
	public void run() {
		try {
			semaphore.acquire(); // 消费1个permit
			System.out.println("Executed!");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}