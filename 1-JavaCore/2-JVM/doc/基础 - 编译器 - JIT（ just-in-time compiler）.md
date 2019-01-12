## 解释执行 VS JIT

非JIT执行方式

	java source code -> compiler -> bytecode -> JVM -> translate every time -> direct machine code

JIT执行方式

	java source code -> compiler -> bytecode -> JVM -> JIT -> direct machine code -> Speed Up!!!

## 解释执行模式
-Xint  字节码解释执行模式，该模式下JVM不会将字节码预先编译为可直接执行的机器码。

Runs the application in interpreted-only mode. 

Compilation to native code is disabled, and all bytecode is executed by the interpreter. 

The performance benefits offered by the just in time (JIT) compiler are not present in this mode.

## JIT 模式

In the Java programming language and environment, a just-in-time (JIT) compiler is a program that turns Java bytecode (a program that contains instructions that must be interpreted) into instructions that can be sent directly to the processor. 

After you've written a Java program, the source language statements are compiled by the Java compiler into bytecode rather than into code that contains instructions that match a particular hardware platform's processor (for example, an Intel Pentium microprocessor or an IBM System/390 processor). 

The bytecode is platform-independent code that can be sent to any platform and run on that platform.

JIT编译器会将平台独立的字节码，针对当前所在平台进行再次编译，生成在该平台可直接运行的指令。


In the past, most programs written in any language have had to be recompiled, and sometimes, rewritten for each computer platform. 

One of the biggest advantages of Java is that you only have to write and compile a program once. 

The Java on any platform will interpret the compiled bytecode into instructions understandable by the particular processor. 

However, the virtual machine handles one bytecode instruction at a time. 

Using the Java just-in-time compiler (really a second compiler) at the particular system platform compiles the bytecode into the particular system code (as though the program had been compiled initially on that platform). 

Once the code has been (re-)compiled by the JIT compiler, it will usually run more quickly in the computer.

The just-in-time compiler comes with the virtual machine and is used optionally. 

It compiles the bytecode into platform-specific executable code that is immediately executed. 

Sun Microsystems suggests that it's usually faster to select the JIT compiler option, especially if the method executable is repeatedly reused.