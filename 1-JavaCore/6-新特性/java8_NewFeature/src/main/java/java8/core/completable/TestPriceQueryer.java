package java8.core.completable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import java8.core.completable.ExchangeService.Money;

/**趋势一： 并行计算*/
// 将一个操作切分为多个子操作，在多个不同的核、CPU甚至是机器上并行地执行这些子操作。
// java7 ForkJoin 需要开发者自己实现任务的拆分逻辑
// java8 Stream 内部使用Spliter自动完成对集合元素的拆分，提交到ForkJoinPoll线程池并行计算

/**趋势二：异步执行*/
// 如果你的意图是实现并发，而非并行，或者你的主要目标是在同一个CPU上执行几个松耦合的任务，充分利用CPU的核，让其足够忙碌，从而最大化程序的吞吐量，
// 那么你其实真正想做的是避免线程被阻塞（等待远程服务的返回，或等待数据库的查询结果），而浪费宝贵的计算资源，因为这种等待的时间很可能相当长。
// 让执行任务流程的线程不阻塞，主线程可以继续执行其他任务，不会被某个耗时操作所影响
// ---> Future / CompletableFuture - 解放执行线程，不再因异步结果而阻塞


/**异步API： 服务端接收到请求后，将请求交给另一个线程去处理，并立即响应客户端；客户端需要结果时，再次发起请求获取结果，此时，有几种可能：1、结果已计算完成；2、正在计算中；3、发生异常，计算失败*/
// 异步调用/异步接口的好处：客户端和服务器不需要一直保持连接状态，双方不是紧耦合的，系统调用的灵活度很高。

// 多个 CompletableFuture 的整合 
// thenCompose 前一个future的结果，作为参数传递给第二个completeable，第二个依赖于前一个的结果，因此需要等待第一个完成后才能执行第二个
// thenCombine 将两个没有依赖关系的completeablefuture结合起来，得到一个结果，第二个不依赖前一个的结果，因此两个可以同时执行
// thenAccept 对CompleteableFuture结果进行消费

/** 接口调用的几种方式 */
// 服务端提供同步接口，客户端同步调用
// 服务端提供同步接口，客户端使用CompleteableFuture实现异步调用
// 服务端提供异步接口，客户端直接异步调用

public class TestPriceQueryer {

