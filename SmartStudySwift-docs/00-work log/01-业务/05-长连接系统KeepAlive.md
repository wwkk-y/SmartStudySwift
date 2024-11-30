- websocket
https://cloud.baidu.com/article/3319758
## 长连接权限认证
1. 规定客户端给服务端消息结构
	1. token字段，权限校验，与http请求复用，登录时获取；path字段，路由
	2. 解析token获取username
	3. 看username过期没
	4. 复用权限管理模块SpringSecurity的UserDetailService根据username获取userDetail用户详细信息以及权限
	5. 看用户的权限有没有包含path路由 // NOT DO
	6. 如果有就执行path对应业务逻辑
2. 因为长连接是用来服务器给客户端发消息的，客户端给服务器发消息还是用http，所以只用在服务端给客户端发信息客户端怎么校验权限呢？
	1. 建立连接后需要立马发一个`connet`消息进行权限校验，如果不进行这个操作，服务器会在连接建立一分钟后检查，如果没有校验就断开连接。
		1. 过期删除怎么做的：如果遍历的话时间复杂度可能过高，时间快的先处理，时间慢的后处理，典型的根据时间的先进先出，可以使用队列，多线程使用阻塞队列；建立连接的时候入队并设置一个连接时间；初始化的时候创建一个线程不断检查队列里的头部元素超一分钟没有，超了就拿出来检查，如果还没有权限验证就断开连接。
## 长连接集群负载均衡
## nginx
- 负载均衡算法，基于权重、最少连接数或IP哈希
```
upstream sss_keep_alive {
    least_conn; # 基于最少连接数负载均衡
    server 1.15.232.25:9040;
    # 如果有更多服务器，继续添加
}

server {
    listen 537;
    server_name sss;

    location / {
        proxy_pass http://sss_keep_alive;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;  # 允许升级到WebSocket协议
        proxy_set_header Connection "upgrade";
        proxy_set_header Host $host;

        # 设置超时时间
        proxy_read_timeout 3600s;
        proxy_send_timeout 3600s;

        # 重写URL，移除/ka前缀
        # rewrite ^/ka/(.*)$ /$1 break;
    }
}
```
## 集群服务消息分发
### 几个发消息场景
- 客户端 -> 自己连接的长连接服务端
	1. 规定消息结构，
		1. token字段，权限校验
		2. path字段业务逻辑，需要在长连接模块写好这个path执行什么样的逻辑，通过反射动态去调用具体方法
		3. body业务执行逻辑需要的参数
- 长连接服务端 -> 给某个用户发消息 -> 用户的多个客户端可能与集群内多个长连接服务端建立了连接：
	- 使用RocketMQ实现集群分发消息，每个长连接服务设置一个监听器，使用广播模式
	- 发消息给 topic 发消息
	- 消费者监听到消息去判断需要发消息的用户跟自己建立连接没有，建立了就发消息
- 其它服务端 -> 给某个用户发消息
	- 长连接模块提供api, 其它模块用openfeign远程
# 连接时机
- 初次建立连接: 登录后
- 断开连接: 浏览器退出自动断开
- 重连: 当意外断开连接时, 尝试重连 , 发消息时检查连接是否还在，不在就重连
- 起一个定时器不断尝试去重连就可以了，消耗的是用户的资源而不是服务器的资源，一个定时器问题不大。
## 增加单机WebSocket连接数量
https://shibd.github.io/message-center-3/
## FAQ
### 支持多少个长连接
- 单机4C8G自测支持8192个，但是连接满了并没有出现明显的性能损失。
	- 受tomcat参数影响：`server.tomcat.max-connections`
- Tomcat使用NIO多路复用，一个线程可以处理多个WebSocket连接，理论上可以支持的连接数没有固定上限，但操作系统有限制，每个 TCP 连接会占用一个文件描述符。操作系统的文件描述符限制决定了单机可以支持的最大连接数。可以通过`ulimit -n`查看当前的文件描述符限制：
- 要增加文件描述符的限制，可以编辑 `/etc/security/limits.conf` 文件
```
* soft nofile 100000
* hard nofile 100000
```
- 但是考虑到每个长连接都会消耗cpu和内存，为了避免OOM，需要`-Xmx10G -Xms8G`设置java程序的堆内存，自测10G堆内存支持4W长连接。所以需要根据实际场景来设置，我这里项目的话单机设置1w，集群部署3个长连接服务就是3w，目前来说完全够用，而且性能也还可以，不会出现明显延迟。