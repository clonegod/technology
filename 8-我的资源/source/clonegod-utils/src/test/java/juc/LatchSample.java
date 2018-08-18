package juc;

import java.util.concurrent.CountDownLatch;

/**
 * 如果用 CountDownLatch 去实现上面的排队场景，该怎么做呢？
 * 
 * 假设有 10 个人排队，我们将其分成 5 个人一批，通过 CountDownLatch 来协调批次，你可以试试下面的示例代码。
 *
 */
public class LatchSample {
	public static void main(String[] args) throws InterruptedException {
		final CountDownLatch sharedLatch = new CountDownLatch(5);
		
		for (int i = 0; i < 5; i++) {
			new Thread(new FirstBatchWorker(sharedLatch)).start();
			new Thread(new SecondBatchWorker(sharedLatch)).start();
		}
	}
}

class FirstBatchWorker implements Runnable {
	private CountDownLatch latch;

	public FirstBatchWorker(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public void run() {
		System.out.println("First batch executed!");
		latch.countDown(); // latch内部计数器减1
	}
}

class SecondBatchWorker implements Runnable {
	private CountDownLatch latch;

	public SecondBatchWorker(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public void run() {
		try {
			latch.await(); // 等待latch减为0
			System.out.println("Second batch executed!");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}