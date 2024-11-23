## 系统服务管理
### systemctl
`systemctl`命令是`service`和`chkconfig`命令的组合体，可用于管理系统。
- 输出系统中各个服务的状态：
```
systemctl list-units --type=service
```
![[Pasted image 20241121151456.png]]
- 查看服务的运行状态：
```
systemctl status firewalld
```
![[Pasted image 20241121151515.png]]
- 关闭服务：
```
systemctl stop firewalld
```
![[Pasted image 20241121151536.png]]
- 启动服务：
```
systemctl start firewalld
```
![[Pasted image 20241121151556.png]]
- 重新启动服务（不管当前服务是启动还是关闭）：
```
systemctl restart firewalld
```
- 重新载入配置信息而不中断服务：
```
systemctl reload firewalld
```
- 禁止服务开机自启动：
```
systemctl disable firewalld
```
![[Pasted image 20241121151626.png]]
- 设置服务开机自启动：
```
systemctl enable firewalld
```
![[Pasted image 20241121151640.png]]
## 文件管理
### ls：列出文件
列出`/`目录下的文件：
```
ls -l /
```

```
ls
```

```
ll
```

![[Pasted image 20241121151726.png]]
### vi/vim 编辑文件
### pwd：当前工作目录绝对路径：
![[Pasted image 20241121151745.png]]
### cd：改变当前工作目录
```
cd /usr/local
```
### date：显示或修改系统时间与日期
```
date '+%Y-%m-%d %H:%M:%S'
```
### clear：清除屏幕信息
### man：显示指定命令的帮助信息
```
man ls
```
### who： 显示当前信息
- 查询系统处于什么运行级别：
```
who -r
```
- 显示目前登录到系统的用户：
```
who -buT
```
```
whoima
```
### mkdir：创建目录
### more：分页查看文件
例如每页10行查看`boot.log`文件：
```
more -c -10 /var/log/boot.log
```

分页查看1.txt
```
cat 1.txt |more
```
### cat：查看文件
例如查看Linux启动日志文件文件，并标明行号：
```
cat -Ab /var/log/boot.log
```
### touch：创建文件
例如创建`text.txt`文件：
```
touch text.txt
```
### rm：删除文件/目录
- 删除文件：
```
rm text.txt
```
- 强制删除某个目录及其子目录：
```
rm -rf testdir/
```
### cp：拷贝文件
例如将`test1`目录复制到`test2`目录
```
cp -r /mydata/tes1 /mydata/test2
```
### mv：移动或覆盖文件
```
mv text.txt text2.txt
```
## 压缩与解压
### tar：.tar|.gz|.bz2

- 将`/etc`文件夹中的文件归档到文件`etc.tar`（并不会进行压缩）：
```
tar -cvf /mydata/etc.tar /etc
```
- 用`gzip`压缩文件夹`/etc`中的文件到文件`etc.tar.gz`：
```
tar -zcvf /mydata/etc.tar.gz /etc
```
- 用`bzip2`压缩文件夹`/etc`到文件`/etc.tar.bz2`：
```
tar -jcvf /mydata/etc.tar.bz2 /etc
```
- 分页查看压缩包中内容（gzip）：
```
tar -ztvf /mydata/etc.tar.gz |more -c -10
```
- 解压文件到当前目录（gzip）：
```
tar -zxvf /mydata/etc.tar.gz
```
- 解压文件到指定目录（gzip）：
```
tar -zxvf /mydata/etc.tar.gz -C /mydata/etc
```
### unzip：.zip
- 解压zip
```
unzip 1.zip
```
## 进程
### free：显示系统内存状态
（单位MB）：
```
free -h
```

```
free -m
```
### jps：查看运行的Java程序
```
jps -l
```
### ps：查看进程
- 显示系统进程运行动态：
```
ps -ef
```
- 查看某个包含某个关键字进程的运行动态：
```
ps -ef | grep java
```
### kill：杀死进程
```
kill PID
```
强制杀死进程(危险)
```
kill -9 PID
```
### & 后台运行程序

```
程序 &
```

```
nohup sh 程序 > 日志文件 &
```
### top：任务管理器
查看即时活跃的进程，类似Windows的任务管理器。
## 磁盘和网络管理
### df：查看磁盘空间占用情况
```
df -hT
```
### dh：查看当前目录下的文件及文件夹所占大小
```
du -h --max-depth=1 ./*
```
### ifconfig：当前网络接口状态
### netstat：路由、连接状态

- 查看当前路由信息：
```
netstat -rn
```
- 查看所有有效TCP连接：
```
netstat -an
```
- 查看系统中启动的监听服务：
```
netstat -tulnp
```
- 查看处于连接状态的系统资源信息：
```
netstat -atunp
```
### wget：下载文件
```bash
wget url
```
## 文件上传下载
- 安装上传下载工具`lrzsz`；
```
yum install -y lrzsz
```
- 上传文件，输入以下命令`XShell`会弹出文件上传框；
```
rz
```
- 下载文件，输入以下命令`XShell`会弹出文件保存框；
```
sz fileName
```
## 软件的安装与管理
### rpm

