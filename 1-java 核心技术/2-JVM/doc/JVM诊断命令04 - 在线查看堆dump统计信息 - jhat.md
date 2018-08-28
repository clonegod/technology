## jhat - JVM Heap Analysis Tool 在线查看dump堆文件的内容

Heap Dump Browser - Starts a web server on a heap dump file (for example, produced by jmap -dump), allowing the heap to be browsed.

JVM Heap Analysis Tool命令是与jmap搭配使用，用来分析jmap生成的dump，jhat内置了一个微型的HTTP/HTML服务器，生成dump的分析结果后，可以在浏览器中查看。

要注意，一般不会直接在服务器上进行分析，因为jhat是一个耗时并且耗费硬件资源的过程，一般把服务器生成的dump文件复制到本地或其他机器上进行分析。

[参考文档](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jhat.html)

---

