package thread;

import java.util.concurrent.TimeUnit;

public class DeadLockSample extends Thread {

	
	private String first;
	private String second;
	
	public DeadLockSample(String name, String first, String second) {
		super(name);
		this.first = first;
		this.second = second;
	}
	
	public void run() {
		synchronized(first) {
			System.out.println(this.getName() + " obtained: " + first);
			try {
				TimeUnit.SECONDS.sleep(1);
				synchronized(second) {
					System.out.println(this.getName() + " obtained: " + second);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		
		DeadLockDetection.checkDeadLock();
		
		String lockA = "lockA";
		String lockB = "lockB";
		
		Thread t1 = new DeadLockSample("Thread1", lockA, lockB);
		Thread t2 = new DeadLockSample("Thread2", lockB, lockA);
		
		t1.start();
		t2.start();
		
		t1.join();
		t2.join();
	}
}
