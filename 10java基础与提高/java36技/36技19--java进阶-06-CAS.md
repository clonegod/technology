# JUC 并发包的底层实现机制: CAS + AQS

---
## CAS - lock free 
	CAS 是 Java 并发中所谓 lock-free 机制的基础。
	
	大多数情况下，Java 开发者并不需要直接利用 CAS 代码去实现线程安全容器等，
	更多是通过并发包等间接享受到 lock-free 机制在扩展性上的好处。

#### AtomicInteger底层实现原理是什么？
	AtomicIntger 是对 int 类型的一个封装，提供原子性的访问和更新操作，
	其原子性操作的实现是基于 CAS（compare-and-swap）技术。

	所谓 CAS，表征的是一些操作的集合，获取当前数值，进行一些运算，利用 CAS 指令试图进行更新。
		如果当前数值未变，代表没有其他线程进行并发修改，则成功更新。
		否则，可能出现不同的选择，要么进行重试，要么就返回一个成功或者失败的结果。

	从 AtomicInteger 的内部属性可以看出，它依赖于 Unsafe 提供的一些底层能力，进行底层操作；
	以 volatile 的 value 字段，记录数值，以保证可见性。
#
	private static final jdk.internal.misc.Unsafe U = jdk.internal.misc.Unsafe.getUnsafe();
	private static final long VALUE = U.objectFieldOffset(AtomicInteger.class, "value");
	private volatile int value;

#
	具体的原子操作细节，可以参考任意一个原子更新方法，比如下面的 getAndIncrement。
	Unsafe 会利用 value 字段的内存地址偏移，直接完成操作。
	public final int getAndIncrement() {
	    return U.getAndAddInt(this, VALUE, 1);
	}

	因为 getAndIncrement 需要返归数值，所以需要添加失败重试逻辑。
	public final int getAndAddInt(Object o, long offset, int delta) {
	    int v;
	    do {
	        v = getIntVolatile(o, offset);
	    } while (!weakCompareAndSetInt(o, offset, v, v + delta));
	    return v;
	}
#	
	而类似 compareAndSet 这种返回 boolean 类型的函数，因为其返回值表现的就是成功与否，所以不需要重试。
	public final boolean compareAndSet(int expectedValue, int newValue)


#### CAS在底层是如何实现的？
	有的同学反馈面试官会问 CAS 更加底层是如何实现的，这依赖于 CPU 提供的特定指令，
	具体根据体系结构的不同还存在着明显区别。
	比如，x86 CPU 提供 cmpxchg 指令；
	而在精简指令集的体系架构中，则通常是靠一对儿指令（如“load and reserve”和“store conditional”）实现的，
	在大多数处理器上 CAS 都是个非常轻量级的操作，这也是其优势所在。

#### 如何在自己的产品代码中应用CAS操作？
Unsafe 属于内部API，一般不推荐直接使用Unsafe相关API。 那你有其它替代方案吗？
 
	目前 Java 提供了两种公共 API，可以实现这种 CAS 操作
	1、使用atomic包提供的原子更新工具类

	使用 java.util.concurrent.atomic.AtomicLongFieldUpdater，
	它是基于反射机制创建，我们需要保证类型和字段名称正确。
	原子数据类型和 Atomic*FieldUpdater，创建更加紧凑的计数器实现，以替代 AtomicLong。
	
	atomic 包下的LongAdder，在高度竞争环境下，可能就是比 AtomicLong 更佳的选择，尽管它的本质是空间换时间。

	2、 Java 9 以后，完全可以采用另外一种方式实现，也就是 Variable Handle API，这是源自于JEP 193.
	首先，获取相应的变量句柄，然后直接调用其提供的 CAS 方法。
	private static final VarHandle HANDLE = MethodHandles.lookup().findStaticVarHandle(AtomicBTreePartition.class, "lock");

	private void acquireLock(){
	    long t = Thread.currentThread().getId();
	    while (!HANDLE.compareAndSet(this, 0L, t)){
	        // wait some time to recheck
	        …
	    }
	}

	一般来说，我们进行的类似 CAS 操作，可以并且推荐使用 Variable Handle API 去实现，其提供了精细粒度的公共底层 API。
	我这里强调公共，是因为其 API 不会像内部 API 那样，发生不可预测的修改，这一点提供了对于未来产品维护和升级的基础保障。


###  [JEP 193](http://openjdk.java.net/jeps/193)


##### CAS 的副作用
	CAS 也并不是没有副作用，试想，其常用的失败重试机制，隐含着一个假设，即竞争情况是短暂的。
	大多数应用场景中，确实大部分重试只会发生一次就获得了成功，
	但是总是有意外情况，所以在有需要的时候，还是要考虑限制自旋的次数，以免过度消耗 CPU。

### CAS 下的 [ABA](https://en.wikipedia.org/wiki/ABA_problem)问题
	这是通常只在 lock-free 算法下暴露的问题。
	前面说过 CAS 是在更新时比较前值，如果对方只是恰好相同，
	例如期间发生了 A -> B -> A 的更新，仅仅判断数值是 A，可能导致不合理的修改操作。
	针对这种情况，Java 提供了 AtomicStampedReference 工具类，
	通过为引用建立类似版本号（stamp）的方式，来保证 CAS 的正确性，
	具体用法请参考这里:
		http://tutorials.jenkov.com/java-util-concurrent/atomicstampedreference.html


