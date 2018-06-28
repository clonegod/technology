# Lession3 | 谈谈final、finally、 finalize有什么不同？


## final
final 可以用来修饰类、方法、变量，分别有不同的意义:

	final 修饰的 class 代表不可以继承扩展
	final 的变量是不可以修改的
	final 的方法也是不可以重写的（override）。
	
###### 使用final 能带来什么好处？
推荐使用 final 关键字来明确表示我们代码的语义、逻辑意图，

这已经被证明在很多场景下是非常好的实践，比如：

	1、使用 final 修饰参数或者变量，也可以清楚地避免意外赋值导致的编程错误，
	甚至，有人明确推荐将所有方法参数、本地变量、成员变量声明成 final。

	2、final 变量产生了某种程度的不可变（immutable）的效果，
	所以，可以用于保护只读数据，尤其是在并发编程中，
	因为明确地不能再赋值 final 变量，有利于减少额外的同步开销，也可以省去一些防御性拷贝的必要。	

---
## finally
	finally 则是 Java 保证重点代码一定要被执行的一种机制。
	我们可以使用 try-finally 或者 try-catch-finally 来进行类似关闭 JDBC 连接、保证 unlock 锁等动作。

	》》》通常来说，利用try-with-resources 或者 try-finally 机制，是非常好的回收资源的办法。
	如果确实需要额外处理，可以考虑 Java 提供的 Cleaner 机制或者其他替代方法。

##### finally 中的代码一定会被执行到吗？
	答案是否定的！

	public class TestFinally {
	
	public static void main(String[] args) {
		try {
				System.out.println("application start");
				System.exit(1);
			} finally {
				System.out.println("finally 被执行"); // JVM已经退出，此时finally不会被执行
			}
		}
		
	}


###### 不要在finally中return返回值，否则会try或catch中的返回值
	public class Finally {
	public static void main(String[] args) {
		// It will print "c" both times!!!

	    System.out.println(finallyTester(true));
	    System.out.println(finallyTester(false));
	}
	
	public static String finallyTester(boolean succeed) {
	    try {
	        if(succeed) {
	            return "a";
	        } else {
	            throw new Exception("b");
	        }
	    } catch(Exception e) {
	        return "b";
	    } finally {
	        return "c";
	    }
	  }
	
	}
	

---
## finalize
	finalize 是基础类 java.lang.Object 的一个方法
	它的设计目的是保证对象在被垃圾收集前完成特定资源的回收。
	finalize 机制现在已经不推荐使用，并且在 JDK 9 开始被标记为 deprecated。
	
	对于 finalize，我们要明确它是不推荐使用的，业界实践一再证明它不是个好的办法，
	在 Java 9 中，甚至明确将 Object.finalize() 标记为 deprecated！
	如果没有特别的原因，不要实现 finalize 方法，也不要指望利用它来进行资源回收。	

	为什么不建议使用finalize呢？
	简单说，你无法保证 finalize 什么时候执行，执行的是否符合预期。
	如果使用不当会影响性能（尤其是影响GC对内存的快速回收），导致程序死锁、挂起等。
	
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


---
## 知识扩展

### 1. 注意，final 不是 immutable！
	final 只能约束 strList 这个引用不可以被赋值，
	但是 strList 对象行为不被 final 影响，添加元素等操作是完全正常的。
	如果我们真的希望对象本身是不可变的，那么需要相应的类支持不可变的行为。
	
	 final List<String> strList = new ArrayList<>();
	 strList.add("Hello");
	 strList.add("world");  
	 List<String> unmodifiableStrList = List.of("hello", "world");
	 unmodifiableStrList.add("again");

### Immutable 在很多场景是非常棒的选择，实现 immutable 的类，需要做到：
	1、将 class 自身声明为 final，这样别人就不能扩展来绕过限制了。
	2、将所有成员变量定义为 private 和 final，并且不要实现 setter 方法。
	3、通常构造对象时，成员变量使用深度拷贝来初始化，而不是直接赋值，
		这是一种防御措施，因为你无法确定输入对象不被其他人修改。
	4、如果确实需要实现 getter 方法，或者其他可能会返回内部状态的方法，
		使用 copy-on-write 原则，创建私有的 copy。
	
