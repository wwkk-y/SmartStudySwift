- https://www.cnblogs.com/qinshengfei/p/18077106
## 下载
```ruby
wget https://dl.min.io/server/minio/release/linux-amd64/minio
```
- 更新权限 - 可执行文件
```shell
chmod +x minio
```
## 启动
MinIO服务器启动的基本命令及其常用参数如下：
```css
minio server [目录列表]
```
[目录列表] 是指你想要用作数据存储的本地磁盘或挂载点的路径，多个路径之间以空格分隔。例如：
```bash
minio server /data1 /data2 /data3
```
以上命令会启动一个使用 /data1、/data2 和 /data3 作为存储后端的MinIO服务，默认监听在 [http://localhost:9000](http://localhost:9000/)。

- 以下是几个常用的启动参数：
- 监听地址与端口：
```bash
minio server --address=:9000 /data
```
- **端口以及前端端口**
```bash
nohup ./minio server --address=:501 --console-address 10.0.16.10:502 ./data > ./log/minio.log &
```
这将使MinIO服务器监听所有网络接口上的9000端口。
- 启用HTTPS：
```bash
minio server --address=:9000 --certs-dir /path/to/certs /data
```
在此示例中，--certs-dir  
参数指向包含证书和私钥文件的目录，以便支持HTTPS连接。
- 设置访问密钥和秘密密钥：
```vbnet
minio server --address=:9000 --access-key=myaccesskey --secret-key=mysecretkey /data
```
这里设置了自定义的访问密钥和秘密密钥用于身份验证。
- 启用配置文件：
```bash
minio server --config-dir /path/to/config /data
```
使用指定目录下的配置文件来配置MinIO服务器。
- 区域设置：
```bash
minio server --address=:9000 --region us-west-1 /data
```
## 使用
启动后进入前台界面，建立桶，设置可读
![[Pasted image 20241128183201.png]]
这样就能公开访问了，但是写需要使用权限，需要建立一个用户，给他所有权限，写在后端写