	List<Shop> shops = Arrays.asList(new Shop("BestPrice"),
			new Shop("LetsSaveBig"),
			new Shop("MyFavoriteShop"),
			new Shop("BuyItAll"),
			new Shop("ShopEasy1"),
			new Shop("ShopEasy2"),
			new Shop("ShopEasy3"),
			new Shop("ShopEasy4"),
			new Shop("ShopEasy5"));
	
	
	/**服务端提供的服务，可能是同步接口，也可能是返回异步的Future，但是调用方可以将同步接口的调用改造为异步调用*/
	
	
	@Test
	public void test01_UseCompletableFuture() {
		Shop shop = new Shop("BestShop");
		
		// 1、服务端同步执行，阻塞主线程
		String res = shop.getPriceSync("my favorite product"); 
		System.out.println(res);
		
		// 2、服务端返回future，不会阻塞主线程
		long start = System.nanoTime();
		CompletableFuture<Double> futurePrice = shop.getPriceAsync("my favorite product"); 
		
		long invocationTime = ((System.nanoTime() - start) / 1_000_000);
		System.out.println("Invocation returned after " + invocationTime + " msecs");
		
		doSomethingElse(); // 主线程继续执行更多任务
		try {
			double price = futurePrice.get(5, TimeUnit.SECONDS); // 建议设置超时时间，防止服务方长时间不返回结果
			System.out.printf("Price is %.2f%n", price);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		long retrievalTime = ((System.nanoTime() - start) / 1_000_000);
		System.out.println("Price returned after " + retrievalTime + " msecs");
	}

	private static void doSomethingElse() {
		System.out.println("....................do some thing?");
	}
	
	
	//--------------------------------------------------------//
	
	@Test
	public void test02_findPricesSequential() {
		long start = System.nanoTime();
		System.out.println(findPricesOnebyOne("myPhone27S"));
		long duration = (System.nanoTime() - start) / 1_000_000;
		System.out.println("Done in " + duration + " msecs");
	}
	
	// 一个接着一个调用，效率低
	public List<String> findPricesOnebyOne(String product) {
		return shops.stream().map(shop -> shop.getPriceSync(product))
				.collect(Collectors.toList());
	}
		
	
	//--------------------------------------------------------//
	
	@Test
	public void test03_findPricesParallel() {
		long start = System.nanoTime();
		System.out.println(findPricesParallel("myPhone27S"));
		long duration = (System.nanoTime() - start) / 1_000_000;
		System.out.println("Done in " + duration + " msecs");
	}
	
	// 对所有商店实现并行查询
	public List<String> findPricesParallel(String product) {
		// java.util.concurrent.ForkJoinPool.common.parallelism 可以通过系统参数进行设置
		return shops.parallelStream().map(shop -> shop.getPriceSync(product))
				.collect(Collectors.toList());
	}
	
	
	//--------------------------------------------------------//
	
	@Test
	public void test04_findPricesCompletableFutureMapOnce() {
		long start = System.nanoTime();
		System.out.println(findPricesCompletableFutureMapOnce("myPhone27S"));
		long duration = (System.nanoTime() - start) / 1_000_000;
		System.out.println("Done in " + duration + " msecs");
	}
	
	// 使用CompletableFuture将同步方法的调用，改造为异步 - 陷阱
	public List<String> findPricesCompletableFutureMapOnce(String product) {
		// 顺序执行流水线
		return shops.stream()
			.map(shop -> CompletableFuture.supplyAsync(() ->  shop.getPriceSync(product)))
			.map(CompletableFuture::join) // 由于join操作会导致整个流操作以同步、顺序执行：新的CompletableFuture对象只有在前一个操作完全结束之后，才能创建。因此，这样调用将无法实现多个任务的并发启动！
			.collect(Collectors.toList());
	}
	
	
	
	
	
	@Test
	public void test04_findPricesCompletableFutureMapSplit() {
		long start = System.nanoTime();
		System.out.println(findPricesCompletableFutureMapSplit("myPhone27S"));
		long duration = (System.nanoTime() - start) / 1_000_000;
		System.out.println("Done in " + duration + " msecs");
	}
	
	// 使用CompletableFuture将同步方法的调用，改造为异步 - 拆分map
	public List<String> findPricesCompletableFutureMapSplit(String product) { 
		// 并行执行流水线
		List<CompletableFuture<String>> priceFutures =
				shops.stream()
				.map(shop -> CompletableFuture.supplyAsync(() -> shop.getPriceSync(product), executor))
				.collect(Collectors.toList());
		
		// 到这里，上面实际上已经启动了所有的远程调用，因此，最后等待结果不会影响到任务的执行
		return priceFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
	}
	
	
	//--------------------------------------------------------//
	
	@Test
	public void test05_findPricesDiscount() {
		long start = System.nanoTime();
		System.out.println(findPricesDiscount("myPhone27S"));
		long duration = (System.nanoTime() - start) / 1_000_000;
		System.out.println("Done in " + duration + " msecs");
	}
	
	/** 将多个异步操作结合在一起，以流水线的方式运行 */
	public List<String> findPricesDiscount(String product) { 
		//对多个异步任务进行流水线操作
		return shops.stream()
				.map(shop -> shop.getPriceSync(product))
				.map(Quote::parse)
				.map(Discount::applyDiscount)
				.collect(Collectors.toList());
	}
	
	
	//--------------------------------------------------------//
	
	@Test
	public void test06_thenCompose() {
		long start = System.nanoTime();
		System.out.println(findPricesThenCompose("myPhone27S"));
		long duration = (System.nanoTime() - start) / 1_000_000;
		System.out.println("Done in " + duration + " msecs");
	}
	
	public List<String> findPricesThenCompose(String product) { 
		// 在需要的地方把它们变成了异步操作
		List<CompletableFuture<String>> priceFuture = 
				shops.stream()
					.map(shop -> CompletableFuture.supplyAsync(
							() -> shop.getPriceSync(product), executor)) // 远程耗时操作都通过异步方式提交
					.map(future -> future.thenApply(Quote::parse))
					// thenCompose方法允许你对两个异步操作进行流水线，将前面操作的结果作为参数传递给第二个操作
					.map(future -> future.thenCompose(quote -> 
									CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote), executor))) // 远程耗时操作都通过异步方式提交
					.collect(Collectors.toList());
		