###	2.finalize 真的那么不堪？缺点是什么？
	finalize 的执行是和垃圾收集关联在一起的，
	一旦实现了非空的 finalize 方法，就会导致相应对象回收呈现数量级上的变慢，
	有人专门做过 benchmark，大概是 40~50 倍的下降。
	
	因为，finalize 被设计成在对象被垃圾收集前调用，
	这就意味着实现了 finalize 方法的对象是个“特殊公民”，JVM 要对它进行额外处理。finalize 本质上成为了快速回收的阻碍者，可能导致你的对象经过多个垃圾收集周期才能被回收。

	实践中，因为 finalize 拖慢垃圾收集，导致大量对象堆积，也是一种典型的导致 OOM 的原因。

	从另一个角度，我们要确保回收资源就是因为资源都是有限的，
	垃圾收集时间的不可预测，可能会极大加剧资源占用。
	这意味着对于消耗非常高频的资源，千万不要指望 finalize 去承担资源释放的主要职责，
	最多让 finalize 作为最后的兜底，况且它已经暴露了如此多的问题。

	》》》基于以上分析，推荐资源用完即显式释放，或者利用资源池来尽量重用！

### 3. 有什么机制可以替换 finalize 吗？
	Java 平台目前在逐步使用 java.lang.ref.Cleaner 来替换掉原有的 finalize 实现。
	Cleaner 的实现利用了幻象引用（PhantomReference），
	这是一种常见的所谓 post-mortem 清理机制。
	利用幻象引用和引用队列，我们可以保证对象被彻底销毁前做一些类似资源回收的工作
	比如关闭文件描述符（操作系统有限的资源），它比 finalize 更加轻量、更加可靠。

	注意，从可预测性的角度来判断，Cleaner 或者幻象引用改善的程度仍然是有限的，
	如果由于种种原因导致幻象引用堆积，同样会出现问题（对象无法快速被GC回收）。
	所以，Cleaner 适合作为一种最后的保证手段，而不是完全依赖 Cleaner 进行资源回收，
	不然我们就要再做一遍 finalize 的噩梦了。

	建议显示地主动释放资源，不要太过依赖自动清理机制。

### 4、Cleaner 释放资源需要注意的地方

这种代码如果稍有不慎添加了对资源的强引用关系，就会导致循环引用关系，

前面提到的 MySQL JDBC 就在特定模式下有这种问题，导致内存泄漏。

	public class CleaningExample implements AutoCloseable {
        // A cleaner, preferably one shared within a library
        private static final Cleaner cleaner = <cleaner>;
        static class State implements Runnable { 
            State(...) {
                // initialize State needed for cleaning action
            }
            public void run() {
                // cleanup action accessing State, executed at most once
            }
        }
        private final State;
        private final Cleaner.Cleanable cleanable
        public CleaningExample() {
            this.state = new State(...);
            this.cleanable = cleaner.register(this, state);
        }
        public void close() {
            cleanable.clean();
        }
    }
 
上面的示例代码中，将 State 定义为 static，就是为了避免普通的内部类隐含着对外部对象的强引用，因为那样会使外部对象无法进入幻象可达的状态。

### 5、幻象引用和引用队列释放资源的应用
	我也注意到很多第三方库自己直接利用幻象引用定制资源收集，
	比如广泛使用的MySQL JDBC driver 之一的mysql-connector-j，就利用了幻象引用机制。
	幻象引用也可以进行类似链条式依赖关系的动作
	比如，进行总量控制的场景，保证只有连接被关闭，相应资源被回收，连接池才能创建新的连接。

	》》》 Netty对堆外内存的回收貌似用的就是幻象引用和引用队列来实现释放操作的！