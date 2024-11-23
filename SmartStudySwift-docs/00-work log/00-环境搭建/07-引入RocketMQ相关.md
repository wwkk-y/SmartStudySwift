- 版本: 4.9.2
## 父模块
- pom.xml - properties
```xml
<!--RocketMQ-->  
<rocket-mq.version>2.2.2</rocket-mq.version>
```
- pom.xml - dependencyManagement
```xml
<dependency>  
    <groupId>org.apache.rocketmq</groupId>  
    <artifactId>rocketmq-spring-boot-starter</artifactId>  
    <version>${rocket-mq.version}</version>  
</dependency>
```

## 需要RocketMQ的模块
- `pom.xml`
```xml
<dependencies>
    <!--springboot 整合 rocketmq -->    
    <dependency>  
        <groupId>org.apache.rocketmq</groupId>  
        <artifactId>rocketmq-spring-boot-starter</artifactId>  
    </dependency>
</dependencies>
```
- `application-dev.properties`
```properties
rocketmq:  
  name-server: IP:9876  
  producer:  
    group: test-producer-group  
    access-key: # 账号  
    secret-key: # 密码    
consumer:  
    access-key: # 账号  
    secret-key: # 密码
```
- 使用生产者
```java
@Resource  
private RocketMQTemplate rocketMQTemplate;
```
- 消费者 | 监听器
```java
import lombok.extern.slf4j.Slf4j;  
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;  
import org.apache.rocketmq.spring.core.RocketMQListener;  
import org.springframework.stereotype.Component;  
  
@Slf4j  
@Component  
@RocketMQMessageListener(topic = "test-topic", consumerGroup = "test-consumer-group")  
public class RockerMQListener implements RocketMQListener<String> {  
    /**  
     *  泛型指定业务消息类型  
     *  如果写成MessageExt就可以获取消息的全部信息  
     *      发生异常就消费消息失败  
     */  
  
    @Override  
    public void onMessage(String s) {  
        log.info(s);  
    }  
}
```
## FAQ
### RocketMQTemplate NullPointerException
```java
import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;  
import org.springframework.context.annotation.Import;  
  
@Import(RocketMQAutoConfiguration.class)  
public class RocketMQConfig {  
  
}
```