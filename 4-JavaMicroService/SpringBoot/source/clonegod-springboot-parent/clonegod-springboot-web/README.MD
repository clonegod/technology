# springboot 打war包时动态设置需要激活的profile

## Maven profiles
	<!-- 不同环境下，使用不同的值替换application。properties中的变量 -->
	<profiles>
		<profile>
			<id>dev</id>
			<properties>
				<!-- 自定义变量 -->
				<application.env>dev</application.env>
			</properties>
			<activation>
				<activeByDefault>true</activeByDefault>
			</activation>
		</profile>
		<profile>
			<id>sit</id>
			<properties>
				<!-- 自定义变量 -->
				<application.env>sit</application.env>
			</properties>
		</profile>
		<profile>
			<id>prod</id>
			<properties>
				<!-- 自定义变量 -->
				<application.env>prod</application.env>
			</properties>
		</profile>
	</profiles>

## Maven 插件: maven-resources-plugin
	<plugin>
		        <artifactId>maven-resources-plugin</artifactId>
		        <configuration>
					<encoding>${project.build.sourceEncoding}</encoding>
					<useDefaultDelimiters>false</useDefaultDelimiters>
					<delimiters>
						<delimiter>${*}</delimiter>
					</delimiters>
					<resources>
						<!-- 不过滤相关文件 -->
						<resource>
							<directory>src/main/resources</directory>
							<filtering>false</filtering>
							<excludes>
								<exclude>application.properties</exclude>
							</excludes>
						</resource>
						<!-- 仅过滤指定的文件 -->
						<resource>
							<directory>src/main/resources</directory>
							<filtering>true</filtering>
							<includes>
								<include>application.properties</include>
							</includes>
						</resource>
					</resources>
		        </configuration>
		    </plugin>


## springboot 配置文件 application.properties
	# 动态激活的环境
	spring.profiles.active=${application.env}
			    
		    
## 打包命令
	# -P 激活对应的profile
	mvn clean install -DskipTests -Pprod
	
			
