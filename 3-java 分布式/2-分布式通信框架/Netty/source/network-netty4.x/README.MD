# Netty 学习思路
	1、Netty从架构层面，将网络相关的IO操作，与业务相关的逻辑进行了分离，内部通过 配置相关的ChannelHandler来对IO事件进行处理。
	2、网络相关：
		IO模型： 非阻塞、事件驱动(os底层epoll)
		线程模型：bossGroup（负责建立连接）, workerGroup（负责建立连接后的数据读写操作） - reactor模型
		数据模型：编码、解码/ 序列化、反序列化
	3、业务相关：
		ChannelHandler链
			编码、解码
			数据读写、异常处理
		
# Netty4.x
	an asynchronous event-driven network application framework.
	基于nio的实现的高性能网络服务器，IO操作都是非阻塞的
	将网络相关的操作与业务相关的操作进行了分离
	数据序列化方式可灵活配置
	支持HTTP协议 - 可作为Web服务器，替代tomcat
	支持Websocket协议 - 与浏览器端js建立长连接，实现服务端主动推送数据到浏览器的功能
	
# Core Concepts - 非阻塞
	Netty is a non-blocking framework. 
	This leads to high throughput compared to blocking IO. 
	Understanding non-blocking IO is crucial to understanding Netty’s core components and their relationships.
	
## Channel - TCP连接，以及该连接上的读写操作
	Channel is the base of Java NIO. 
	It represents an open connection which is capable of IO operations such as reading and writing.
	
## ChannelFuture - 在channel的异步io操作上绑定监听器
	Every IO operation on a Channel in Netty is non-blocking.
	This means that every operation is returned immediately after the call. 
	Netty has its own ChannelFuture interface. 
	We can pass a callback to ChannelFuture which will be called upon operation completion.
	
	Netty对java原生Future扩展的原因：
		java原生Future，只能判断异步操作是否完成，或者是阻塞等待异步操作完成。对于Netty而言，不满足需求，因此对Future进行了扩展。

	interface ChannelFuture extends Future<Void>
		addListener(...) // 在Channel上绑定监听器，当异步io操作完成时被调用
		removeListener(...) // 在Channel上移除监听器，当异步io操作完成时被调用
		sync() // 当前线程以同步的方式等待异步io执行完成

## Events and Handlers - io事件和处理器
	Netty uses an event-driven application paradigm,
	so the pipeline of the data processing is a chain of events going through handlers. 	
	
	Events and handlers can be related to the inbound and outbound data flow. 
	
	> 接收数据
	Inbound events can be the following:
		Channel activation and deactivation	
		Read operation events 可读事件
		Exception events	  异常事件
		User events
	
	> 响应数据 
	Outbound events are simpler and, generally, are related to 
		opening/closing a connection and writing/flushing data.
	
	Netty applications consist of a couple of networking and application logic events and their handlers. 
	Netty 应用程序由网络相关事件、应用逻辑相关事件，以及它们各自所对应的处理器组成。
	
	通道事件处理器(ChannelHandler)
	The base interfaces for the channel event handlers are ChannelHandler 
	and its ancestors ChannelOutboundHandler and ChannelInboundHandler.
	
	Netty provides a huge hierarchy of implementations of ChannelHandler. 
	
	ChannelHandler - Adapter实现（空实现）
		ChannelInboundHandlerAdapter 
		ChannelOutboundHandlerAdapter
	
	SimpleChannelInboundHandler	仅处理单一类型的数据，比如字符串String---简化操作API的便捷类
		
	
	ChannelHandler - HTTP协议的相关实现
		HttpRequestDecoder，HttpResponseEncoder，HttpObjectAggregator
		
	
## Encoders and Decoders - 编码、解码 / 序列化、反序列化
	编码：将内存中的数据编码为指定格式的二进制流，以便在网络上传输（序列化）
	解码：将从网络上接收到的二进制流还原为原始内存，以便进行数据处理（反序列化）
	
	As we work with the network protocol, we need to perform data serialization and deserialization. 
	
	ByteToMessageDecoder 解码/反序列化
		Netty introduces special extensions of the ChannelInboundHandler for decoders which are capable of decoding incoming data. 
		
	MessageToByteEncoder 编码/序列化
		For encoding outgoing data, Netty has extensions of the ChannelOutboundHandler called encoders.
	
	

 