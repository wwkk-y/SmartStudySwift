- https://zhuanlan.zhihu.com/p/613527997
- https://developer.aliyun.com/article/983651
- https://springdoc.cn/spring-boot-websocket/
## FAQ
### 连接不上
需要把websocket路径配置到springsecurty白名单避免被拦截了
## Bean没有注入
- 原因：**运行时的 WebSocket 连接对象，也就是端点实例，是由服务器创建，而不是 Spring，所以不能使用自动装配**。是 “服务器会为每个连接创建一个端点实例对象”。
- 解决办法：https://springdoc.cn/spring-boot-websocket/