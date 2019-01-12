## JDK 、JRE 、 JVM

![](img/jdk.png)

# JDK
	JDK 包含 JRE 和 JVM
	java 开发工具包，在JRE的基础上，提供了监控JVM内存的相关工具/命令。

# JRE
	JRE 包含 JVM
	为Java程序提供运行时环境，比如rt.jar等核心依赖包

# JVM
	运行在底层操作系统上，屏蔽不同平台操作系统(windows, linux)的区别，为上层提供统一的编程接口。
	JVM 为不同操作系统平台定制专门的版本，使得Java能够实现一次编写，到处运行的能力！


---

## Java Overview
Java is a programming language and computing platform first released by Sun Microsystems in 1995. 
It is the underlying technology that powers Java programs including utilities, games, and business applications. 
Java runs on more than 850 million personal computers worldwide, and on billions of devices worldwide, including mobile and TV devices. Java is composed of a number of key components that, as a whole, create the Java platform.

## Java Runtime Edition
When you download Java, you get the Java Runtime Environment (JRE). 
The JRE consists of the Java Virtual Machine (JVM), Java platform core classes, and supporting Java platform libraries. 
All three are required to run Java applications on your computer. 
With Java 7, Java applications run as desktop applications from the operating system, as a desktop application but installed from the Web using Java Web Start, or as a Web Embedded application in a browser (using JavaFX).

## Java Programming Language
Java is an object-oriented programming language that includes the following features.
	
	  ● Platform Independence - Java applications are compiled into bytecode which is stored in class files and loaded in a JVM. Since applications run in a JVM, they can be run on many different operating systems and devices.
	  ● Object-Oriented - Java is an object-oriented language that take many of the features of C and C++ and improves upon them.
	  ● Automatic Garbage Collection - Java automatically allocates and deallocates memory so programs are not burdened with that task.
	  ● Rich Standard Library - Java includes a vast number of premade objects that can be used to perform such tasks as input/output, networking, and date manipulation.

## Java Development Kit
The Java Development Kit (JDK) is a collection of tools for developing Java applications. 

With the JDK, you can compile programs written in the Java Programming language and run them in a JVM. 

In addition, the JDK provides tools for packaging and distributing your applications.

The JDK and the JRE share the Java Application Programming Interfaces (Java API). The Java API is a collection of prepackaged libraries developers use to create Java applications. 

The Java API makes development easier by providing the tools to complete many common programming tasks including string manipulation, date/time processing, networking, and implementing data structures (e.g., lists, maps, stacks, and queues).

## Java Virtual Machine
The Java Virtual Machine (JVM) is an abstract computing machine. The JVM is a program that looks like a machine to the programs written to execute in it. This way, Java programs are written to the same set of interfaces and libraries. Each JVM implementation for a specific operating system, translates the Java programming instructions into instructions and commands that run on the local operating system. This way, Java programs achieve platform independence.

The first prototype implementation of the Java virtual machine, done at Sun Microsystems, Inc., emulated the Java virtual machine instruction set in software hosted by a handheld device that resembled a contemporary Personal Digital Assistant (PDA). Oracle's current implementations emulate the Java virtual machine on mobile, desktop and server devices, but the Java virtual machine does not assume any particular implementation technology, host hardware, or host operating system. It is not inherently interpreted, but can just as well be implemented by compiling its instruction set to that of a silicon CPU. It may also be implemented in microcode or directly in silicon.
The Java virtual machine knows nothing of the Java programming language, only of a particular binary format, the class file format. A class file contains Java virtual machine instructions (or bytecodes) and a symbol table, as well as other ancillary information.

For the sake of security, the Java virtual machine imposes strong syntactic and structural constraints on the code in a class file. However, any language with functionality that can be expressed in terms of a valid class file can be hosted by the Java virtual machine. Attracted by a generally available, machine-independent platform, implementors of other languages can turn to the Java virtual machine as a delivery vehicle for their languages. 