		return priceFuture.stream()
				.map(CompletableFuture::join) // 阻塞，等待流中的所有Future执行完毕，并提取各自的返回值
				.collect(Collectors.toList());
	}
	
	
	//--------------------------------------------------------//
	
	@Test
	public void test07_thenCombine() {
		long start = System.nanoTime();
		
		Stream<CompletableFuture<String>> stream = findPricesThenCombine("myPhone27S");
		CompletableFuture<?>[] futures =  
				stream.map(f -> f.thenAccept(
							data -> System.out.println(data + " (done in " + ((System.nanoTime() - start) / 1_000_000) + " msecs)")))
				.toArray(size -> new CompletableFuture[size]);
		
		CompletableFuture.allOf(futures).join(); // 全部完成
		
		long duration = (System.nanoTime() - start) / 1_000_000;
		System.out.println("Done in " + duration + " msecs");
	}
	
	private Stream<CompletableFuture<String>> findPricesThenCombine(String product) {
		// 第1个CompleteableFuture计算价格
		// 第2个CompleteableFuture获取汇率（不依赖第1个CompleteableFuture）
		// 将2个结合CompleteableFuture结合，计算出价格
		
		return 
			shops.stream()
			.map( shop -> CompletableFuture.supplyAsync(() -> shop.doCalculatePrice(product))
					.thenCombine(CompletableFuture.supplyAsync(
							() -> ExchangeService.getRate(Money.EUR, Money.USD)), 
							new BiFunction<Double, Double, String>() {
								public String apply(Double price, Double rate) {
									System.out.println(price + "\t" + rate);
									return String.valueOf(price * rate);
								}
							})
				);
	}
	
	
	//--------------------------------------------------------//
	
	@Test
	public void test08_thenAccept() {
		long start = System.nanoTime();
		
		Stream<CompletableFuture<String>> stream = findPricesStream("myPhone27S"); // 返回一个异步流类型的结果
		
		CompletableFuture<?>[] futures =  
				stream.map(f -> f.thenAccept(
							data -> System.out.println(data + " (done in " + ((System.nanoTime() - start) / 1_000_000) + " msecs)")))
				.toArray(size -> new CompletableFuture[size]);
		
		CompletableFuture.allOf(futures).join(); // 等待所有结果，全部完成。否则，一直阻塞
		
//		CompletableFuture.anyOf(futures).join(); // 只要任一完成，解除阻塞
		
		System.out.println("All shops have now responded in " + ((System.nanoTime() - start) / 1_000_000) + " msecs");
	}
	
	private Stream<CompletableFuture<String>> findPricesStream(String product) {
		// 在每个CompletableFuture上注册一个操作
		return shops.stream()
					.map(shop -> CompletableFuture.supplyAsync(() -> shop.getPriceSync(product), executor))
					.map(future -> future.thenApply(Quote::parse))
					.map(future -> future.thenCompose(quote -> 
							CompletableFuture.supplyAsync(
									() -> Discount.applyDiscount(quote), executor)));
	}
	

	//--------------------------------------------------------//
	
	// 创建一个由守护线程构成的线程池
	private final AtomicInteger nextId = new AtomicInteger(0);
	private final Executor executor = Executors.newFixedThreadPool(Math.min(100, shops.size()), new ThreadFactory() {
		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			t.setName("PREFIX-" + nextId.incrementAndGet());
			t.setDaemon(true);
			return t;
		}
	});
	
}
