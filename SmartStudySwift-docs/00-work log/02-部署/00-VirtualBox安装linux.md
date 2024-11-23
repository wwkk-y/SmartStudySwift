## 虚拟机安装
VirtualBox 是一款开源虚拟机软件，由Sun公司出品，现在则由Oracle进行开发。VirtualBox号称是最强的免费虚拟机软件，它性能优异且简单易用。可虚拟的系统包括Windows、Linux、MacOS、Android等操作系统！本节课程将使用VirtualBox作为虚拟机来安装Linux系统。
### VirtualBox的安装
- 我们先下载VirtualBox安装包，下载地址：[https://www.virtualbox.org/wiki/Downloads](https://www.virtualbox.org/wiki/Downloads)
![[Pasted image 20241121150648.png]]

- 下载完成后双击运行安装包一路点击下一步即可：
![[Pasted image 20241121150703.png]]
- 中途需要自定义一下安装路径：
![[Pasted image 20241121150711.png]]
- 最后点击完成，完成安装。
![[Pasted image 20241121150719.png]]
### 创建虚拟机
- 创建一个Linux虚拟机：
![[Pasted image 20241121150739.png]]
- 分配虚拟机内存大小，可以根据自己电脑配置来决定：
![[Pasted image 20241121150746.png]]
- 创建虚拟硬盘，设置好虚拟硬盘的大小，这里推荐设置`30G`以上：
![[Pasted image 20241121150757.png]]
- 设置完成后确认安装信息如下。
![[Pasted image 20241121150805.png]]
## Linux系统安装

CentOS（Community Enterprise Operating System）是Linux发行版之一，中文意思为社区企业操作系统。它是来自于商业版 Red Hat Enterprise Linux依照开放源代码规定释出的源代码所编译而成，因此具有高度稳定性且完全开源。本文将以CentOS 7.9为例来介绍Linux系统的安装。
### 下载

  这里我们将从阿里云的`开源镜像站`进行下载，网站地址为：[https://mirrors.aliyun.com](https://mirrors.aliyun.com)

这里我们选择的下载文件为`CentOS-7-x86_64-DVD-2009.iso`，下载地址：[https://mirrors.aliyun.com/centos/7.9.2009/isos/x86_64/](https://mirrors.aliyun.com/centos/7.9.2009/isos/x86_64/)
![[Pasted image 20241121150818.png]]
### 安装
- 为虚拟机添加虚拟光盘，虚拟光盘指定为我们下载的ISO镜像文件：
![[Pasted image 20241121150830.png]]
- 点击启动运行虚拟机：
![[Pasted image 20241121150839.png]]
- 运行成功后，选择`Install CentOS 7`进行安装：
![[Pasted image 20241121150848.png]]
- 选择系统安装过程中的语言，建议选择`English`选项：
![[Pasted image 20241121150855.png]]
- 需要进行设置的部分示意图：
![[Pasted image 20241121150903.png]]
- 时区设置，地区选择`Asia`，城市选择`Shanghai`：
![[Pasted image 20241121150910.png]]
- 语言支持选择安装英文、简体中文两种语言安装包：
![[Pasted image 20241121150918.png]]
- 软件安装设置选择`Server with GUI`，同时选择如图三种附加环境：
![[Pasted image 20241121150929.png]]
- 磁盘分区设置，直接设置自动分区即可：
![[Pasted image 20241121150943.png]]
- 如果你想手动分区的话可以参考下，有时候虚拟机设置的内存较小，需要创建一个较大的`swap`分区：
![[Pasted image 20241121150953.png]]
- 按如图所示进行手动分区操作；
![[Pasted image 20241121151008.png]]
- 关于分区的几个目录的说明：
	- /：根分区；
	- swap：交换分区，可以当虚拟内存使用；
	- /boot：存储系统的引导信息和内核信息；
	- /usr：存储系统应用软件安装信息；
	- /var：存储系统日志信息。

- 网络设置，设置主机名称和进行网络激活操作：
![[Pasted image 20241121151030.png]]
- 单击`Begin Installation`进行安装：
![[Pasted image 20241121151039.png]]
- 安装过程中可以设置`root`用户的密码；
![[Pasted image 20241121151051.png]]
- 完成安装后重新启动即可进入系统，第一次启动需要同意协议并完成配置：
![[Pasted image 20241121151104.png]]
- 此时宿主机还无法直接访问虚拟机，需要将虚拟机的网络模式改为桥接模式才可以。
![[Pasted image 20241121151115.png]]
## SSH客户端工具
XShell
## 其他配置
### 静态IP设置
- 将服务器改为静态IP，可以更加方便连接管理，首先打开网络设置界面；
![[Pasted image 20241121151125.png]]
- 然后打开`IPv4设置`，将自动模式改为手动，设置好IP地址、子网掩码、网关和DNS；
![[Pasted image 20241121151132.png]]
- 之后重启下网络即可生效；
![[Pasted image 20241121151140.png]]
- 如果使用动态分配IP的话，可以在命令行使用`ifconfig`命令来获取IP地址。
![[Pasted image 20241121151201.png]]
### 修改默认启动模式

如果不想默认启动图形化界面的话，可以修改默认的启动模式，因为图形化界面还是比较占用内存的，使用命令如下。
```bash
# 将默认级别修改为多用户文本模式
systemctl set-default multi-user.target
# 将默认级别修改为图形用户界面模式
systemctl set-default graphical.target
# 重启
reboot
```