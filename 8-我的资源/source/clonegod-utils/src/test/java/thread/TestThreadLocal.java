package thread;

public class TestThreadLocal {
	/**
	 * 一个Thread内部只有一个ThreadLocalMap实例的引用
	 * 而一个ThreadLocalMap中可以存储多个ThreadLocal作为key的entry
	 * 
	 * 也就是，一个Thread在生命周期内可以使用多个不同的ThreadLocal对象！
	 * 
	 * 线程与ThreadLocal的关系： 多对多
	 * 线程与ThreadLocalMap的关系：1对1
	 * ThreadLocal与ThreadLocalMap的关系：多对1
	 */
	static ThreadLocal<Object> threadLocal1 = new ThreadLocal<>();
	static ThreadLocal<Object> threadLocal2 = new ThreadLocal<>();
	
	public static void main(String[] args) {
		threadLocal1.set("123"); // ThreadLocalMap中的key是threadLocal1，value是"123"
		threadLocal2.set("234"); // ThreadLocalMap中的key是threadLocal2，value是"234"
		
		Object value1 = threadLocal1.get(); // 从ThreadLocalMap中将key=threadLocal1的value取出
		System.out.println(value1);
		
		Object value2 = threadLocal2.get(); // 从ThreadLocalMap中将key=threadLocal2的value取出
		System.out.println(value2);
		
		// 使用完毕，主动释放ThreadLocalMap中的数据，即删除ThreadLocalMap中key为threadLocal1,threadLocal2中的entry!
		threadLocal1.remove(); 
		threadLocal2.remove();
	}
	
}
