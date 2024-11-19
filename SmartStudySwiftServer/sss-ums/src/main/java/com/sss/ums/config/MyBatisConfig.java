package com.sss.ums.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.sss.ums.mapper")
public class MyBatisConfig {
}
