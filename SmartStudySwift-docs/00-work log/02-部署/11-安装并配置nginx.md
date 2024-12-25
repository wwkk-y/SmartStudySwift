## 配置反向代理
- 配置文件 `conf/nginx.conf` 里http里添加server
### 配置普通请求
```conf
server {
    listen 538; # 端口
    server_name sss-prod; # 名字

    location /ums/ { # 处理/ums/下的请求 最后的斜杠别忘了加上
        proxy_pass http://localhost:9010/; # 目标地址
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location /cms/ {
        proxy_pass http://localhost:9060/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```
#### 注意
在Nginx配置中，`location /ums/`后面的斜杠是很有意义的。这个斜杠表示该位置匹配是一个目录，并且会匹配以 `/ums/` 开头的所有请求。
- `location /ums/` 会匹配 `/ums/` 下面的所有URL路径，例如 `/ums/resource`, `/ums/api/call` 等等。
- 如果写成 `location /ums`（没有斜杠），匹配 `/ums` 但不会自动匹配 `/ums/` 或者 `/ums/anything`。它只会匹配完全符合 `/ums` 的路径。
另外，当使用 `location /ums/` 并且有默认的 try_files 指令时，Nginx 会尝试查找与请求相匹配的文件或目录。如果请求的是一个目录，它通常会查找该目录下的索引文件（如 `index.html`）。因此，末尾的斜杠对于正确处理目录请求和重定向非常重要。

### 配置长连接和负载均衡
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
        proxy_pass http://sss_keep_alive; # 使用负载均衡
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;  # 允许升级到WebSocket协议
        proxy_set_header Connection "upgrade";
        proxy_set_header Host $host;

        # 设置超时时间
        proxy_read_timeout 3600s;
        proxy_send_timeout 3600s;
    }
}
```

## 常用命令
### nginx -s reload 重载配置文件
