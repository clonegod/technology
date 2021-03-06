# Flux 会取代 Web MVC，或可不再基于 Servlet 容器了?

对 Java 开发者来说，2017 年 9 月是个热闹的月份不但 Java SE 9、Java EE 8 相继发布，就连 Spring 框架，也在这段时间发布 5.0 正式版。

而新版 Spring 的一大特色，就是 Reactive Web 方案 Web Flux，这是用来替代 Spring Web MVC 的吗？或者，只是终于可以不再基于 Servlet 容器了？

## 基于 Servlet 容器的 Web MVC

身为 Java 开发者，对于 Spring 框架并不陌生。它起源于 2002 年、Rod Johnson 著作《Expert One-on-One J2EE Design and Development》中的 Interface 21 框架，到了 2004 年，推出 Spring 1.0，从 XML 到 3.0 之后，支持 JavaConfig 设定；进一步，在 2014 年时，除了 Spring 4.0 之外，首次发表了Spring Boot，最大的亮点是采用自动组态，令基于 Spring 的快速开发成为可能。

对 Web 开发者来说，Spring 中的 Web MVC 框架，也一直随着 Spring 而成长，然而由于基于 Servlet 容器，早期被批评不易测试（例如：控制器中包含了 Servlet API）。

不过，从实操 Controller 介面搭配 XML 设定，到后来的标注搭配 JavaConfig，Web MVC 使用越来越便利。如果愿意，也可采用渐进的方式，将基于 Servlet API 的 Web 应用程序，逐步重构为几乎没有 Servlet API 的存在，在程序代码层面达到屏蔽 Servlet API 的效果。

由于不少 Java 开发者的 Web 开发经验，都是从 Servlet 容器中累积起来的，在这个时候，Web MVC 框架基于 Servlet API，就会是一项优点。因为，虽然运用 Web MVC 编写程序时，可做到不直接面对 Servlet API，然而，也意味着更强烈地受到 Spring 的约束，有时则是无法在设定或 API 中找到对应方案，有时也因为心智模型还是挂在 Servlet 容器，经验上难以脱离，在搞不出 HttpSession、ServletContext 对应功能时，直接从 HttpSession、ServletContext 下手，毕竟也是个方法。

编写程序时，就算没用到 Servlet API，Web MVC 基于 Servlet 容器仍是事实，因为，底层还是得借助Servlet 容器的功能，例如 Spring Security，本质上还是基于 Servlet 容器的 Filter 方案。

然而在今日，Servlet 被许多开发者视为陈旧、过时技术的象征，或许是因为这样，在 Java EE 8 宣布推出的这段期间，当在某些场合谈及 Servlet 4.0 之时，总会听到有人提出“Web Flux 可以脱离 Servlet 了”之类的建议。

## 实现 Reactive Streams 的 Reactor

Web Flux 不依赖 Servlet 容器是事实，然而，在谈及 Web Flux 之前，我们必须先知道 Reactor 项目，它是由 Pivotal 公司，也就是目前 Spring 的拥有者推出，实现了 Reactive Streams 规范，用来支持 Reactive Programming 的实作品。

既然是实现了 Reactive Streams 规范，开发者必然会想到的是 RxJava/RxJava 2，或者是 Java 9 的 Flow API。这也意谓着，在能使用 Web Flux 之前，开发者必须对于 Reactive Programming 典范，有所认识。

开发者这时有疑问了，Spring 为何不直接基于 RxJava 2，而是打造专属的 Reactive Streams 项目呢？

就技术而言，Reacto r是在 Java 8 的基础上开发，并全面拥抱 Java 8 之后的新 API，像是 Lambda 相关介面、新日期与时间 API 等，这意谓着，项目如果还是基于 Java 7 或更早版本，就无法使用 Reactor。

在 API 层面，RxJava 2 有着因为历史发展脉络的原因，不得不保留一些令人容易困惑或混淆的型态或操作，而 Reactor 在这方面，都有着明确的对应 API 来取代，然而，却也提供与 RxJava 2（甚至是 Flow API）间的转换。

