package com.sss.cms.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.sss.cms.mapper")
public class MyBatisConfig {
}
