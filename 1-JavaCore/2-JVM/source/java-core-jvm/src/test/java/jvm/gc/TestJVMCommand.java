package jvm.gc;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.IntStream;

public class TestJVMCommand {
	/**
	  
-Xms20m -Xmx20m -Xmn10m -XX:SurvivorRatio=8 
-XX:+PrintCommandLineFlags -XX:+PrintFlagsFinal 
-XX:+PrintGCDetails -Xloggc:/tmp/logs/gc.log
	  
	 */
	public static void main(String[] args) throws Exception {
		while(true) {
			MakeGCBusy.start();
			Thread.sleep(100);
		}
	}
	
	private static class MakeGCBusy {
		
		private static final Collection<Object> leak = new ArrayList<Object>();
		
		public static void start() {
			IntStream.rangeClosed(1, 100).forEach(n -> {
				try {
					Date date = new Date();
					leak.add(new byte[1024*1024]);
					System.out.println(
							MessageFormat.format("{0,date,yyyy-MM-dd HH:mm:ss.SSS},{1}", date,
									Thread.currentThread().getName())
							);
				} catch (OutOfMemoryError e) {
					leak.clear();
				}
			});
		}
	}

}
