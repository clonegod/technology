# netty-based-frameworks-comparison

-> http://florindragu.com/2015/10/netty-based-frameworks-comparison/

---
The huge increase in performance (both throughput and response times) for fully async http/rest frameworks is well documented, one example being [Techempower](https://www.techempower.com/benchmarks/).

I will focus on Java and Netty based frameworks even though Undertow is also async and performs amazingly well.

What I’m hoping to see is most of the following features:

Java8
Fully async (hopefully supporting Java8 CompletableFuture)
Easy to build RESTful or proxy style endpoints
Easy to customize (start/stop hooks, pipeline hooks)
Distributed tracing (Google Dapper or Twitter Zipkin style)
Error handling (map errors and exceptions to HTTP status codes and detailed messages)
Support for JSON serialization. Extensible for other serialization formats.
Support for request object validation and mapping to corresponding error codes
Plugins for metrics (codahale), dependency injection (guice/spring), config (typesafe/archaius), service registry (consul/eureka), rx and more
Support for configuration, packaging and image (docker/AMI/etc) creation

## Netty
Netty is a low level, extremely performant, general networking toolkit that provides the building blocks for all protocols or your own protocol. While building APIs or proxies with pure Netty is entirely possible it requires a lot of extra code that can be error prone and it is missing some of the features mentioned above to make it easier to write APIs, build the app and manage it.
Netty is more of a network protocol framework rather than an application framework, therefore most companies using Netty built an application framework on top of it or used one of the existing abstractions on top of Netty.


## RxNetty
RxNetty is the integration of RxJava and Netty. It is slightly more than a “reactive” layer over Netty. Everything else about Netty applies to RxNetty.
It is easy to convert between it and RX Observable to integrate RX constructs, even though Java8 lambdas and the streams API provide a similar set of features.

## Reactor
Reactor is a foundational library from Spring for building reactive fast-data applications on the JVM. It is an implementation of the Reactive Streams Specification. You can use Reactor to power an application that has a low tolerance for latency and demands extremely high throughput.
Reactor includes powerful abstractions for high-speed data ingestion via TCP. The TcpServer comes with a default implementation based on Netty 4 but is extensible and other implementations could be plugged in.
Reactor is a fairly young abstraction that allows the use of Netty under the hood, but it remains to be seen if it becomes popular.


## Karyon2
Karyon is a Netflix framework based on RxNetty that is the Netflix blueprint of what it means to implement a cloud ready web service. Karyon can be thought of as a nucleus that contains the following main ingredients.
Bootstrapping , dependency and Lifecycle Management (via Governator)
Runtime Insights and Diagnostics (via karyon-admin-web module)
Configuration Management (via Archaius)
Service discovery (via Eureka)
Powerful transport module via RxNetty
Karyon2 is a great choice for teams that want to use Rx and that are buying into the whole Netflix stack, as there is no easy way to pick and choose only certain parts.

## Ratpack
Ratpack is a set of Java libraries that facilitate fast, efficient, evolvable and well tested HTTP applications.
The core of Ratpack is made up of only Java 8, Netty, Google Guava and Reactive Streams.
You can write Ratpack applications in Java 8 or any alternative JVM language that plays well with Java. Specific support for the Groovy language is provided, utilizing the latest static compilation and typing features.
Ratpack does not take a heavily opinionated approach as to what libraries and build tools you should use to compose your application. As the developer of the application, you are in control. Direct integration of tools and libraries is favored over generic abstractions.
Ratpack looks very lightweight and extremely promising.


## RestExpress
RestExpress is a minimalist Java framework for rapidly creating scalable, containerless, RESTful microservices. Ship a production-quality, headless, RESTful API in the shortest time possible. Uses Netty for HTTP, Jackson for JSON, Metrics for metrics, properties files for configuration. Sub-projects and plugins enable, NoSQL, Swagger, Auth0, HAL integration, etc.
RESTExpress uses non-blocking I/O to service requests while leveraging Executors to service back-end (possibly blocking) operations, however it is not fully async (https://github.com/RestExpress/RestExpress/wiki/Frequently-Asked-Questions ) and
is still mainly based on Netty 3.9.x.there is an effort to upgrade to Netty 4.0.x. as the benefits of Netty 4 over 3 are well documented (https://blog.twitter.com/2013/netty-4-at-twitter-reduced-gc-overhead), but it is not clear if they will support fully async requests.