> https://cloud.tencent.com/developer/article/1669256
- 依赖，common模块已经引入了
```xml
<!-- pagehelper -->
<dependency>    
    <groupId>com.github.pagehelper</groupId>    
    <artifactId>pagehelper-spring-boot-starter</artifactId>
</dependency>
```
- application.properties 可以配置在nacos
```properties
# 分页框架
pagehelper.helperDialect=mysql
pagehelper.reasonable=true
pagehelper.supportMethodsArguments=true
pagehelper.params=count=countSql
```
　- helperDialect ：指定数据库，可以不配置，pagehelper插件会自动检测数据库的类型。
　- resonable ：分页合理化参数默认false，当该参数设置为true 时，pageNum <= 0 时，默认显示第一页，pageNum 超过 pageSize 时，显示最后一页。
　- params ：用于从对象中根据属性名取值，可以配置pageNum，pageSize，count 不用配置映射的默认值。
　- supportMethodsArguments ：分页插件会根据查询方法的参数中，自动根据params 配置的字段中取值，找到合适的值会自动分页。
- 使用
```java
    PageHelper.startPage(1, 10);
    List<User> users = userMapper.selectAllUsers();
    PageInfo<User> pageInfo = new PageInfo<>(users);
```
>分页的核心就一行代码， PageHelper.startPage(page,pageSize); 这个就表示开始分页。加了这个之后pagehelper 插件就会通过其内部的拦截器，将执行的sql语句，转化为分页的sql语句。
>
>注意：使用时PageHelper.startPage(pageNum, pageSize)一定要放在列表查询的方法中，这样在**查询时会查出相应的数据量且会查询出总数**