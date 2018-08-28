## 第1种方式： Maven插件生成可执行jar （推荐）
	https://www.baeldung.com/executable-jar-with-maven

### 1、jar与依赖包相分离
	好处：生成的jar仅包含源码编译后的字节码，依赖包则被复制到独立的目录中进行集中管理。

	# 首先，将依赖包复制到libs目录中
	<plugin>
	    <groupId>org.apache.maven.plugins</groupId>
	    <artifactId>maven-dependency-plugin</artifactId>
	    <executions>
	        <execution>
	            <id>copy-dependencies</id>
	            <phase>prepare-package</phase>
	            <goals>
	                <goal>copy-dependencies</goal>
	            </goals>
	            <configuration>
	                <outputDirectory>
	                    ${project.build.directory}/libs
	                </outputDirectory>
	            </configuration>
	        </execution>
	    </executions>
	</plugin>
	
	# 然后，将依赖包设置到MANIFEST的CLASS-PATH中，并设置MainClass启动入口类
	<plugin>
	    <groupId>org.apache.maven.plugins</groupId>
	    <artifactId>maven-jar-plugin</artifactId>
	    <configuration>
	        <archive>
	            <manifest>
	                <addClasspath>true</addClasspath>
	                <classpathPrefix>libs/</classpathPrefix>
	                <mainClass>clonegod.jar.JarExample</mainClass>
	            </manifest>
	        </archive>
	    </configuration>
	</plugin>


### 2、完整的jar
	
##### Apache Maven Assembly Plugin
	# 将依赖包一起打包到最终生成的jar中。
	<plugin>
		    <groupId>org.apache.maven.plugins</groupId>
		    <artifactId>maven-assembly-plugin</artifactId>
		    <executions>
		        <execution>
		            <phase>package</phase>
		            <goals>
		                <goal>single</goal>
		            </goals>
		            <configuration>
		                <archive>
		                <manifest>
		                    <mainClass>clonegod.jar.JarExample</mainClass>
		                </manifest>
		                </archive>
		                <descriptorRefs>
		                    <descriptorRef>jar-with-dependencies</descriptorRef>
		                </descriptorRefs>
		            </configuration>
		        </execution>
		    </executions>
		</plugin>


##### Spring Boot Maven Plugin
	# 将依赖包一起打包到最终生成的jar中。
	<plugin>
	    <groupId>org.springframework.boot</groupId>
	    <artifactId>spring-boot-maven-plugin</artifactId>
	    <executions>
	        <execution>
	            <goals>
	                <goal>repackage</goal>
	            </goals>
	            <configuration>
	                <classifier>spring-boot</classifier>
	                <mainClass>clonegod.jar.JarExample</mainClass>
	            </configuration>
	        </execution>
	    </executions>
	</plugin>

---

## 第2种方式： 手动打包

命令格式
	
	jar cvfe jarName.jar package.MainClass dir

	参数说明：
	> c  create new archive
	> v  generate verbose output on standard output
	> f  specify archive file name
	> e  specify application entry point for stand-alone application bundled into an executable jar file

比如，package为clonegod.jar的目录下，有两个类
	
	package clonegod.jar;
	public class JarExample {
		public static void main(String[] args) {
			Util.print("success");
		}
	}

	package clonegod.jar;
	public class Util {
		
		public static void print(String str) {
			System.out.println(str);
		}
	}

打开命令行，进入到classes目录下（注意执行命令时所在目录不要搞错了），执行

	# . 表示要打包的文件在当前目录下
	jar cvfe myjar.jar clonegod.jar.JarExample .
	
运行可执行jar

	java -jar myjar.jar


---

## 第3种方式： Eclipse 导出jar

In Eclipse you can do it simply as follows :

	Right click on your Java Project and select Export.

	Select Java -> Runnable JAR file -> Next.
	
	Select the Launch Configuration and choose project file as your Main class
	
	Select the Destination folder where you would like to save it and click Finish.

	另外，如果依赖其它的jar包，需要选择如何处理这些依赖包。
	三种处理方式：
		1、 将依赖的jar的package提取出来，放到生成的jar中。
		2、 将依赖的jar复制到生成的jar中，并在MANIFEST.MF中设置到Class-Path中。
		3、 将依赖的jar独立出来，复制到一个其它的目录中存放。