RPM是`Red-Hat Package Manager`的缩写，一种Linux下通用的软件包管理方式，可用于安装和管理`.rpm`结尾的软件包。
- 安装软件包：
```
rpm -ivh nginx-1.12.2-1.el7_4.ngx.x86_64.rpm
```
- 模糊搜索软件包：
```
rpm -qa | grep nginx
```
- 精确查找软件包：
```
rpm -qa nginx
```
- 查询软件包的安装路径：
```
rpm -ql nginx-1.12.2-1.el7_4.ngx.x86_64
```
- 查看软件包的概要信息：
```
rpm -qi nginx-1.12.2-1.el7_4.ngx.x86_64
```
- 验证软件包内容和安装文件是否一致：
```
rpm -V nginx-1.12.2-1.el7_4.ngx.x86_64
```
- 更新软件包：
```
rpm -Uvh nginx-1.12.2-1.el7_4.ngx.x86_64
```
- 删除软件包：
```
rpm -e nginx-1.12.2-1.el7_4.ngx.x86_64
```
### yum
Yum是`Yellow dog Updater, Modified`的缩写，能够在线自动下载RPM包并安装，可以自动处理依赖性关系，并且一次安装所有依赖的软件包，非常方便！
- 安装之前我们需要安装一个epel的安装源：
```
yum install epel-release
```
- 安装软件包：
```
yum install nginx
```
- 检查可以更新的软件包：
```
yum check-update
```
- 更新指定的软件包：
```
yum update nginx
```
- 在资源库中查找软件包信息：
```
yum info nginx*
```
- 列出已经安装的所有软件包：
```
yum info installed
```
- 列出软件包名称：
```
yum list nginx*
```
- 模糊搜索软件包：
```
yum search nginx
```
- 卸载软件包：
```
yum remove nginx
```
- 有时候旧的缓存数据可能导致问题，可以尝试清理yum缓存再重试安装命令。
```
sudo yum clean all
sudo yum makecache
```
## 用户管理
### 用户信息查看
- 查看用户信息：
```
cat /etc/passwd
```
- 用户信息格式如下（密码已过滤）：
```
# 用户名:密码:用户标识号:组标识号:组注释性描述:主目录:默认shell
root:x:0:0:root:/root:/bin/bash
macro:x:1000:982:macro:/home/macro:/bin/bash
```
- 查看用户组信息：
```
cat /etc/group
```
- 用户组信息格式如下：
```
# 组名:密码:组标识号:组内用户列表
root:x:0:
docker:x:982:macro,andy
```
### passwd
用于设置用户密码：
```
passwd root
```
### su
改变用户身份（切换到超级用户）：
```
# 切换到root用户
su -
# 切换到macro用户
su macro
```
### groupadd
添加用户组，使用`-g`可以设置用户组的标志号：
```
groupadd -g 1024 macrozheng
```
### groupdel
删除用户组：
```
groupdel macrozheng
```
### useradd
添加用户，`-u`设置标志号，`-g`设置主用户组：
```
useradd -u 1024 -g macrozheng macro
```
### usermod
修改用户所属用户组：
```
usermod -g docker macro
```
### userdel
删除用户，使用`-r`可以删除用户主目录：
```
userdel macro -r
```
## 环境变量
修改环境变量后刷新
```
source /etc/profile
```
## 防火墙
如果我们在Linux服务器的某个端口上运行了个服务，需要外网能访问到，就必须通过防火墙将服务运行端口给开启。Linux中有两种防火墙软件，CentOS7.0以上使用的是firewall，CentOS7.0以下使用的是iptables
### Firewall
- 开启防火墙：
```
systemctl start firewalld
```
- 关闭防火墙：
```
systemctl stop firewalld
```
- 查看防火墙状态：
```
systemctl status firewalld
```
- 设置开机启动：
```
systemctl enable firewalld
```
- 禁用开机启动：
```
systemctl disable firewalld
```
- 重启防火墙：
```
firewall-cmd --reload
```
- 开放端口（修改后需要重启防火墙方可生效）：
```
firewall-cmd --zone=public --add-port=8080/tcp --permanent
```
- 查看开放的端口：
```
firewall-cmd --list-ports
```
- 关闭端口：
```
firewall-cmd --zone=public --remove-port=8080/tcp --permanent
```
### Iptables
#### 安装
由于CentOS7.0以上版本并没有预装Iptables，我们需要自行装。
- 安装前先关闭firewall防火墙：  
    ![](https://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/teach/refer_screen_34.png?x-oss-process=image%2Fwatermark%2Ctype_d3F5LW1pY3JvaGVp%2Csize_32%2Ctext_5YWs5LyX5Y-377yabWFjcm96aGVuZw%3D%3D%2Ccolor_FFFFFF%2Cshadow_50%2Ct_80%2Cg_se%2Cx_10%2Cy_10)
- 安装iptables:
```
yum install iptables
```
- 安装iptables-services:
```
yum install iptables-services
```
#### 使用
- 开启防火墙：
```
systemctl start iptables.service
```
- 关闭防火墙：
```
systemctl stop iptables.service
```
- 查看防火墙状态：
```
systemctl status iptables.service
```
- 设置开机启动：
```
systemctl enable iptables.service
```
- 禁用开机启动：
```
systemctl disable iptables.service
```
- 查看filter表的几条链规则(INPUT链可以看出开放了哪些端口)：
```
iptables -L -n
```
- 查看NAT表的链规则：
```
iptables -t nat -L -n
```
- 清除防火墙所有规则：
```
iptables -F
```

```
iptables -X
```

```
iptables -Z
```
- 给INPUT链添加规则（开放8080端口）：
```
iptables -I INPUT -p tcp --dport 8080 -j ACCEPT
```
- 查找规则所在行号：
```
iptables -L INPUT --line-numbers -n
```
- 根据行号删除过滤规则（关闭8080端口）：
```
iptables -D INPUT 1
```
## 电源
- 重启
```
sudo shutdown -r now
```