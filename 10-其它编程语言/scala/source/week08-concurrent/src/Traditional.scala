

object SynchronizedGuardBlocks extends App {
  val lock = new AnyRef
  
  var message:Option[String] = None
  
  val greet = new Thread(new Runnable {
    def run() {
      lock.synchronized {
        while(message == None) lock.wait()
        println(message get)
      }
    }
  })
  
  greet.start()

  lock.synchronized {
    message = Some("Hello")
    lock.notify()
  }
  
  greet.join()
  
}