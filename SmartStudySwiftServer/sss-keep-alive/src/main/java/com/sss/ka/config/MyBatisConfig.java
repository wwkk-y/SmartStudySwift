package com.sss.ka.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.sss.common.mapper")
public class MyBatisConfig {
}
