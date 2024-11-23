- 模块名: xxx-ums
## 目标
- [x] 登录 
- [x] 注册
- [x] 下线
- [ ] 注销
- [ ] 修改用户相关信息
- [ ] 管理员: 管理用户权限 
	- [ ] 用户CURD ums:user
	- [ ] 角色CURD ums:role:curd
	- [ ] 权限CURD ums:permission:curd
	- [ ] 用户->角色
	- [ ] 角色->权限
## 方案
- 对于邮箱使用前缀索引加快检索速度
基于权限管理模块实现登录下线功能
其它CURD