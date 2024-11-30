
## 背景
实现对接口的访问权限访问控制，比如一些敏感接口不让普通用户访问，不给他授权。

目前广泛采用的两种权限模型：基于**角色**的访问控制（role-based access control RBAC)和基于**属性**的访问控制（attribute-based access control ABAC）
**区别**：
- RBAC权限取决与角色，比如用户登录某管理系统的时候，允许哪些用户访问编辑哪些菜单，允许访问编辑哪些商品资源等，决定这些权限都取决于用户是哪个角色。
- ABAC权限可以根据各种属性，比如用户特征，对象特征，操作类型觉得是否授权。
**优缺点**：
- RBAC:
	- 优点：模型更加简单，更好理解，构建更快
	- 缺点：没有提供操作顺序的控制机制，**很难适应那些对操作顺序有严格要求的系统**
- ABAC:
	- 对于大型组织，基于RBCA的控制模型需要维护大量的角色和授权关系，相比而言，ABAC更加灵活。
	- 新增资源时，ABAC仅需要维护较少的资源，而RBAC需要维护所有相关的角色，ABAC可扩展性更强、更方便。
	- ABAC 有更加细粒度控制和根据上下文动态执行，RBAC只能基于静态的参数进行判断。
	- 缺点：模型构建相对比较复杂。
## 目标
- 实现RBAC模型，两部分
	- 权限解析就是解析我们用户有哪些权限
	- 权限认证就是判断用户有没有访问这个接口的权限
- 效率必须要高，因为每个请求都会执行一遍这个逻辑，不能在这里耗费太长时间，用户体验不好。
- 可靠性必须要高，因为每个请求都会执行一遍这个逻辑，必须要可靠。不能因为某些异常比如redis宕机了就不能用了，导致所有请求都执行不了了
- 可以由主动下线用户
## 方案
- JWT + redis + SpringSecurity
- 数据库结构：
	- um_user 用户
		- um_role_of_user
	- um_role 角色, name使用ROLE_前缀标识可识别为权限，比如ROLE_MANAGER可以使用注解判定
		- um_permission_of_role
	- um_permission 权限
- 每次请求时都会解析token查询用户权限
	- 可以使用redis加快查询速度(优化到5ms以内，数据库查询时间取决于用户量，比如当用户量大于100w时查询时间大于50ms，此时速度优化了到原来的1/10)
	- 但**不能依赖于redis**的可靠性，因为一旦redis不可用时，所有请求都执行不了了
	- 使用redis缓存的代价就是当redis不可用时，有多余的逻辑去处理相关异常，这样耗时比正常查数据库很长(900ms左右，是原来的18倍)，但是考虑一般情况下redis是可用的，
		- 可用时时时间缩短到原来的1/10，小概率不可用时是原来18倍的执行时间，综合考虑下这是可以接受的。
- 主动下线用户
	- 登录时 redis中设置token key，mysql登录日志中添加一条数据。
	- redis 删除token，mysql表中登录状态改成已下线

核心执行流程
- `SecurityFilterChain.filterChain()`：用于配置SecurityFilterChain实例，SpringSecurity的核心配置类，可以SpringSecurity进行路径授权配置、过滤器配置等；
	- 排除白名单`ignoreUrlsConfig.urls`, 在`properties`里配置
	- 允许跨域请求的OPTIONS请求
		- OPTIONS请求即预检请求，可用于检测服务器允许的http方法。
	- 其它所有接口都需要认证
	- 关闭跨站请求防护及不使用session
	- 设置异常时返回结果为通用格式
		 - 无权限访问: `AccessDeniedHandler`
		-  未登录或者`token`失效: `AuthenticationEntryPoint`
		- 登录失败: `AuthenticationFailureHandler`
	- 自定义拦截器JWT过滤器处理：在请求之前执行，解析`token`为用户信息保存到上下文中
		- 根据`token`获取用户名`uername`
		- 看`redis`中有没有`token`, 有的话表示在线
		- `userDetailsService.loadUserByUsername(username)`获取用户详细信息, 包括用户，角色，权限，封装成`UserDetails`返回
			- `UserDetailsService` 和 `UserDetails` 需要自行实现，此处是去数据库里查询对应数据
		- 将用户信息放到请求上下文中表示已登录
## 具体步骤
### 数据库
- 源码见 `xxx-security/sql/create.sql`
### 引入依赖
#### 父模块
- `pom.xml`
```xml
<properties>
	<jjwt.version>0.9.1</jjwt.version>
	
	<!--常用子模块-->  
	<xxx-security.version>0.0.1-SNAPSHOT</xxx-security.version>
</properties>
```

