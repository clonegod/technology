package java8.core.completable;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class Shop {
	private String shopName;
	public Shop(String name) {
		this.shopName = name;
	}

	public String getShopName() {
		return shopName;
	}
	
	/**
	 * 最低效的同步调用 
	 */
	public String getPriceSync(String product) {
		double price = doCalculatePrice(product);
		Discount.Code code = Discount.Code.values()[ random.nextInt(Discount.Code.values().length)];
		String priceInfos = String.format("%s:%.2f:%s", shopName, price, code);
		return priceInfos;
	}

	/**
	 * 创建一个新线程，异步执行实际的价格计算工作
	 */
	public CompletableFuture<Double> getPriceAsync(String product) {
		CompletableFuture<Double> futurePrice = new CompletableFuture<>();
		
		new Thread(() -> {
			try {
				double price = doCalculatePrice(product);
				futurePrice.complete(price); // 设置计算的结果
			} catch (Exception e) {
				futurePrice.completeExceptionally(e); // 非forkjoinpoll线程池执行，异常需要手动设置，否则发生异常后调用者无法得到异常信息
			}
		}).start();
		
		// 立即返回future，不阻塞主线程的执行
		return futurePrice;
	}
	
	/**
	 * 使用CompletableFuture 提供异步API，将一个同步又缓慢的服务转换为异步的服务
	 */
	public CompletableFuture<Double> getPriceAsyncForkJonePool(String product) {
		return CompletableFuture.supplyAsync(() -> doCalculatePrice(product)); // 内部已封装对异常的处理
	}

	
	private static final Random random = new Random();
	
	/** 模拟耗时的远程http接口 */
	public double doCalculatePrice(String product) {
		randomDelay();
		if(Math.random() > 0.99) {
			System.out.println(1 / 0);
		}
		System.out.println("Calc: "+ Thread.currentThread().getName());
		return random.nextDouble() * product.charAt(0) + product.charAt(1);
	}

	public static void randomDelay() {
		int delay = 500 + random.nextInt(2000);
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	
}
