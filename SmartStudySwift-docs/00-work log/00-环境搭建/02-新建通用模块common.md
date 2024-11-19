通用模块被所有子模块引入, 编写一些通用的代码, 引入一些通用的依赖
在通用模块中：
- 一般不直接注入类，可以编写一个基本配置类使用`@Bean`来注入，需要使用对应类的模块在自己模块编写一个配置子类使用`@Configuration`这样就可以按需引入而不是全部引入
步骤：
- 新建子模块`sss-common`
- 引入基本依赖
```xml
<dependencies>  
    <dependency>  
        <groupId>org.springframework.boot</groupId>  
        <artifactId>spring-boot-starter-web</artifactId>  
    </dependency>  
</dependencies>
```
---
- 父模块中定义`sss-common`的版本号
```xml
<properties>
	<!--子模块-->  
	<sss-common.version>0.0.1-SNAPSHOT</sss-common.version>
</properties>
```
- 父模块中定义子模块依赖配置
```xml
<dependencyManagement>  
    <dependencies>  
		<!--常用子模块-->  
		<dependency>  
		    <groupId>com.sss</groupId>  
		    <artifactId>sss-common</artifactId>  
		    <version>${sss-common.version}</version>  
		</dependency>  
    </dependencies>  
</dependencyManagement>
```