```xml
<dependencyManagement>
    <dependencies>  
        <!--JWT(Json Web Token)登录支持-->  
        <dependency>  
            <groupId>io.jsonwebtoken</groupId>  
            <artifactId>jjwt</artifactId>  
            <version>${jjwt.version}</version>  
        </dependency> 

		<!--常用子模块-->  
		<dependency>  
		    <groupId>com.xxx</groupId>  
		    <artifactId>xxx-security</artifactId>  
		    <version>${xxx-security.version}</version>  
		</dependency>
    </dependencies>  
</dependencyManagement>
```
#### 新建子模块`xxx-security`
- `pom.xml`
```xml
<dependencies>  
    <dependency>  
        <groupId>com.sss</groupId>  
        <artifactId>sss-common</artifactId>  
    </dependency>  
    <dependency>  
        <groupId>org.springframework.boot</groupId>  
        <artifactId>spring-boot-starter-security</artifactId>  
    </dependency>  
    <dependency>  
        <groupId>io.jsonwebtoken</groupId>  
        <artifactId>jjwt</artifactId>  
    </dependency>  
</dependencies>
```
### security模块
- 源码: `xxx-security`
### 需要管理权限的模块
- `pom.xml`
```
<dependency>  
    <groupId>com.sss</groupId>  
    <artifactId>sss-security</artifactId>  
</dependency>
```
- [[04-引入数据库MySQL相关]]：需要扫描Security实现的mapper
- [[05-引入redis相关]]：`security`需要使用到redis
- `application.yml`
```yml
jwt:  
  tokenKey: Authorization #JWT存储的请求头  
  secret: sss-security-secret #JWT加解密使用的密钥  
  
login:  
  expiration: 604800 #登录(token)过期时间(60*60*24*7)  
  
secure:  
  ignored:  
    urls: #安全路径白名单  
      - /swagger-ui/  
      - /swagger-resources/**  
      - /**/v2/api-docs  
      - /**/*.html  
      - /**/*.js  
      - /**/*.css  
      - /**/*.png  
      - /**/*.map  
      - /favicon.ico  
      - /druid/**  
      - /actuator/**  
      - /test/**
```
- 然后就可以在接口上使用SpringSecurity的权限注解控制权限了
- 获取当前登录的用户
```java
UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();  
UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
```

## 效果
- 实现了基于角色的权限控制模型RBAC
- 高效：使用redis加速，权限解析和权限认证平均占时5ms，非常少，如果只有这个一个逻辑理论QPS是4w，实测最简单的接口QPS 2.7w
- 可靠：redis崩溃了还是能够正常运行，只要数据库不崩溃就可以，数据库都崩溃了肯定需要人工介入了。
## FAQ
### 为什么不使用网关统一进行权限管理？
各有优劣势
首先我们需要了解权限管理具体做了什么，第一个权限解析，第二个权限认证。
- 权限解析就是解析我们用户有哪些权限
- 权限认证就是判断用户有没有访问这个接口的权限
使用网关：把权限解析和权限认证都放到网关里。由网关决定这个请求执不执行。所以我们需要去配置接口对应的权限信息。
使用我这种方案：权限管理模块只负责权限解析，权限认证放到各种需要使用到权限管理的模块。
对比之下：
- 第一点是我的方案设计起来更简单一点，因为不需要额外的去配置接口对应的权限信息，不需要多于的数据库表。
- 第二点是开发直接可以使用注解，更方便一点。使用网关的话，因为和其它模块解耦开了，不能使用SpringSecurity提供的注解判断权限，需要使用其它方式。之前实习时接触过的实现方式是把接口需要的访问权限保存到数据库里面，为了加快访问速度在redis里面存一个缓存。开发时如果增删改接口都很不方便，需要先在MySQL里添加对应接口权限数据，还要刷新缓存。
- 但是到了后期项目比较大的时候我认为还是采用网关统一处理比较好，把权限相关的放在一块，实现了解耦，因为这样更方便后期维护，比如说我们想要统一修改权限认证方式或者换个框架处理权限认证只需要改这一个模块就可以了，非常方便。
- 我之所以选取这样的方式，最主要原因还是因为我这里权限管理不但涉及http连接，还涉及websocket长连接，为了方便复用我把它设计成过滤器的模式，http和websocket都使用相同的权限校验逻辑对于我前期开发来说更方便一点。
## FAQ
- 为什么不用双token
https://juejin.cn/post/7035280102636126244