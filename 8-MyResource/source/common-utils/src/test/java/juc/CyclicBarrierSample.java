package juc;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


/**
 * 如果用 CyclicBarrier 来表达这个场景呢？我们知道 CyclicBarrier 其实反映的是线程并行运行时的协调，
 * 	在下面的示例里，从逻辑上，5 个工作线程其实更像是代表了 5 个可以就绪的空车，而不再是 5 个乘客，
 * 	对比前面 CountDownLatch 的例子更有助于我们区别它们的抽象模型，
 * 
 */
public class CyclicBarrierSample {
	
	public static void main(String[] args) throws InterruptedException {
		CyclicBarrier barrier = new CyclicBarrier(5, new Runnable() {
			@Override
			public void run() {
				System.out.println("Action...GO again!");
			}
		});
		for (int i = 0; i < 5; i++) {
			Thread t = new Thread(new CyclicWorker(barrier));
			t.start();
		}
	}

	static class CyclicWorker implements Runnable {
		private CyclicBarrier barrier;

		public CyclicWorker(CyclicBarrier barrier) {
			this.barrier = barrier;
		}

		@Override
		public void run() {
			try {
				for (int i = 0; i < 3; i++) {
					System.out.println("Executed!");
					barrier.await(); // 直到所有线程都调用了await()才解除阻塞，然后barrier内部会进行状态重置（即barrier可以重用）
				}
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}