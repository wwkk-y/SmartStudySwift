- MySQL版本: 5.7
## 父模块配置
- 父模块中定义版本信息
```xml
<properties>
	<!--数据库-->
	<mysql-connector.version>8.0.29</mysql-connector.version>
	<druid.version>1.2.14</druid.version>
    <mybatis.version>3.5.10</mybatis.version>
    <pagehelper-starter.version>1.4.5</pagehelper-starter.version>
    <pagehelper.version>5.3.2</pagehelper.version>
</properties>
```
- 父模块依赖配置引入配置
```xml
<dependencyManagement>  
    <dependencies>  
        <!--Mysql数据库驱动-->  
        <dependency>  
            <groupId>mysql</groupId>  
            <artifactId>mysql-connector-java</artifactId>  
            <version>${mysql-connector.version}</version>  
        </dependency>
	    <!--集成druid连接池-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>${druid.version}</version>
	    </dependency>
	    <!-- MyBatis-->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>${mybatis.version}</version>
        </dependency>
	    <!--MyBatis分页插件starter-->
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper-spring-boot-starter</artifactId>
            <version>${pagehelper-starter.version}</version>
        </dependency>
        <!--MyBatis分页插件-->
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper</artifactId>
            <version>${pagehelper.version}</version>
        </dependency>  
    </dependencies>  
</dependencyManagement>
```
## 通用模块
- `pom.xml`
```xml
<dependencies>
	<dependency>  
	    <groupId>com.github.pagehelper</groupId>  
	    <artifactId>pagehelper-spring-boot-starter</artifactId>  
	</dependency>  
	<dependency>  
	    <groupId>com.alibaba</groupId>  
	    <artifactId>druid-spring-boot-starter</artifactId>  
	</dependency>  
	<dependency>  
	    <groupId>org.mybatis</groupId>  
	    <artifactId>mybatis</artifactId>  
	</dependency>  
	<dependency>  
	    <groupId>mysql</groupId>  
	    <artifactId>mysql-connector-java</artifactId>  
	</dependency>
</dependencies>
```
## 需要使用数据库的子模块配置
- `application.yml`
```yml
mybatis:  
  mapper-locations:  
    - classpath:mapper/*.xml # 映射文件目录  
    - classpath*:com/**/mapper-mbg/*.xml # 不但扫描当前JAR，扫描所有JAR,主要作用：扫描其它地方的mapper
```
- `application-dev.yml` 或者``application-prod.yml``
```yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/db?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&useSSL=false
    username: root
    password: root
    druid:
      initial-size: 5 #连接池初始化大小
      min-idle: 10 #最小空闲连接数
      max-active: 20 #最大连接数
      web-stat-filter:
        exclusions: "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*" #不统计这些请求数据
      stat-view-servlet: #访问监控网页的登录用户名和密码
        login-username: druid
        login-password: druid
```
- 配置类
```java
import org.mybatis.spring.annotation.MapperScan;  
import org.springframework.context.annotation.Configuration;  
  
@Configuration  
@MapperScan("com.sss.test.mapper") // mapper类扫描路径，可以扫描其它JAR包的路径
public class MyBatisConfig {  
}
```
- 在mapper下写接口, resources/mapper下写xml