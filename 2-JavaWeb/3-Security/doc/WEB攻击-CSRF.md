# Cross-site request forgery 

## [跨站请求伪造-WIKI](https://en.wikipedia.org/wiki/Cross-site_request_forgery)

## 攻击原理

假设你经常使用bank.example.com进行网上转账，在你提交转账请求时bank.example.com的前端代码会提交一个HTTP请求:

	POST /transfer HTTP/1.1
	Host: bank.example.com
	cookie: JsessionID=randomid; Domain=bank.example.com; Secure; HttpOnly
	Content-Type: application/x-www-form-urlencoded
	
	amount=100.00&routingNumber=1234&account=9876

你图方便**没有登出**bank.example.com，随后又访问了一个恶意网站，该网站的HTML页面包含了这样一个表单：

	<form action="https://bank.example.com/transfer" method="post">
	    <input type="hidden" name="amount" value="100.00"/>
	    <input type="hidden" name="routingNumber" value="evilsRoutingNumber"/>
	    <input type="hidden" name="account" value="evilsAccountNumber"/>
	    <input type="submit" value="点击就送!"/>
	</form>

你被“点击就送”吸引了，当你点了提交按钮时你已经向攻击者的账号转了100元。现实中的攻击可能更隐蔽，恶意网站的页面可能使用Javascript自动完成提交。尽管恶意网站没有办法盗取你的session cookie（从而假冒你的身份），但恶意网站向bank.example.com发起请求时，你的cookie会被自动发送过去。



## 预防CSRF攻击

#### 1、[Using Spring Security CSRF Protection](https://docs.spring.io/spring-security/site/docs/current/reference/html/csrf.html)
使用 spring security 提供的解决方案




### 2、JWT，每次请求在HTTP header上附加jwt作为token

