### Java8从函数式编程中引入的两个核心思想：
    将方法和Lambda作为一等值，以及在没有可变共享状态时，函数或方法可以有效、安全地并行执行。

    
### 行为参数化---把代码传递给方法的简洁方式：
    方法引用、Lambda、接口中的默认方法
    将代码块作为参数传递给另一个方法，稍后再去执行它。这样，这个方法的行为就基于那块代码被参数化了。
        将一个lambda表达式传递给方法的Predicate参数，完成集合的过滤。

### 将代码传递给方法的功能 =》函数式编程
    没有共享的可变数据
    将方法和函数即代码传递给其他方法的能力
    函数 --- 值的一种新形式

#
	面向值编程：传递给方法的参数是值/对象
	面向行为编程：传递给方法的参数是一个函数
    
-----------------------------------------
    
### Lambda表达式  --- 将使用1次的某个简单逻辑，用匿名函数来实现。
    如果代码逻辑很长，则不建议使用lambda表达式，而应该将该段逻辑封装到一个方法中。
    只有在接受函数式接口的地方才可以使用Lambda表达式。
        button.setOnAction((ActionEvent event) -> label.setText("Sent!!"));
        Thread t = new Thread(() -> System.out.println("Hello world"));
        inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));

### 函数式接口 @FunctionalInterface
    只有一个抽象方法的接口。
    接口中哪怕有很多默认方法，只要接口只定义了一个抽象方法，它就仍然是一个函数式接口。
    Lambda表达式允许你直接以内联的形式为函数式接口的抽象方法提供实现，并把整个表达式作为函数式接口的实例。
    Comparable、Runnable和Callable，Predicate、Consumer、Supplier和Function

### 方法引用  --- Lambda表达式的语法糖，比lambda更简洁的写法
    重复使用现有的方法定义，并像Lambda一样传递它们
    使用"::"符号来引用类的方法
    比如：File[] hiddenFiles = new File("").listFiles(File::isHidden);

### 接口中的默认方法 --- 让接口和库的演变更顺畅、编译更少。
    改变已发布的接口而不破坏已有的实现: 向接口中添加新的接口方法，并提供默认实现，这样就不会对原来的那些实现类造成影响。
    
### 接口中的静态方法

    
-----------------------------------------

### 流 - Stream
    Stream的流式编程风格处理Collection集合中的数据
        stream, filter, map, reduce, collect(groupBy())
        Collections <-> Streams
        
    Stream的并行性
        Java 8可以进行多核处理器上的并行编程，依赖于函数式编程：函数，值的一种新的表现形式
        多核CPU上执行，对大数据提供（数据分块）并行处理，透明地把输入的不相关部分拿到几个CPU内核上去分别执行
        注意：并行只有在假定你的代码的多个副本可以独立工作时才能进行，也就是说不能发生多线程并发修改共享数据的问题。
        
        
        
    
    
    
    
    
    


