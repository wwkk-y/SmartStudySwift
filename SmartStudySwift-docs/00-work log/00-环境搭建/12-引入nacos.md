## 父模块
- pom.xml
```xml
<properties>
	<!--spring cloud alibaba-->  
    <cloud-alibaba.version>2021.0.5.0</cloud-alibaba.version>  
</properties>
```

```xml
<!--spring cloud-->
<dependencyManagement>
    <dependencies>  
        <dependency>  
            <groupId>com.alibaba.cloud</groupId>  
            <artifactId>spring-cloud-alibaba-dependencies</artifactId>  
            <version>${cloud-alibaba.version}</version>  
            <type>pom</type>  
            <scope>import</scope>  
        </dependency>  
    </dependencies>  
</dependencyManagement>
```
## nacos
- 新建配置 dev.yml | prod.yml
```yml
spring:
  datasource:
    url: jdbc:mysql://localhost:13306/smart_study_swift?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&useSSL=false
    username: root
    password: wwwwww20
    druid:
      initial-size: 5 #连接池初始化大小
      min-idle: 10 #最小空闲连接数
      max-active: 20 #最大连接数
      web-stat-filter:
        exclusions: "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*" #不统计这些请求数据
      stat-view-servlet: #访问监控网页的登录用户名和密码
        login-username: druid
        login-password: druid
  redis:
    host: localhost # Redis服务器地址
    database: 0 # Redis数据库索引（默认为0）
    port: 6379 # Redis服务器连接端口
    password: # Redis服务器连接密码（默认为空）
    timeout: 300ms # 连接超时时间（毫秒）

rocketmq:
  name-server: 1.15.232.25:9876
  producer:
    group: sss-producer-group
    access-key: # 账号
    secret-key: # 密码
  consumer:
    access-key: # 账号
    secret-key: # 密码

jwt:
  tokenKey: Authorization #JWT存储的请求头
  secret: sss-security-secret #JWT加解密使用的密钥

login:
  expiration: 604800 #登录过期时间(token过期时间)(60*60*24*7)

mybatis:  
	mapper-locations:  
		- classpath:mapper/*.xml # 映射文件目录  
	    - classpath*:com/**/mapper/*.xml # 不但扫描当前JAR，扫描所有JAR,主要作用：扫描其它地方的mapper
	configuration:
		map-underscore-to-camel-case: true # 下划线转驼峰

```
## 需要使用的模块
- https://sca.aliyun.com/docs/2021/overview/what-is-sca/
- nacos注册发现和配置中心
```xml
<dependency>  
    <groupId>com.alibaba.cloud</groupId>  
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>  
</dependency>  
<dependency>  
    <groupId>com.alibaba.cloud</groupId>  
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>  
</dependency>
```
- 配置类
```java
@Configuration  
@EnableDiscoveryClient  
public class CloudConfig {  
}
```
- application-dev.yml
```yml
spring:  
  cloud:  
    nacos:  
      server-addr: 106.15.5.247:8848  
      config:  
        group: SSS_GROUP  
  config:  
    import:  
      - nacos:dev.yml?refreshEnabled=true
```
- application-prod.yml
```
spring:  
  cloud:  
    nacos:  
      server-addr: 106.15.5.247:8848  
      config:  
        group: SSS_GROUP  
  config:  
    import:  
      - nacos:prod.yml?refreshEnabled=true
```
