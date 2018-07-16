# Lesson17 | Java 并发包提供了哪些并发工具类？

我们通常所说的并发包也就是 java.util.concurrent 及其子包，
集中了 Java 并发的各种基础工具类，具体主要包括几个方面：
	
	【同步结构】
	提供了比 synchronized 更加高级的各种同步结构，包括: 
	CountDownLatch、CyclicBarrier、Phaser、Semaphore 等，可以实现更加丰富的多线程操作，
	比如利用 Semaphore 作为资源控制器，限制同时进行工作的线程数量。
	
	【线程安全的容器】
	比如最常见的 ConcurrentHashMap、有序的 ConcunrrentSkipListMap
	通过类似快照机制，实现线程安全的动态数组 CopyOnWriteArrayList、CopyOnWriteArraySet 等。
	
	【并发阻塞队列】
	如各种 BlockedQueue 实现，
	比较典型的 ArrayBlockingQueue、 LinkedBlockingQueue、SynchorousQueue 
	或针对特定场景的 PriorityBlockingQueue 等。

	【强大的 Executor 框架】
	封装了若干工厂方法，用来创建各种不同类型的线程池，调度任务运行等，
	绝大部分情况下，不再需要自己从头实现线程池和任务调度器。
	如果有特殊需求，可以调用ThreadPoolExecutor构造函数来创建线程池。


## CountDownLatch
	允许一个或多个线程等待某些操作完成。
	

## CyclicBarrier
	一种辅助性的同步结构，多个线程等待，知道全部到达某个屏障，才解除阻塞。

## Semaphore
	Java 版本的信号量实现。
	它通过控制一定数量的允许（permit）的方式，来达到限制通用资源访问的目的。
	总的来说，我们可以看出 Semaphore 就是个计数器，
	其基本逻辑基于 acquire/release，并没有太复杂的同步逻辑。


## Phaser
	Java 并发类库还提供了Phaser，功能与 CountDownLatch 很接近，
	但是它允许线程动态地注册到 Phaser 上面，而 CountDownLatch 显然是不能动态设置的。
	
	Phaser 的设计初衷是，实现多个线程类似步骤、阶段场景的协调，
	线程注册等待屏障条件触发，进而协调彼此间行动，具体请参考这个

## CountDownLatch 和 CyclicBarrier 二者有什么区别
	CountDownLatch 是不可以重置的，所以无法重用；
	而 CyclicBarrier 则没有这种限制，可以重用。

	CountDownLatch 的基本操作组合是 countDown/await。
	调用 await 的线程阻塞等待 countDown 足够的次数，不管你是在一个线程还是多个线程里 countDown，只要次数足够即可。
	CountDownLatch 操作的是事件。

	CyclicBarrier 的基本操作组合，则就是 await，
	当所有的伙伴（parties）都调用了 await，才会继续进行任务，并自动进行重置。
	正常情况下，CyclicBarrier 的重置都是自动发生的，如果我们调用 reset 方法，
	但还有线程在等待，就会导致等待线程被打扰，抛出 BrokenBarrierException 异常。
	CyclicBarrier 侧重点是线程，而不是调用事件，它的典型应用场景是用来等待并发线程结束。


## 线程安全 Map、List 和 Set
![](img/juc-conc-util.png)

	如果我们的应用侧重于 Map 放入或者获取的速度，而不在乎顺序，
	大多推荐使用 ConcurrentHashMap，反之则使用 ConcurrentSkipListMap；
	
	如果我们需要对大量数据进行非常频繁地修改，ConcurrentSkipListMap 也可能表现出优势。

	

---
## 知识扩展
 
#### 为什么并发容器里面没有 ConcurrentTreeMap 呢？
	普通无顺序场景选择 HashMap，有顺序场景则可以选择类似 TreeMap 等，
	但是为什么并发容器里面没有 ConcurrentTreeMap 呢？

	这是因为 TreeMap 要实现高效的线程安全是非常困难的，它的实现基于复杂的红黑树。
	为保证访问效率，当我们插入或删除节点时，会移动节点进行平衡操作，
	这导致在并发场景中难以进行合理粒度的同步。

	而 SkipList 结构则要相对简单很多，通过层次结构提高访问速度，
	虽然不够紧凑，空间使用有一定提高（O(nlogn)），
	但是在增删元素时线程安全的开销要好很多。
	
	SkipList 的内部结构示意图：
![](img/skiplist.png)


## CopyOnWrite 到底是什么意思呢?
	注意： 
		这种数据结构，相对比较适合读多写少的操作，不然修改的开销还是非常明显的。

	它的原理是，任何修改操作，如 add、set、remove，都会拷贝原数组，
	修改后替换原来的数组，通过这种防御性的方式，实现另类的线程安全。

	请看下面的代码片段，我进行注释的地方，可以清晰地理解其逻辑。

	public boolean add(E e) {
	    synchronized (lock) {
	        Object[] elements = getArray();
	        int len = elements.length;
	           // 拷贝
	        Object[] newElements = Arrays.copyOf(elements, len + 1);
	        newElements[len] = e;
	           // 替换
	        setArray(newElements);
	        return true;
	            }
	}
	final void setArray(Object[] a) {
	    array = a;
	}
	