另一方面，Reactor 较直觉易用，例如最常介绍的 Mono 与 Flux，实现了 Reactive Streams 的 Publisher界面，并简化了信息发布，让开发者在许多场合，不用处理 Subscriber 和 Subscription 的细节（当然，这些在 Reactor 也予以实现）。而在 Spring Web Flux 中，Mono 与 Flux 也是主要的操作对象。想知道如何使用Mono与Flux，可以参考〈使用 Reactor 进行反应式编程〉（https://goo.gl/vc2fGc）。

## 又一个 Web 框架？

到了 Spring 5，在 Reactor 的基础上，新增了 Web Flux 作为 Reactive Web 的方案，我们在许多介绍文件的简单示例，例如〈使用 Spring 5 的 WebFlux 开发反应式 Web 应用〉（https://goo.gl /G5uotZ），就看到当中使用了 Flux、Mono 来示范，而且，程序的代码看起来就像是 Spring MVC。

这是因为 Web Flux 提供了基于 Java 标注的方式，有许多 Web MVC 中使用的标注，也拿来用在 Web Flux 之中，让熟悉 Web MVC 的开发者也容易理解与上手 Web Flux，然而，这不过就是新的 Web 框架吗？

实际上，当然不是如此。Web Flux 并不依赖 Web MVC，而且它是基于 Reactor，本质属于非同步、非阻断、Reactive Programming 的心智模型，也因此，如果打算将 Web Flux 运行在 Servlet 容器之上，必须是支持 Servlet 3.1 以上，因为才有非阻断输入输出的支持，虽然 Web Flux 的 API 在某些地方，确实提供了阻断的选项，若单纯只是试着将基于 Web MVC 的应用程序，改写为套用 Web Flux，并不会有任何益处，反而会穷于应付如何在 Web Flux 实现对应的方案。

例如，此时，Spring Security 显然就不能用了，毕竟是 Spring 基于 Servlet 的安全方案，开发者必须想办法套用 Spring Security Reactive；而且，在储存方案上，也不是直接采用 Spring Data，而是 Spring Data Reactive 等。

就算能套用相关的设定与 API，要能获得 Web Flux 的益处，应用程序中相关的元件，也必须全面检视，重新设计为非阻断、基于 Reactive Programming 方式，这或许才是最困难、麻烦的部份。

除了基于 Java 标注的方式，让熟悉 Web MVC 的开发者容易理解之外，Web Flux 还提供了基于函数式的设计与组态方式。

实际上，在运用 RxJava 2/Reacto r等 Reactive Streams 的实操时，我们也都必须熟悉函数式的思考方式，才能充分掌握，这点在 Web Flux 并不例外。

## 可以脱离 Servlet 容器了？

Servlet 容器是个旧时代的象征，如果能够屏蔽 Servlet 容器或相关 API，许多开发者应该都会很开心，可以少一层抽象，不必使用肥肥的 Servlet 容器，当然会是使用 Web Flux 时附带的优点，然而，如果只是为了屏蔽 Servlet，其实，早就有其他技术选择存在。

基于 Servlet 一路发展过来的 Web MVC，虽然目前在某些地方可以安插一些函数式的设计，然而，本质上不变的部分在于，在技术堆叠中所隐含的，仍是一个基于同步、阻断式、命令式的心智模型。如果在这样的堆叠中，开发者老是因为想要实现非同步、非阻断、Reactive、函数式而感到不快，Web Flux 也许才会是可考虑的方案，而不单只是用来作为脱离 Servlet 容器，Web MVC 的替代品。

整体而言，Web Flux 还算是新技术，也还有待时间验证可行性，如果只是为了想用 Web Flux 来取代 Web MVC，或者更小一点的野心，只是想要能脱离 Servlet 容器，最好在采取行动之前，全面检视一下，确认自身或团队成员是否准备好接受 Web Flux 的心智模型，或者真的存在着对应的应用场景吧。

