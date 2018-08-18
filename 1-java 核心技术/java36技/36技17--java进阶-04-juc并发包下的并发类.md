# Lesson17 | Java 并发包提供了哪些并发工具类？

我们通常所说的并发包也就是 java.util.concurrent 及其子包，
集中了 Java 并发的各种基础工具类，具体主要包括几个方面：
	
	【同步结构】
	提供了比 synchronized 更加高级的各种同步结构，包括: 
	CountDownLatch、CyclicBarrier、Phaser、Semaphore 等，可以实现更加丰富的多线程操作，
	比如利用 Semaphore 作为资源控制器，限制同时进行工作的线程数量。
	
	【线程安全的容器】
	比如最常见的 
		无序的 ConcurrentHashMap、
		有序的 ConcunrrentSkipListMap

	通过类似快照机制，实现线程安全的动态数组 
		CopyOnWriteArrayList、CopyOnWriteArraySet
	
	【并发阻塞队列】
	如各种 BlockedQueue 实现，
	比较典型的 ArrayBlockingQueue、 LinkedBlockingQueue、SynchorousQueue 
	或针对特定场景的 PriorityBlockingQueue 等。

	【强大的 Executor 框架】
	封装了若干工厂方法，用来创建各种不同类型的线程池，调度任务运行等，
	绝大部分情况下，不再需要自己从头实现线程池和任务调度器。
	如果有特殊需求，可以调用ThreadPoolExecutor构造函数来创建线程池。

## JUC并发容器的分类
java.util.concurrent 包提供的容器（Queue、List、Set）、Map，

从命名上可以大概区分为 Concurrent、CopyOnWrite和 Blocking* 等三类，

同样是线程安全容器，可以简单认为：

	> Concurrent 类型没有类似 CopyOnWrite 之类容器相对较重的修改开销。
	
	> Concurrent 往往提供了较低的遍历一致性。
	所谓的弱一致性，例如，当利用迭代器遍历时，如果容器发生修改，迭代器仍然可以继续进行遍历。

	> 弱一致性的另外一个体现是，size 等操作准确性是有限的，未必是 100% 准确。

	> 与弱一致性对应的，就是同步容器常见的行为“fast-fail”，
	也就是检测到容器在遍历过程中发生了修改，则抛出 ConcurrentModificationException，不再继续遍历。


## 问：并发包中的 ConcurrentLinkedQueue 和 LinkedBlockingQueue 有什么区别？
	Concurrent 类型基于 lock-free，在常见的多线程访问场景，一般可以提供较高吞吐量。

	而 LinkedBlockingQueue 内部则是基于锁，并提供了 BlockingQueue 的等待性方法。

---
# 同步工具类
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
## 线程安全的队列实现
![](img/queue.png)

## 选择队列考量的依据
	1、队列容量是否有界
	2、是否为阻塞队列 
		*BlockingQueue
	3、是否为高并发队列
		Concurrent*

## 队列有界性的问题
	BlockingQueue 是否有界（Bounded、Unbounded），这一点也往往会影响我们在应用开发中的选择。

	> ArrayBlockingQueue 是最典型的的有界队列，其内部以 final 的数组保存数据，
	数组的大小就决定了队列的边界，所以我们在创建 ArrayBlockingQueue 时，都要指定容量，如
		public ArrayBlockingQueue(int capacity, boolean fair)

	> LinkedBlockingQueue容易被误解为无边界，但其实其行为和内部代码都是基于有界的逻辑实现的，
	只不过如果我们没有在创建队列时就指定容量，
	那么其容量限制就自动被设置为Integer.MAX_VALUE，成为了无界队列。

	> SynchronousQueue，这是一个非常奇葩的队列实现，
	每个删除操作都要等待插入操作，反之每个插入操作也都要等待删除动作。
	那么这个队列的容量是多少呢？
		是 1 吗？其实不是的，其内部容量是 0。

	> PriorityBlockingQueue 是无边界的优先队列
	虽然严格意义上来讲，其大小总归是要受系统资源影响。
	
	> DelayedQueue 和 LinkedTransferQueue 同样是无边界的队列。
	对于无边界的队列，有一个自然的结果，就是 put 操作永远也不会发生阻塞等待的情况。


## ConcurrentLinkedQueue
	类似 ConcurrentLinkedQueue 等，则是基于 CAS 的无锁技术，
	不需要在每个操作时使用锁，所以扩展性表现要更加优异。

## SynchronousQueue
	在 Java 6 中，SynchronousQueue的实现发生了非常大的变化，
	利用 CAS 替换掉了原本基于锁的逻辑，同步开销比较小。
	
	> 它是 Executors.newCachedThreadPool() 的默认队列。

	CacheThreadPool会确保随时都有空闲线程等待处理任务，因此选择SynchronousQueue正好符合场景需求。

## Deque 双端队列
	Deque 的侧重点是支持对队列头尾都进行插入和删除，所以提供了特定的方法，如:
	尾部插入时需要的
		addLast(e)、offerLast(e)
	尾部删除所需要的
		removeLast()、pollLast()

## LinkedBlockingQueue 与 ArrayBlockingQueue 的区别
	ArrayBlockingQueue 其条件变量与 LinkedBlockingQueue 版本的实现是有区别的。

	ArrayBlockingQueue内部的notEmpty、notFull 都是同一个再入锁的条件变量，

	而 LinkedBlockingQueue 则改进了锁操作的粒度，头、尾操作使用不同的锁，
	所以在通用场景下，它的吞吐量相对要更好一些。


## 比较 LinkedBlockingQueue、ArrayBlockingQueue、SynchronousQueue 的特点
	考虑应用场景中对队列边界的要求：
	ArrayBlockingQueue 是有明确的容量限制的；
	LinkedBlockingQueue 则取决于我们是否在创建时指定；
	SynchronousQueue 则干脆不能缓存任何元素。

	从空间利用角度：
	数组结构的 ArrayBlockingQueue 要比 LinkedBlockingQueue 紧凑，
	因为其不需要创建所谓节点，但是其初始分配阶段就需要一段连续的空间，所以初始内存需求更大。

	通用场景中，LinkedBlockingQueue 的吞吐量一般优于 ArrayBlockingQueue，
	因为它实现了更加细粒度的锁操作(队头和队尾使用不同的lock.condition)。

	ArrayBlockingQueue 实现比较简单，性能更好预测，属于表现稳定的“选手”。

	如果我们需要实现的是两个线程之间接力性（handoff）的场景，可能会选择 CountDownLatch，
	但是SynchronousQueue也是完美符合这种场景的，而且线程间协调和数据传输统一起来，代码更加规范。
	
	可能令人意外的是，很多时候 SynchronousQueue 的性能表现，往往大大超过其他实现，
	尤其是在队列元素较小的场景。
	

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


## CopyOnWrite 底层机制
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
	