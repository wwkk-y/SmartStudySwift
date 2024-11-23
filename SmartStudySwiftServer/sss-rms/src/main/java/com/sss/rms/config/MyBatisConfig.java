package com.sss.rms.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(value = {"com.sss.rms.mapper", "com.sss.common.mapper"}) // mapper类扫描路径，可以扫描其它JAR包的路径
public class MyBatisConfig {
}
