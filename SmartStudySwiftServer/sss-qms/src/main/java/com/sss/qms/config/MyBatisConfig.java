package com.sss.qms.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.sss.qms.mapper")
public class MyBatisConfig {
}
