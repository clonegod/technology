# Lesson7 | 谈谈 int 和Integer的区别？

	Java 虽然号称是面向对象的语言，但是原始数据类型仍然是重要的组成元素
	经常考察原始数据类型和包装类等 Java 语言特性。

## int 原始数据类型 
	int 是我们常说的整形数字，是 Java 的 8 个原始数据类型之一。
	Primitive Types:
		boolean、byte 、short、char、int、float、double、long

## Integer包装类型
	Integer 是 int 对应的包装类，它有一个 int 类型的字段存储数据，并且提供了基本操作，
	比如数学运算、int 和字符串之间转换等。

	在 Java 5 中，引入了自动装箱和自动拆箱功能（boxing/unboxing），
	Java 可以根据上下文，自动进行转换，极大地简化了相关编程。

## Integer的值缓存范围（-128 ~ 127）？
	关于 Integer 的值缓存，这涉及 Java 5 中另一个改进。
	构建 Integer 对象的传统方式是直接调用构造器，直接 new 一个对象。
	但是根据实践，我们发现大部分数据操作都是集中在有限的、较小的数值范围，
	因此，在 Java 5 中新增了静态工厂方法 valueOf，
	在调用它的时候会利用一个缓存机制，带来了明显的性能改进。

---
#知识扩展

### 1. 理解自动装箱、拆箱
	自动装箱实际上算是一种语法糖。

	什么是语法糖？
	可以简单理解为 Java 平台为我们自动进行了一些转换，保证不同的写法在运行时等价，
	它们发生在编译阶段，也就是生成的字节码是一致的。

	javac 替我们自动把装箱转换为 Integer.valueOf()，把拆箱替换为 Integer.intValue()
	

## 自动装箱的时候，缓存机制起作用吗？
	使用静态工厂方法 valueOf 会使用到缓存机制，
	自动装箱调用的是Integer.valueOf，因此能够得到缓存的好处。


## 为什么我们需要原始数据类型，Java 的对象似乎也很高效？
	原则上，建议避免无意中的装箱、拆箱行为，尤其是在性能敏感的场合，
	创建 10 万个 Java 对象和 10 万个整数的开销可不是一个数量级的，
	不管是内存使用还是处理速度，光是对象头的空间占用就已经是数量级的差距了。

	我们其实可以把这个观点扩展开，在性能极度敏感的场景往往具有比较大的优势，
	用原始类型替换掉包装类、动态数组（如 ArrayList）等可以作为性能优化的备选项。
	一些追求极致性能的产品或者类库，会极力避免创建过多对象。
	当然，在大多数产品代码里，并没有必要这么做，还是以开发效率优先。

#
	
	/**
	 * 一个常见的线程安全计数器实现。
	 */
	class Counter {
	    private final AtomicLong counter = new AtomicLong(); // 利用CAS保证线程安全的原子操作
	    public void increase() {
	        counter.incrementAndGet();
	    }
	}

#
	/**
	 * 利用原始数据类型，实现一个线程安全的计数器。
	 * 使用原子数据类型，而不是包装类型，可以进一步提高性能。
	 */
	public class CompactCounter {
	    private volatile long counter; // 原始数据类型
	    
	    private static final AtomicLongFieldUpdater<CompactCounter> updater = 
	    			AtomicLongFieldUpdater.newUpdater(CompactCounter.class, "counter");
	    
	    public void increase() {
	        updater.incrementAndGet(this);
	    }
	    
	    public static void main(String[] args) throws Exception {
			final CompactCounter counter = new CompactCounter();
			
			ExecutorService executor = Executors.newFixedThreadPool(100);
			IntStream.rangeClosed(1, 1000)
					.forEach(n -> {
						executor.execute(() -> {
							counter.increase();
						});
					});
			
			executor.shutdown();
			executor.awaitTermination(3, TimeUnit.SECONDS);
			
			System.out.println(counter.counter);
		}
	}



## 2. Integer源码分析
	整体看一下 Integer 的职责，它主要包括各种基础的常量，比如最大值、最小值、位数等；
	前面提到的各种静态工厂方法 valueOf()；
	各种转换方法，比如转换为不同进制的字符串，如 8 进制，或者反过来的解析方法等。

	首先，继续深挖缓存，Integer 的缓存范围虽然默认是 -128 到 127，
	但是在特别的应用场景，比如我们明确知道应用会频繁使用更大的数值，这时候应该怎么办呢？
	
	缓存上限值实际是可以根据需要调整的，JVM 提供了参数设置：
		-XX:AutoBoxCacheMax=N


#
	// Integer缓存，在 IntegerCache 的静态初始化块里实现。
	private static class IntegerCache {
        static final int low = -128;
        static final int high;
        static final Integer cache[];

        static {
            // high value may be configured by property
            int h = 127;
            String integerCacheHighPropValue =
                sun.misc.VM.getSavedProperty("java.lang.Integer.IntegerCache.high");
            if (integerCacheHighPropValue != null) {
                try {
                    int i = parseInt(integerCacheHighPropValue);
                    i = Math.max(i, 127);
                    // Maximum array size is Integer.MAX_VALUE
                    h = Math.min(i, Integer.MAX_VALUE - (-low) -1);
                } catch( NumberFormatException nfe) {
                    // If the property cannot be parsed into an int, ignore it.
                }
            }
            high = h;

            cache = new Integer[(high - low) + 1];
            int j = low;
            for(int k = 0; k < cache.length; k++)
                cache[k] = new Integer(j++);

            // range [-128, 127] must be interned (JLS7 5.1.7)
            assert IntegerCache.high >= 127;
        }

        private IntegerCache() {}
    }
 

## 3. 原始类型线程安全性问题
	原始数据类型的变量，显然要使用并发相关手段，才能保证线程安全。
	如果有线程安全的计算需要，建议考虑使用类似 AtomicInteger、AtomicLong 这样的线程安全类。

	特别的是，部分比较宽的数据类型，比如 float、double，甚至不能保证更新操作的原子性，
	可能出现程序读取到只更新了一半数据位的数值！

## 4.Java 原始数据类型和引用类型局限性
	从 Java 平台发展的角度来看看，原始数据类型、对象的局限性和演进。

	原始数据类型和 Java 泛型并不能配合使用；

	Java 的对象都是引用类型，如果是一个原始数据类型数组，它在内存里是一段连续的内存，
	而对象数组则不然，数据存储的是引用，对象往往是分散地存储在堆的不同位置。
	这种设计虽然带来了极大灵活性，但是也导致了数据操作的低效，尤其是无法充分利用现代 CPU 缓存机制。

	Java 为对象内建了各种多态、线程安全等方面的支持，但这不是所有场合的需求，
	尤其是数据处理重要性日益提高，更加高密度的值类型是非常现实的需求。
	
	