---
# AQS - 同步相关操作的公共抽象/并发包基础技术
理解为什么需要 AQS，如何使用 AQS，结合 JDK 源代码中的实践，理解 AQS 的原理与应用。

	Doug Lea 曾经介绍过 AQS 的设计初衷。
	从原理上，一种同步结构往往是可以利用其他的结构实现的，例如使用 Semaphore 实现互斥锁。
	但是，对某种同步结构的倾向，会导致复杂、晦涩的实现逻辑!
	所以，他选择了将基础的同步相关操作抽象在 AbstractQueuedSynchronizer 中，
	利用 AQS 为我们构建同步结构提供了范本。

### AQS (AbstractQueuedSynchronizer) 内部核心逻辑
	AQS 内部数据和方法，可以简单拆分为：
		一个 volatile 的整数成员表征状态，同时提供了 setState 和 getState 方法。
			private volatile int state;

		一个先入先出（FIFO）的等待线程队列，以实现多线程间竞争和等待，这是 AQS 机制的核心之一。

		各种基于 CAS 的基础操作方法，以及各种期望具体同步结构去实现的 acquire/release 方法。

	利用 AQS 实现一个同步结构，至少要实现两个基本类型的方法，分别是 
		acquire 操作，获取资源的独占权；
		release 操作，释放对某个资源的独占。

### AQS 在 ReentrantLock中的应用举例
以 ReentrantLock 为例，它内部通过扩展 AQS 实现了 Sync 类型，以 AQS 的 state 来反映锁的持有情况。

	private final Sync sync;
	abstract static class Sync extends AbstractQueuedSynchronizer { …}

	下面是 ReentrantLock 对应 acquire 和 release 操作，
	如果是 CountDownLatch 则可以看作是 await()/countDown()，具体实现也有区别。

	public void lock() {
	    sync.acquire(1);
	}
	public void unlock() {
	    sync.release(1);
	}

#
排除掉一些细节，整体地分析 acquire 方法逻辑，其直接实现是在 AQS 内部，
调用了 tryAcquire 和 acquireQueued，这是两个需要搞清楚的基本部分。

	public final void acquire(int arg) {
	    if (!tryAcquire(arg) &&
	        acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
	        selfInterrupt();
	}

#
首先，我们来看看 tryAcquire。在 ReentrantLock 中，tryAcquire 逻辑实现在 NonfairSync 和 FairSync 中，分别提供了进一步的非公平或公平性方法，而 AQS 内部 tryAcquire 仅仅是个接近未实现的方法（直接抛异常），这是留个实现者自己定义的操作。

我们可以看到公平性在 ReentrantLock 构建时如何指定的，具体如下：

	public ReentrantLock() {
        sync = new NonfairSync(); // 默认是非公平的
    }
    public ReentrantLock(boolean fair) {
        sync = fair ? new FairSync() : new NonfairSync();
    }

#
以非公平的 tryAcquire 为例，其内部实现了如何配合状态与 CAS 获取锁，注意，对比公平版本的 tryAcquire，它在锁无人占有时，并不检查是否有其他等待者，这里体现了非公平的语义。	

	final boolean nonfairTryAcquire(int acquires) {
	    final Thread current = Thread.currentThread();
	    int c = getState();// 获取当前 AQS 内部状态量
	    if (c == 0) { // 0 表示无人占有，则直接用 CAS 修改状态位，
	        if (compareAndSetState(0, acquires)) {// 不检查排队情况，直接争抢
	            setExclusiveOwnerThread(current);  // 并设置当前线程独占锁
	            return true;
	        }
	    } else if (current == getExclusiveOwnerThread()) { // 即使状态不是 0，也可能当前线程是锁持有者，因为这是再入锁
	        int nextc = c + acquires;
	        if (nextc < 0) // overflow
	            throw new Error("Maximum lock count exceeded");
	        setState(nextc);
	        return true;
	    }
	    return false;
	}

#
接下来我再来分析 acquireQueued，如果前面的 tryAcquire 失败，代表着锁争抢失败，进入排队竞争阶段。

这里就是我们所说的，利用 FIFO 队列，实现线程间对锁的竞争的部分，算是是 AQS 的核心逻辑。

当前线程会被包装成为一个排他模式的节点（EXCLUSIVE），通过 addWaiter 方法添加到队列中。

acquireQueued 的逻辑，简要来说，就是如果当前节点的前面是头节点，则试图获取锁，一切顺利则成为新的头节点；否则，有必要则等待，具体处理逻辑请参考我添加的注释。

	final boolean acquireQueued(final Node node, int arg) {
	      boolean interrupted = false;
	      try {
	        for (;;) {// 循环
	            final Node p = node.predecessor();// 获取前一个节点
	            if (p == head && tryAcquire(arg)) { // 如果前一个节点是头结点，表示当前节点合适去 tryAcquire
	                setHead(node); // acquire 成功，则设置新的头节点
	                p.next = null; // 将前面节点对当前节点的引用清空
	                return interrupted;
	            }
	            if (shouldParkAfterFailedAcquire(p, node)) // 检查是否失败后需要 park
	                interrupted |= parkAndCheckInterrupt();
	        }
	       } catch (Throwable t) {
	        cancelAcquire(node);// 出现异常，取消
	        if (interrupted)
	                selfInterrupt();
	        throw t;
	      }
	}

到这里线程试图获取锁的过程基本展现出来了，tryAcquire 是按照特定场景需要开发者去实现的部分，而线程间竞争则是 AQS 通过 Waiter 队列与 acquireQueued 提供的，在 release 方法中，同样会对队列进行对应操作。