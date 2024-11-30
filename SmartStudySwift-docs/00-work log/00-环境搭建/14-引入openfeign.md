
## 父模块
- application.properties properties 需要寻找正确的版本 在spring cloud alibaba官网寻找
```properties
<spring-cloud.version>2021.0.5</spring-cloud.version>
```
- dependentManagement
```
<dependency>  
    <groupId>org.springframework.cloud</groupId>  
    <artifactId>spring-cloud-dependencies</artifactId>  
    <version>${spring-cloud.version}</version>  
    <type>pom</type>  
    <scope>import</scope>  
</dependency>
```
## 需要调用其它模块的模块
- 接口由其它模块的controller提供
- pom.xml
```xml
<dependency>  
    <groupId>org.springframework.cloud</groupId>  
    <artifactId>spring-cloud-starter-openfeign</artifactId>  
</dependency>  
<dependency>  
    <groupId>org.springframework.cloud</groupId>  
    <artifactId>spring-cloud-starter-loadbalancer</artifactId>  
</dependency>
```
- 配置类
```java
import org.springframework.cloud.openfeign.EnableFeignClients;  
import org.springframework.context.annotation.Configuration;  
  
@Configuration  
@EnableFeignClients(basePackages = "com.sss.cms.feign")  
public class FeignConfig {  
  
}
```
- 编写feign
```java
@FeignClient(name = "sss-keep-alive") // name 服务名  
public interface SssKaFeign {  
    @PostMapping("/public/msg/send")  
    RResult<Boolean> send(KaMsgSendVo msgSendVo);  
}
```
- 需要使用时注入就行了
```java
@Resource
private SssKaFeign sssKaFeign;
```
## FAQ
### Feign接口如何保证安全性
- 不公开，外网不能访问，只给内部服务访问
- 配置拦截器，解析token