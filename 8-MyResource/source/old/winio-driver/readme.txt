【winio官网】
http://www.internals.com/utilities/WinIo.zip

=======================================================================================

【参考示例】
wio相关\winIo-demo-original.zip
	包含2个demo： 1个是用jnative来驱动winio的(仅支持winio32),另一个是通过jna来驱动的，支持winio32和winio64
	
=======================================================================================
【windows虚拟机下运行winio】
	Cannot Initialize the WinIO
	解决办法：以管理员身份运行命令行cmd窗口，执行java -jar 。

启动脚本：start.bat
echo off
echo java -version
pause

java -jar c:\users\huang\winio.jar

pause


开发过程中winio可能遇到的其它问题；
1. winio.dll与winio.sys没有复制到jdk/bin目录下。
2. 程序中使用/加载的winio与jdk位数不一致：
	wino32.dll+winio32.sys 需要使用32位jdk
	wino64.dll+winio64.sys 需要使用64位jdk
3. 使用winio64位驱动，需要将winio64.sys的证书导入到系统，并将系统设置为开发模式
	
=======================================================================================
winio分为：winio32和winio64两种
	winio32.dll+winio32.sys
	winio64.dll+winio64.sys
	
java调用winio有几种方式：
1. 使用jna调用（支持32bit和64bit） --- 推荐此方法
2. 使用jnative调用（仅支持32bit的winio）
3. 使用JNI调用，可能比较麻烦


【winio具体使用】

[jna调用winio32]
1. 使用 32bit JDK
2. 复制WinIo32.dll,WinIo32.sys到32位jdk/bin目录下

[jna调用winio64]
1. 使用 64bit JDK
2. 复制WinIo64.dll,WinIo64.sys到64位jdk/bin目录下

注意：
64位的winio需要修改windows为开发模式,并对驱动进行签名（导入过期证书）
bcdedit /set testsigning on		#开启调试模式
bcdedit /set testsigning off	#关闭调试模式


[jnative调用winio32]
1. 使用 32bit JDK
2. 引入jnative.jar
3. 复制WinIo32.dll,WinIo32.sys到32位jdk/bin目录下

=======================================================================================

selenium - http://selenium-release.storage.googleapis.com/index.html?path=2.50/
1. ie驱动分32位和64位,根据需要从官网下载与所用selenium版本一致的ie驱动；
2. 比如，32位ie驱动，则需将IEDriverServer.exe复制到IE安装目录 C:\Program Files (x86)\Internet Explorer
	64位ie驱动，则复制到IE安装目录 C:\Program Files\Internet Explorer

=======================================================================================	
		
IE浏览器版本
	最好用IE8，更高版本未测试
