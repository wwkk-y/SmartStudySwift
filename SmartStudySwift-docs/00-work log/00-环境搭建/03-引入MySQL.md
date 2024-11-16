- MySQL版本: 5.7
- 父模块中定义`mysql-connecter`版本信息
```xml
<properties>
	<!--mysql-->
	<mysql-connector.version>8.0.29</mysql-connector.version>
	<druid.version>1.2.14</druid.version>
</properties>
```
- 父模块依赖配置引入mysql驱动
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
    </dependencies>  
</dependencyManagement>
```
- 需要使用mysql的子模块中配置`application.yml
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