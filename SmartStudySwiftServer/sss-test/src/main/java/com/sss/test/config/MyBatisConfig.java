package com.sss.test.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.sss.test.mapper", "com.sss.mbg.mapper"})
public class MyBatisConfig {
}
