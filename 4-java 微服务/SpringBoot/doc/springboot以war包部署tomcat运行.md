
## springboot打包为war，部署tomcat，如何指定profile？

	[windows]
	在$TOMCAT_HOME/bin下，新建setenv.bat，配置：
		set "spring.profiles.active=dev"

	[linux]
	在$TOMCAT_HOME/bin下，新建setenv.sh，配置：
		spring.profiles.active=dev


