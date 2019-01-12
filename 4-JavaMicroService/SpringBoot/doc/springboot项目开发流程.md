## SpringBoot项目开发一般性流程
	【数据层】
		DataSource配置： 连接池
		
	【服务层】
		事务控制：@Transactional，rollbackFor=RuntimeException/Error
		一个业务操作封装为一个service方法，统一事务入口

	【缓存】
		Redis: RedisTemplate

	【WEB层】
		Filter、Interceptor
		错误处理：统一错误页面
		安全：Spring Security
		用户登录、登出
		
	【页面】
		模板：Thymeleaf
		CSS: BootStrap
		JS: JQuery

	【功能】
		登陆页面:LoginPage
		导航菜单：NavBar
		子页面：表单Form，表格Table