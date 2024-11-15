- MySQL版本: 5.7
- 父模块中定义`mysql-connecter`版本信息
```xml
<properties>
	<!--mysql-->
	<mysql-connector.version>8.0.29</mysql-connector.version>
</properties>
```
- 父模块依赖配置引入mysql驱动
```xml
<dependencyManagement>  
    <dependencies>  
        <!--Mysql数据库驱动-->  
        <dependency>  
            <groupId>mysql</groupId>  
            <artifactId>mysql-connector-java</artifactId>  
            <version>${mysql-connector.version}</version>  
        </dependency>  
    </dependencies>  
</dependencyManagement>
```