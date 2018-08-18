package week08.homework

import java.util.concurrent.CountDownLatch

object CdlTest extends App {
  val cdl = new CountDownLatch(1)
  new Thread(new Runnable() {
    def run() {
      Thread.sleep(1000)
      cdl.countDown()
    }
  }).start();
  
  cdl.await()
  
  println("over")
}