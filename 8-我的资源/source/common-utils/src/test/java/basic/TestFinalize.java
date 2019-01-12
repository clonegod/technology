package basic;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import clonegod.uitls.ThreadUtils;

/**
 * 不推荐finalize中释放资源的原因：
 * 	1、finalize执行的不可预测性
 *  2、 finalize中的代码如果执行耗时长，则会严重影响GC的回收速度
 *
 */
public class TestFinalize {

	int[] bytes = new int[1024*10];

	@Override
	protected void finalize() throws Throwable {
		System.out.println(ThreadUtils.currentThreadName() + "执行资源回收开始, object=" + this.hashCode());
		TimeUnit.MILLISECONDS.sleep(3000); // 模拟释放资源的操作非常耗时
		System.out.println(ThreadUtils.currentThreadName() + "执行资源回收结束, object=" + this.hashCode());
	}

	public static void main(String[] args) throws InterruptedException {
		// 创建大量的对象，每个对象被GC回收之前，都会调用该对象的finalize()
		// 如果finalize不能快速执行结束，将会响应GC的性能
		IntStream.range(1, 100).forEach(n -> {
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						new TestFinalize();
						ThreadUtils.sleep(100);
					}
				}
			}).start();
		});
	}

}
