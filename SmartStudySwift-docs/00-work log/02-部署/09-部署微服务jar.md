部署时会把test文件夹里的所有测试案例跑一遍，避免出问题最好全删了
## 父模块
- pom.xml 
```xml
<build>  
    <pluginManagement>  
        <plugins>  
            <!--未解析的插件: 'org.apache.maven.plugins:maven-clean-plugin:3.2.0-->  
            <plugin>  
                <groupId>org.apache.maven.plugins</groupId>  
                <artifactId>maven-clean-plugin</artifactId>  
                <version>3.1.0</version>  
            </plugin>  
            <plugin>  
                <groupId>org.springframework.boot</groupId>  
                <artifactId>spring-boot-maven-plugin</artifactId>  
            </plugin>  
        </plugins>  
    </pluginManagement>  
</build>
```
## 需要打包成jar的模块
- pom.xml
```xml
<build>  
    <plugins>  
        <plugin>  
            <groupId>org.springframework.boot</groupId>  
            <artifactId>spring-boot-maven-plugin</artifactId>  
        </plugin>  
    </plugins>  
</build>
```

- 然后maven对父模块使用package
	- 如果有残留文件，需要clean,compile,package
![[Pasted image 20241127021545.png]]
- 打包完成后在子模块target子文件下就有jar了，上传到服务器即可
- 运行命令
```sh
java -Xms256m -Xmx512m -Dspring.profiles.active=prod -jar sss-keep-alive-0.0.1-SNAPSHOT.jar
```